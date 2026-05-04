# Etapa de compilación (Build Stage)
FROM eclipse-temurin:17-jdk-alpine AS buildstage
WORKDIR /app

# Copiar el wrapper de maven y el pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permisos de ejecución al wrapper
RUN chmod +x ./mvnw

# Descargar dependencias (mejora el uso de caché de Docker)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src src

# Compilar el proyecto empaquetando el JAR
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución (Run Stage)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de compilación
COPY --from=buildstage /app/target/hotel-booking-service-0.0.1-SNAPSHOT.jar /app/hotel-booking-service.jar

# Copiar la carpeta Wallet al contenedor
COPY Wallet_MIDBDUOC /app/wallet

# Definir la variable de entorno para que Spring Boot encuentre la Wallet
ENV WALLET_PATH=/app/wallet

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/hotel-booking-service.jar"]
