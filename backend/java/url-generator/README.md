# Spring Boot to generate URLs

This module generates URLs to be injected in a performance tool like `JMETER`. 
It provides endpoints to generate all possible URLs respecting some constraints, and store them in a CSV file

## Getting started

1. Build & Test via Gradle

```shell script
cd backend/java
./gradlew :url-generator:build
``` 
2. Run Spring Boot Application

```shell script
cd backend/java
./gradlew ./gradlew :url-generator:build
java -jar ./url-generator/build/libs/url-generator-1.0-SNAPSHOT.jar
```

## Generate URLs and store them in a CSV file

Run the following curl to generate all URLs with a maximum of 3 parameters, and a maximum of 2 values per parameters, and store them in the `example.csv`:

```shell script
curl --request POST \
  --url http://localhost:8081/urls \
  --header 'content-type: application/json' \
  --data '{
	"maxParameters": 3,
	"maxValues": 2,
    "fileName": "example.csv"
}'
```
