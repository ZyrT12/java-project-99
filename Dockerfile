# ===== Build stage =====
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /project

COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle.kts ./settings.gradle.kts
COPY build.gradle.kts ./build.gradle.kts
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:21-jre
WORKDIR /opt/app

COPY --from=builder /project/build/libs/*.jar /opt/app/app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
