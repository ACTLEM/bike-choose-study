# Build the docker image of Spring Boot Elasticsearch

cd ..
./gradlew :spring-boot-elasticsearch:build
cd spring-boot-elasticsearch || exit
mkdir build/dependency
cd build/dependency || exit
unzip ../libs/*.jar
cd ../..
docker build -t actlem/spring-boot-es .
