# Etapa 1: Compilación
FROM maven:3.8.5-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -Dmaven.test.skip=true -Dspring.main.lazy-initialization=true

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080

# Usamos el formato limpio que usaste en Neon
ENTRYPOINT ["java", "-jar", "app.jar"]