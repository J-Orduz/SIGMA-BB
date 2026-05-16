# Frontend - Sistema de Gestión de Manteniemieto de BolivaBioingeniería (SIGMA-BB)

Este es el proyecto frontend de la aplicación, desarrollado con **React**, **TypeScript**, **Vite** y **Tailwind CSS**. Sigue las instrucciones a continuación para instalar y ejecutar el proyecto en tu entorno local.

### 1. Instalar dependencias
Instala todos los paquetes necesarios configurados en el proyecto:
```bash
npm install
```

### 2. Configurar variables de entorno (Aún no)
Crea un archivo `.env` en la raíz de la carpeta `front` y añade las variables necesarias (guíate del archivo `.env.templeate` si está disponible):
```env
pendiente
```
### 3. Iniciar el servidor de desarrollo
Para levantar la aplicación en modo desarrollo, ejecuta:
```bash
npm run dev
```
Abre tu navegador en la ruta local que te indique la terminal (usualmente `http://localhost:5173`).

## Arquitectura de Carpetas

El proyecto sigue una estructura orientada a **Features (Características/Módulos)** para facilitar el escalado y mapear directamente los Requisitos Funcionales (RF) del sistema:

```bash
src/
├── features/
│   ├── auth/                 ← RF-49, RF-50, RF-51, RF-52, RF-53, RNF-23
│   │   ├── components/
│   │   ├── hooks/
│   │   └── services/
│   ├── work-orders/          ← RF-01 al RF-07, RF-31, RF-32, RF-33, RF-34, RNF-02, RNF-16
│   │   ├── components/
│   │   ├── hooks/
│   │   └── services/
│   ├── clients/              ← RF-08, RF-53
│   │   ├── components/
│   │   ├── hooks/
│   │   └── services/
│   ├── reports/              ← RF-09, RF-10, RF-11, RF-13, RF-15, RNF-06, RD-01
│   │   ├── components/
│   │   ├── hooks/
│   │   └── services/
│   └── equipments/           ← RD-03, RD-04, RD-05    
│       ├── components/
│       ├── hooks/
│       └── services/
├── shared/
│   ├── components/           ← Botones, inputs, modales reutilizables
│   ├── hooks/                ← useAuth, useFetch, useRole
│   └── utils/                ← Formateo de fechas, validaciones
├── router/                   ← Rutas protegidas por rol
├── store/                    ← Estado global (Zustand)
├── App.tsx                   ← Componente raíz
├── index.css                 ← Directivas de Tailwind CSS
└── main.tsx                  ← Punto de entrada que renderiza la app en el HTML
```