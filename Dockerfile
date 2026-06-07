# Etapa 1: Construcción
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
# Actualizamos e instalamos Maven dentro de la infraestructura de Render
RUN apt-get update && apt-get install -y maven
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre 
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]