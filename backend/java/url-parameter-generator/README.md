# Spring Boot to generate URL Parameters

This module generates URL parameters to be injected in a performance tool like `JMETER`. 
It provides endpoints to generate all possible URL parameters respecting some constraints, and store them in a CSV file

## Getting started

1. Build & Test via Gradle

```shell script
cd backend/java
./gradlew :url-parameter-generator:build
``` 
2. Run Spring Boot Application

```shell script
cd backend/java
./gradlew ./gradlew :url-parameter-generator:build
java -jar ./url-generator/build/libs/url-parameter-generator-1.0-SNAPSHOT.jar
```

## Generate URL parameters and store them in a CSV file

Run the following curl to generate all URL parameters with a maximum of 3 parameters, and a maximum of 2 values per parameters, and store them in the `url_parameters.csv`:

```shell script
curl --request POST \
  --url http://localhost:8081/url-parameters \
  --header 'content-type: application/json' \
  --data '{
	"maxParameters": 3,
	"maxValues": 2,
    "fileName": "url_parameters.csv"
}'
```
