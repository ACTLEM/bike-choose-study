# Build the docker image of Spring Boot Postgres

cd ..
./gradlew :spring-boot-postgres:build
cd spring-boot-postgres || exit
mkdir build/dependency
cd build/dependency || exit
unzip ../libs/*.jar
cd ../..
docker build -t actlem/spring-boot-postgres .
