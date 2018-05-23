#!/bin/bash

set -eu

docker-compose down || true
mvn clean package

docker-compose up --build -d


# docker build -t postgre-db Docker/.
# docker run -ti -d  --rm --net host -p 5432:5432 --name postgre-db postgre-db


# SVR="service-registry"
# USR="user"
# APIG="api-gateway"

# docker build -t service-registry $SVR/target/.
# docker run -ti -d  --rm --net host  -p 8761:8761 --name service-registry service-registry


# docker build -t user $USR/target/.
# docker run -ti -d -p 8180:8180 --rm --net host --name $USR-service $USR

# docker build -t api-gateway $APIG/target/.
# docker run -ti -d -p 8080:8080 --rm --net host --name $APIG-service $APIG




