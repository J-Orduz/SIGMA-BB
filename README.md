# SIGMA-BB

SIGMA-BB es el Sistema de Gestión de Mantenimiento de BolivarBioingenieria. El proyecto integra una aplicacion frontend, un backend Spring Boot, base de datos PostgreSQL, autenticacion con Keycloak y servicios de soporte para la operacion del sistema.

## Componentes Principales

```bash
SIGMA-BB/
├── Backend/                 # API principal del sistema
│   └── sigma-bb/            # Proyecto Spring Boot
├── Frontend/                # Aplicacion web React + TypeScript + Vite
├── DataBase/                # Scripts de inicializacion y versionamiento de base de datos
├── keycloak/                # Temas, proveedores y configuracion de autenticacion
├── docker-compose.yaml      # Orquestacion local de servicios
├── .env_template            # Plantilla de variables de entorno
└── .env                     # Variables locales del entorno
```

## Stack Tecnologico

- Frontend: React, TypeScript, Vite, Tailwind CSS y Zustand.
- Backend: Java 21, Spring Boot, Spring Data JPA, Spring Security, OAuth2 Resource Server, MapStruct y Lombok.
- Base de datos: PostgreSQL 17.
- Autenticacion: Keycloak.
- Infraestructura local: Docker Compose.
- Mensajeria: RabbitMQ.
- Administracion de base de datos: pgAdmin.

## Servicios Docker

El archivo `docker-compose.yaml` levanta los siguientes servicios:

- `postgres17`: base de datos principal.
- `pgadmin`: consola de administracion para PostgreSQL.
- `rabbitmq`: broker de mensajeria con panel de administracion.
- `keycloak`: servidor de identidad y autenticacion.
- `bolivarbioingenieria-app`: backend de SIGMA-BB.

## Para Ejecutar el Proyecto

1. Copie la plantilla de variables de entorno:

```bash
cp .env_template .env
```

2. Ajuste los valores del archivo `.env` segun el entorno local.

3. Levante los servicios principales en Docker:

```bash
docker compose up -d
```

4. Instale y ejecute el frontend:

```bash
cd Frontend
npm install
npm run dev
```

5. Abra la URL local indicada por Vite, normalmente:

```bash
http://localhost:5173
```

## Backend

El backend se encuentra en `Backend/sigma-bb` y puede ejecutarse desde Docker Compose o directamente con Maven:

```bash
cd Backend/sigma-bb
./mvnw spring-boot:run
```

En Windows:

```bash
cd Backend/sigma-bb
mvnw.cmd spring-boot:run
```

## Frontend

El frontend se encuentra en `Frontend`. Su documentacion especifica esta en `Frontend/README.md`.

Comandos principales:

```bash
npm install
npm run dev
npm run build
```

## Autenticación

La autenticación se gestiona con Keycloak. El frontend obtiene el token de acceso desde el realm configurado y el backend valida las solicitudes protegidas mediante OAuth2 Resource Server.

Los recursos personalizados de Keycloak viven en:

```bash
keycloak/
├── configuration/
├── providers/
└── themes/
```

## Base de Datos

Los scripts de inicializacion y versiones de base de datos se encuentran en `DataBase`. Docker Compose monta la version `DataBase/v4/initdb` para inicializar PostgreSQL.

## Notas de Desarrollo

- Mantener las variables sensibles fuera del repositorio.
- Usar `.env_template` como referencia para nuevos entornos.
- Ejecutar `npm run build` en `Frontend` antes de entregar cambios visuales o de rutas.
- Revisar `Backend/sigma-bb/REQUESTS_BY_DOMAIN.md` para ejemplos de uso de endpoints por dominio.
- Activar el `Group Membership` en los Mappers de Clients en Keycloak.
