![Build Spring Boot with Elasticsearch](https://github.com/ACTLEM/bike-choose-study/workflows/Build%20Spring%20Boot%20with%20Elasticsearch/badge.svg)

# Spring Boot with Elastisearch

This module implements the "Choice of bike" feature on the backend server side.
It retrieves via the HTTP REST API the necessary data to be displayed on the website.

It uses Spring Boot and Spring Data Elasticsearch 2.2.6 and Elasticsearch 7.6.1.

The build is done via Gradle Kotlin.

## Elastisearch configuration

A cluster of 3 nodes is created via a `docker-compose.yml` file. 
There is only one index for `bike` products. The configuration is the default one for Spring Data Elasticsearch: 5 shards and 1 replica.

## Getting started

1. Build & Test via Gradle

```shell script
cd backend/java
./gradlew :spring-boot-elasticsearch:build
``` 

2. Run your Elasticsearch cluster

```shell script
docker-compose up -d
```

3. Check that Elasticsearch is running

```shell script
docker ps -a

CONTAINER ID              IMAGE                                                 COMMAND                  CREATED             STATUS              PORTS                              NAMES
<container_id_es1>        docker.elastic.co/elasticsearch/elasticsearch:7.6.1   "/usr/local/bin/dock…"   xx minutes ago      Up xx minutes       9200/tcp, 9300/tcp                 es02
<container_id_es2>        docker.elastic.co/elasticsearch/elasticsearch:7.6.1   "/usr/local/bin/dock…"   xx minutes ago      Up xx minutes       0.0.0.0:9200->9200/tcp, 9300/tcp   es01
<container_id_es3>        docker.elastic.co/elasticsearch/elasticsearch:7.6.1   "/usr/local/bin/dock…"   xx minutes ago      Up xx minutes       9200/tcp, 9300/tcp                 es03
```

Volumes:
 
- Volumes will be stored in `/var/lib/docker/volumes/springbootelasticsearch_esdata01/_data/` directory for es1
- To reset volumes run `docker-compose down -v`

4. Run Spring Boot Application

```shell script
cd backend/java
./gradlew :spring-boot-elasticsearch:build
java -jar ./spring-boot-elasticsearch/build/libs/spring-boot-elasticsearch-1.0-SNAPSHOT.jar
```

## Create a Bike

Run the following curl:

```shell script
curl --request POST \
  --url http://localhost:8080/bikes \
  --header 'content-type: application/json' \
  --data '{
	"label": "My new bike",
	"types": ["URBAN","ELECTRIC"],
	"genders": ["MEN","WOMEN"],
	"brand": "TREK",
	"frameMaterial": "CARBON",
	"forkMaterial": "CARBON",
	"brake": "HYDRAULIC_DISC",
	"cableRouting": "MIX",
	"chainset": "SINGLE",
	"groupsetBrand": "SHIMANO",
	"wheelSize": "MM_650C",
	"modelYear": "2019",
	"colors": ["BLACK"]
}'
```

## Get bikes

### Pagination

Run the following curl to get the first page of bikes (by default the size of a page = 20):

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0'
```

Run the following curl to get the second page of bikes with 10 results per page:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=1&size=10'
```

### Sorting

Run the following curl to get the first page of bikes sorted by brand name by ascending order:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0&sort=brand'
```

or

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0&sort=brand,asc'
```

Run the following curl to get the first page of bikes sorted by brand name by descending order:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0&sort=brand,desc'
```

### Filtering

Run the following curl to get the first page of bikes with types are equals to URBAN or ELECTRIC:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0&types=URBAN,ELECTRIC'
```

Here, the filters that can be used:

| Name           |
|----------------|
| types          |
| genders        |
| brands         |
| frames         |
| forks          |
| brakes         |
| cableRoutings  |
| chainsets      |
| groupsets      |
| wheelSizes     |
| colors         |

### Get possible facets

Run the following curl:

```shell script
curl --request GET \
  --url http://localhost:8080/bikes/facets?brands=TREK
```

All facets are "multi select", so the filter linked to a facet is not applied to it. 
It means that if the brand `TREK` is selected, the filter `brand=TREK` will be applied to all facets to get possible values except the facet `BRAND`.

### Search bikes

It is a combination of filtering and finding facets, so it returns a page of bikes and the list of facets, according to filters.

Run the following curl:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes/search?page=0&types=URBAN,ELECTRIC&brands=BTWIN,TREK&genders=BOYS'
```

## Logs

To activate the logs of the Elasticsearch queries, add the following lines in the `application.properties` file:

```
logging.level.org.elasticsearch.client=TRACE
logging.level.org.apache.http=TRACE
```

## Container (Docker)

### Simple container

To build the `Docker` image, run:

```shell script
cd backend/java/spring-boot-elasticsearch
./build_docker_sb_es.sh
```

It will create a Docker image from `adoptopenjdk:13-jre-openj9` 

To run it in the host network:

```shell script
docker run --rm --network host -p 8080:8080 -t actlem/spring-boot-es
```

### Docker Compose

In order limit resources and set up a load balancer for performance testing, use the `docker-compose-with-sb.yml` file. 
By default, two `Elasticsearch Spring Boot` instances based on the previously built image are launched. 
A `Nginx` container is created to manage the load balancer.

To run it:

```shell script
docker-compose -f docker-compose-with-sb.yml --compatibility up -d
````

All queries remain as above (use `http://localhost:8080/bikes`).

To see the logs of a container (here the first spring boot instance):

```shell script
docker-compose -f docker-compose-with-sb.yml logs --follow spring-boot-es1
```
