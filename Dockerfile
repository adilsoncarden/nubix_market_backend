# Usamos una imagen ligera de Java solo para correr la aplicación
FROM eclipse-temurin:17-jre 

# Creamos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo JAR que ya compilaste en tu máquina local
COPY target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para arrancar el backend
ENTRYPOINT ["java", "-jar", "app.jar"]