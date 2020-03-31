![](https://github.com/ACTLEM/bike-choose-study/workflows/Build%20Spring%20Boot%20with%20Elasticsearch/badge.svg)

# Spring Boot with Elastisearch

This module implements the "Choice of bike" feature on the backend server side.
It retrieves via the HTTP REST API the necessary data to be displayed on the website.

It uses Spring Boot and Spring Data Elasticsearch 2.2.5 and Elasticsearch 7.6.1.

The build is done via Gradle Kotlin.

## Elastisearch configuration

A cluster of 3 nodes is created via a `docker-compose.yml` file. 
There is only one index for `bike` products. The configuration is the default one for Spring Data Elasticsearch: 5 shards and 1 replica.

## Getting started

1. Build & Test via Gradle

```shell script
./gradlew test
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
./gradlew build
java -jar ./build/libs/elasticsearch-0.0.1-SNAPSHOT.jar 
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
	"genders": ["MENS","WOMENS"],
	"brand": "TREK",
	"frameMaterial": "CARBON",
	"forkMaterial": "CARBON",
	"brake": "HYDRAULIC_DISC",
	"cableRouting": "MIX",
	"chainset": "SINGLE",
	"groupSetBrand": "SHIMANO",
	"wheelSize": "MM_650C",
	"modelYear": "2019",
	"colors": ["BLACK"]
}'
```

## Get bikes

Run the following curl:

```shell script
curl --request GET \
  --url 'http://localhost:8080/bikes?page=0'
```

## Get possible facets

Run the following curl:

```shell script
curl --request GET \
  --url http://localhost:8080/bikes/facets
```


