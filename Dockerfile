# Based on article from https://solureal.com/blogs/how-to-use-google-distroless-docker-images-with-spring-boot
FROM openjdk:21-jdk-slim as builder

WORKDIR /app

COPY gradle gradle/
COPY *.gradle gradlew ./

RUN ./gradlew --help > /dev/null

COPY src src/

RUN ./gradlew clean build -x test
RUN mkdir /app/extracted && \
    java -Djarmode=layertools -jar /app/build/libs/weesvc-0.0.1-SNAPSHOT.jar extract --destination /app/extracted

# Note: don't run as root
FROM gcr.io/distroless/java21-debian12:nonroot
#FROM gcr.io/distroless/java21-debian12:debug

EXPOSE 8080
WORKDIR /app

COPY --from=builder /app/extracted/dependencies /app/
COPY --from=builder /app/extracted/spring-boot-loader /app/
COPY --from=builder /app/extracted/snapshot-dependencies /app/
COPY --from=builder /app/extracted/application /app/

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
