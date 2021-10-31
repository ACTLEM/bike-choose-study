![Build Spring Boot with Postgres](https://github.com/ACTLEM/bike-choose-study/workflows/Build%20Spring%20Boot%20with%20Postgres/badge.svg)

# Spring Boot with Postgres

This module implements the "Choice of bike" feature on the backend server side.
It retrieves via the HTTP REST API the necessary data to be displayed on the website.

It uses Spring Boot and Spring Data postgres 2.2.6, postgres 8.5.0 and ZooKeeper 3.6.0.

The build is done via Gradle Kotlin.

## postgres configuration

A cluster of 3 ZooKeeper and 3 postgres nodes is created via a `docker-compose.yml` file. 
The embedded ZooKeeper in postgres is not used as it is not recommended for performances and memory issues.
The `ZK_HOST` variable is set, so the postgres nodes will launch in `cloud` mode. 

## Getting started

1. Build & Test via Gradle

```shell script
cd backend/java
./gradlew :spring-boot-postgres:build
``` 

2. Run your postgres Cloud

```shell script
docker-compose up -d
```

3. Check that postgres is running

```shell script
docker ps -a

CONTAINER ID                IMAGE               COMMAND                  CREATED             STATUS              PORTS                                                  NAMES
<container_id_postgres>         postgres:14-alpine       "docker-entrypoint.s…"   17 seconds ago   Up 16 seconds                0.0.0.0:5432->5432/tcp   postgres_container
```

Volumes:
 
- Volumes will be stored in `/var/lib/docker/volumes/spring-boot-postgres_postgresdata/_data/` directory for postgres1
- To reset volumes run `docker-compose down`

4. Run Spring Boot Application

```shell script
cd backend/java
./gradlew :spring-boot-postgres:build
java -jar ./spring-boot-postgres/build/libs/spring-boot-postgres-1.0-SNAPSHOT.jar
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

## Container (Docker)

### Simple container

To build the `Docker` image, run:

```shell script
cd backend/java/spring-boot-postgres
./build_docker_sb_postgres.sh
```

It will create a Docker image from `adoptopenjdk:13-jre-openj9` 

To run it in the host network:

```shell script
docker run --rm --network host -p 8080:8080 -t actlem/spring-boot-postgres
```

### Docker Compose

In order limit resources and set up a load balancer for performance testing, use the `docker-compose-with-sb.yml` file. 
By default, two `postgres Spring Boot` instances based on the previously built image are launched. 
A `Nginx` container is created to manage the load balancer.

To run it:

```shell script
docker-compose -f docker-compose-with-sb.yml --compatibility up -d
````

All queries remain as above (use `http://localhost:8080/bikes`).

To see the logs of a container (here the first spring boot instance):

```shell script
docker-compose -f docker-compose-with-sb.yml logs --follow spring-boot-postgres1
```
