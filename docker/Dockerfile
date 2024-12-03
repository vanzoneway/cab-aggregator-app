FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle clean build -x test --no-daemon

FROM openjdk:21-slim AS builder
ARG SERVICE_NAME
WORKDIR /app
COPY --from=build /app/build/libs/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar ./${SERVICE_NAME}.jar
RUN java -Djarmode=layertools -jar ${SERVICE_NAME}.jar extract

FROM openjdk:21-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

