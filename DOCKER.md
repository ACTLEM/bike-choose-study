# Docker Optimization

Some tips and tricks to optimize Docker images used from an eco-responsible point of view.

## Goals

### Container Image Size

In a CI/CD context, the size of the Docker image can quickly impact both build time and deployment speed during push and pull phases via the network. 
Storage can also be negatively impacted even though from an eco-responsible point of view, this is not where the impact is strongest.

### Resources Usage

CPU and memory usage may vary depending on the choice of the docker image. 
Saving resources can reduce the number of servers needed in addition to reducing energy and water consumption in the data center.

### Container Building and Starting

When building the docker image, the use of the layer cache can be optimized for a faster and less resource-intensive build and can improve container startup. 
Of course the gain is not necessarily huge but the implementation can be very simple and is done once and for all.

## Spring Boot Application

### Container Image Size

Using the `jdk-13` image to build the image of our spring-boot is the first solution that comes to mind, here the `Dockerfile`:
```dockerfile
FROM openjdk:13-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

If we look at the size of the image, this is what we get:

```shell script
REPOSITORY                                      TAG                 IMAGE ID            CREATED             SIZE
actlem/spring-boot-es                           latest              xxxxxxxxxxxx        xx minutes ago      538MB
```

But we know that Alpine Linux images are much smaller than most distribution base images. So let's try it:

```dockerfile
FROM openjdk:13-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

As a result, 30% less, more than 150MB saved per image pull or push:

```shell script
REPOSITORY                                      TAG                 IMAGE ID            CREATED             SIZE
actlem/spring-boot-es                           latest              xxxxxxxxxxxx        xx minutes ago      384MB
```

But do we need a jdk in the runtime? 
We can indeed use a jre image, for that the image proposed by the `adoptopenjdk` community does the job perfectly:

```dockerfile
FROM adoptopenjdk:13-jre-hotspot
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

With an image size almost divided by 2 compared to the original image, so far so good:

```shell script
REPOSITORY                                      TAG                 IMAGE ID            CREATED             SIZE
actlem/spring-boot-es                           latest              xxxxxxxxxxxx        xx minutes ago      286MB
```

### Resources Usage

If we take a detailed look at the consumption-resource part, we see with the image `13-jre-hotspot` via `docker stats`:

```shell script
CONTAINER ID        NAME                CPU %               MEM USAGE / LIMIT     MEM % 
0c17ef4fef78        wonderful_elion     0.19%               196.8MiB / 15.49GiB   1.24% 
```

These values are obtained without loading (coming later).

Let us now put ourselves in a first mobile spirit, therefore with a strong will to reduce memory and cpu consumption. 
This is where the OpenJ9 project comes in, which develops a JVM optimized especially for smartphones.

```dockerfile
FROM adoptopenjdk:13-jre-openj9
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

And the `docker stats` gives:

```shell script
CONTAINER ID        NAME                CPU %               MEM USAGE / LIMIT     MEM % 
347bacb5a9f9        serene_saha         0.29%               97.52MiB / 15.49GiB   0.61% 
```

The memory usage is divided by 2 but the CPU usage is larger. Test performances will give us more information.
It's slightly smaller in size, by the way: 264MB.

### Container Building and Starting

Even if the gain is not important, optimization of the docker image build can be done simply by observing that the most frequently modified classes are also the lightest.
We can therefore optimize the build by separating the dependencies that are very little modified in another layer. This will allow a more optimal use of its cache.

We need to unpack the Spring Boot jar at first:

```shell script
mkdir build/dependency
cd build/dependency
(jar -xf |unzip) ../libs/*.jar
```

Build with the following Dockerfile:

```dockerfile
FROM adoptopenjdk:13-jre-openj9
ARG DEPENDENCY=build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.actlem.springboot.elasticsearch.ElasticsearchApplication"]
```

Now, the build time of the docker image will be slightly better, but again the gain is not necessarily very significant.



