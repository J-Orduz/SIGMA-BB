FROM eclipse-temurin:21 AS build

WORKDIR /app

# 1. Copiar archivos de dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 2. Descargar dependencias
RUN ./mvnw dependency:go-offline

# 3. Ahora copiar el código
COPY src src

# 4. Compilar
RUN ./mvnw clean package -DskipTests

# Runtime
FROM eclipse-temurin:21

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]