FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Instalar Maven y dependencias necesarias
RUN apk add --no-cache maven git

# Copiar pom.xml
COPY ./pom.xml .

# Copiar código fuente
COPY ./src ./src

# Compilar y empaquetar con Maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR desde builder
COPY --from=builder /build/target/sigma-bb-0.0.1-SNAPSHOT.jar app.jar

# Crear directorio de logs
RUN mkdir -p ./logs

# Variables de entorno
ENV PORT=8100
ENV JAVA_OPTS=""

EXPOSE 8100

# Ejecutar con spring.config.additional-location para soporte de variables de entorno mejorado
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
