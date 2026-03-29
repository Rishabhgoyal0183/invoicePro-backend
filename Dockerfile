# -------- Stage 1 --------
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .

# Use installed Gradle (no download needed)
RUN gradle clean build -x test

# -------- Stage 2 --------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]