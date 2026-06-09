# 🏪 Nubix Market - Backend API

**API REST para la plataforma de compras de Nubix Market**, construida con Spring Boot, desarrolla la lógica de negocio para autenticación, gestión de usuarios e integración con base de datos.

---

## 📋 Tabla de Contenidos

- [Acerca de](#acerca-de)
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Endpoints API](#endpoints-api)
- [Autores](#autores)

---

## 📌 Acerca de

### Propósito

Backend de **Nubix Market**, un API REST que proporciona servicios de autenticación, gestión de usuarios y operaciones de negocio para la plataforma integral de compras. Desarrollado con **Spring Boot** para garantizar escalabilidad, seguridad y mantenibilidad.

### Características del Backend

- ✅ Autenticación con JWT (JSON Web Tokens)
- ✅ Gestión segura de usuarios
- ✅ Validación de datos robusta
- ✅ Conexión a base de datos PostgreSQL
- ✅ Filtros de seguridad personalizados
- ✅ Manejo de excepciones centralizado

---

## ✨ Características

- 🔐 **Autenticación JWT** - Tokens seguros y validación de sesiones
- 👤 **Gestión de Usuarios** - Registro, login y perfil de usuario
- 🔒 **Seguridad** - Encriptación de contraseñas y filtros de autorización
- 🗄️ **Base de Datos** - Integración con PostgreSQL (Supabase)
- ⚙️ **Configuración centralizada** - Manejo de propiedades por ambiente
- 📝 **Logging** - Trazabilidad de operaciones

---

## 🛠️ Tecnologías

| Categoría                | Tecnología            | Versión |
| ------------------------ | --------------------- | ------- |
| **Framework**            | Spring Boot           | 4.0.5   |
| **Lenguaje**             | Java                  | 17+     |
| **Build Tool**           | Maven                 | 3.8+    |
| **Base de Datos**        | PostgreSQL            | 15+     |
| **Seguridad**            | Spring Security + JWT | -       |
| **Validación**           | Jakarta Validation    | -       |
| **Control de Versiones** | Git / GitHub          | -       |

---

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java Development Kit (JDK)** - v17 o superior
- **Maven** - v3.8 o superior
- **PostgreSQL** - v15 o superior (o proyecto Supabase)
- **Git** - para control de versiones
- **IDE** (opcional) - IntelliJ IDEA, VS Code o Eclipse

Verificar instalación:

```bash
java -version
mvn -version
psql --version
```

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/nubix/nubix_market_backend.git
cd nubix_market_backend
```

### 2. Configurar base de datos PostgreSQL

Crea un proyecto en [Supabase](https://supabase.com) o una instancia local de PostgreSQL y obtén la URL de conexión (modo pooler, puerto 6543, recomendado en producción).

### 3. Configurar variables de entorno

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Editar `application.properties`:

```properties
# Database (PostgreSQL / Supabase)
spring.datasource.url=jdbc:postgresql://localhost:5432/nubix_market
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=tu_clave_secreta_super_segura_aqui
jwt.expiration-ms=86400000

# CORS
cors.allowed-origins=http://localhost:5173

# Server
server.port=8080
```

### 4. Compilar el proyecto

```bash
mvn clean install
```

---

## 📁 Estructura del Proyecto

```
src/main/java/com/nubix/market/
├── MarketBackendApplication.java      # Punto de entrada
├── config/
│   ├── JwtAuthFilter.java              # Filtro de autenticación JWT
│   ├── JwtUtils.java                   # Utilidades JWT
│   └── SecurityConfig.java             # Configuración de seguridad
├── controllers/
│   └── AuthController.java             # Endpoints de autenticación
├── dto/
│   ├── AuthResponse.java               # Respuesta de autenticación
│   ├── LoginRequest.java               # Solicitud de login
│   └── RegisterRequest.java            # Solicitud de registro
├── entities/
│   └── Usuario.java                    # Entidad Usuario
├── exceptions/                         # Manejo de excepciones
├── repositories/
│   └── UsuarioRepository.java          # Acceso a datos Usuario
└── services/
    └── AuthService.java                # Lógica de negocio

src/main/resources/
├── application.properties              # Configuración principal
├── application.properties.example      # Plantilla de configuración
```

---

## ⚙️ Configuración

### application.properties

```properties
# Información de la aplicación
spring.application.name=market-backend

# Base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/nubix_market
spring.datasource.username=postgres
spring.datasource.password=password123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# JWT Secret (cambiar por una clave segura en producción)
jwt.secret=clave_secreta_super_segura
jwt.expiration-ms=86400000
cors.allowed-origins=http://localhost:5173

# Server
server.port=8080
```

---

## 🚀 Ejecución

### Ejecutar con Maven

```bash
mvn spring-boot:run
```

### Ejecutar desde IDE

1. Abrir la clase `MarketBackendApplication.java`
2. Click derecho → Run 'MarketBackendApplication'

### La aplicación estará disponible en:

```
http://localhost:8080
```

---

## 📡 Endpoints API

### Autenticación

| Método | Endpoint             | Descripción             |
| ------ | -------------------- | ----------------------- |
| `POST` | `/api/auth/register` | Registrar nuevo usuario |
| `POST` | `/api/auth/login`    | Iniciar sesión          |

#### Registro

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "email": "usuario@example.com",
    "password": "password123"
  }'
```

#### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "password123"
  }'
```

---

## 👥 Autores

- **Cardeña Cusi Adilson Aldair**
- **Tinoco Guerrero Wilmer Leopoldo**
- **Abad Puglianini Danna Sherily**
- **Cáceres Aranda Diego Antonio**
- **Ramírez Taboada Christopher**
- **Rodriguez Valencia Pedro**

---

## 📄 Licencia

Este proyecto está bajo licencia [MIT](LICENSE).

---

## 📞 Soporte

Para reportar problemas o sugerencias, abre un issue en el repositorio.
