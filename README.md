export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

export PATH=$JAVA_HOME/bin:$PATH

mvn spring-boot:run 2>&1 | tee crash-report.log

# 1. Stop the database and delete the data volume

docker-compose down -v #

2. Start a fresh, empty database

3. docker-compose up -d

4.

spring.jpa.hibernate.ddl-auto=create

npx ng serve --host 0.0.0.0

sudo apt-get update && sudo apt-get install -y openjdk-17-jdk

docker run -d --name oracle-db -p 1521:1521 -e ORACLE_PWD=VertexSecurePass123! container-registry.oracle.com/database/free:latest

docker rm -f oracle-db

docker start oracle-db

2R89SBZjPo