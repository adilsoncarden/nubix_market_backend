# Etapa 1: Compilación sin inicializar el contexto de Spring
FROM maven:3.8.8-eclipse-temurin-17 AS build
COPY . .
# Forzamos a saltar tests y cualquier inicialización de beans en la compilación
RUN mvn clean package -Dmaven.test.skip=true -Dspring.main.lazy-initialization=true

# Etapa 2: Ejecución (Aquí es donde Render inyectará tus variables)
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080

# Nos aseguramos de pasar el puerto dinámico de Render al arrancar
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]