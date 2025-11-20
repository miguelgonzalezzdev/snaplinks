# SnapLinks

**SnapLinks** es un **acortador de URLs** desarrollado con [**React**](https://react.dev/), [**Spring Boot**](https://spring.io/projects/spring-boot) y [**PostgreSQL**](https://www.postgresql.org/). Permite crear URLs cortas, redirigirlas, generar QR codes y consultar estadísticas de uso.

## 🚀 Características principales

- **Acortamiento de URLs:** Convierte cualquier URL larga en un enlace corto, único y fácil de compartir.
- **Redirección rápida:** Las URLs cortas redirigen automáticamente al destino original mediante HTTP 302.
- **QR Code dinámico:** Genera códigos QR listos para escanear desde dispositivos móviles.
- **Sistema de autenticación:** Gestiona usuarios con JWT, protegiendo el acceso a URLs privadas.
- **Estadísticas de uso:** Muestra accesos, geolocalización y dispositivos de los visitantes.

## 🏗️ Tecnologías utilizadas

### Frontend

- **Lenguaje:** TypeScript
- **Framework:** React
- **Estilos:** Tailwind CSS
- **Routing:** React Router DOM
- **HTTP Client:** Axios
- **Estados globales:** Zustand
- **Tests:** Vitest

### Backend

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3
- **Base de datos:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Seguridad:** Spring Security + JWT
- **Build:** Maven
- **Tests:** JUnit + Mockito
- **Documentación:** Swagger UI + OpenAPI

## 📂 Estructura del proyecto

    frontend/
    └─ snaplinks/src/
       ├─ api/                → Definición del cliente HTTP (Axios)
       ├─ components/         → Componentes del proyecto
       ├─ config/             → Configuraciones globales
       ├─ hooks/              → Hooks personalizados 
       ├─ router/             → Configuración de rutas y navegación (React Router)
       ├─ services/           → Lógica de negocio
       ├─ store/              → Gestión de estado global (Zustand)
       └─ types.d.ts          → Definiciones de tipos globales de TypeScript
    backend/
    └─ snaplinks/src/
        └─ main/java/com/github/miguelgonzalezzdev/snaplinks/
            ├─ config/        → Configuraciones globales (seguridad, beans, etc)
            ├─ controllers/   → Maneja rutas HTTP y redirecciones
            ├─ dtos/          → Objetos para requests/responses (API)
            ├─ models/        → Entidades JPA que representan tablas
            ├─ repositories/  → Acceso a datos con JPA
            ├─ schedules/     → Tareas cron a ejecutar
            └─ services/      → Lógica de negocio 

## 📌 Mejoras futuras

- **Cacheo con Redis:** Almacenar temporalmente las URLs más usadas para mejorar el rendimiento y reducir la carga en la base de datos.

---

Diseñado con ❤️ por [**Miguel**](https://miguelgonzalezdev.es)
