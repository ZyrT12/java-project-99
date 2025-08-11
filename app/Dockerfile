FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle.kts ./
COPY app/build.gradle.kts app/build.gradle.kts
COPY app/src app/src

RUN chmod +x ./gradlew
RUN ./gradlew :app:clean :app:shadowJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /opt/app

COPY --from=builder /app/app/build/libs/app-all.jar /opt/app/app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "/opt/app/app.jar"]
