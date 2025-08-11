FROM eclipse-temurin:21-jdk AS builder
WORKDIR /project

COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle.kts ./
COPY app/build.gradle.kts app/build.gradle.kts

COPY app ./app

RUN chmod +x ./gradlew
RUN ./gradlew :app:clean :app:installDist --no-daemon

# ---- runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /opt/app
COPY --from=builder /project/app/build/install/app /opt/app

ENV PORT=8080
EXPOSE 8080
CMD ["/opt/app/bin/app"]
