# Build: skips tests (-x test) for faster image builds; run `./gradlew test` in CI.
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
COPY database ./database
RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/build/libs/identity-service-*.jar app.jar
COPY database/ ./database/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
