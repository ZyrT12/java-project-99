# app/Dockerfile
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /project

COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle.kts ./settings.gradle.kts
COPY build.gradle.kts ./build.gradle.kts
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon clean installDist

FROM eclipse-temurin:21-jre
WORKDIR /opt/app
ARG APP_NAME=app
COPY --from=builder /project/build/install/${APP_NAME}/ /opt/app/
ENV PORT=8080
EXPOSE 8080
CMD ["/opt/app/bin/app"]
