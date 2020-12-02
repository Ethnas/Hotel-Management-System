# syntax=docker/dockerfile:experimental

## Build inside docker with a maven container
FROM maven:3.6.3-jdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -f pom.xml clean package

## Copy isolated build from intermediate container(useful on server)
FROM payara/server-full:5.201 AS prod
## Fixes clock in payara log
ENV TZ Europe/Oslo
COPY --from=build /app/target/HMSProject-1.war $DEPLOY_DIR/HMSProject-1.war
