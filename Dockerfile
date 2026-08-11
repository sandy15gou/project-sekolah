FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/project-tentang-sekolah-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx384m -XX:+UseG1GC"
CMD ["java", "-jar", "app.jar"]