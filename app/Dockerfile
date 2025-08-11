FROM gradle:8.7.0-jdk-21 AS build
WORKDIR /home/gradle/project

COPY . .

RUN gradle :app:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /home/gradle/project/app/build/libs/*.jar app.jar

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
