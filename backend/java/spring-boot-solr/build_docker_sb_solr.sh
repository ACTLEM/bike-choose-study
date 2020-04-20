# Build the docker image of Spring Boot Solr

cd ..
./gradlew :spring-boot-solr:build
cd spring-boot-solr || exit
mkdir build/dependency
cd build/dependency || exit
unzip ../libs/*.jar
cd ../..
docker build -t actlem/spring-boot-solr .
