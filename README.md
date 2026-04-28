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
- ✅ Conexión a base de datos MySQL
- ✅ Filtros de seguridad personalizados
- ✅ Manejo de excepciones centralizado

---

## ✨ Características

- 🔐 **Autenticación JWT** - Tokens seguros y validación de sesiones
- 👤 **Gestión de Usuarios** - Registro, login y perfil de usuario
- 🔒 **Seguridad** - Encriptación de contraseñas y filtros de autorización
- 🗄️ **Base de Datos** - Integración con MySQL
- ⚙️ **Configuración centralizada** - Manejo de propiedades por ambiente
- 📝 **Logging** - Trazabilidad de operaciones

---

## 🛠️ Tecnologías

| Categoría                | Tecnología            | Versión |
| ------------------------ | --------------------- | ------- |
| **Framework**            | Spring Boot           | 3.x+    |
| **Lenguaje**             | Java                  | 17+     |
| **Build Tool**           | Maven                 | 3.8+    |
| **Base de Datos**        | MySQL                 | 8.0+    |
| **Seguridad**            | Spring Security + JWT | -       |
| **Validación**           | Jakarta Validation    | -       |
| **Control de Versiones** | Git / GitHub          | -       |

---

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java Development Kit (JDK)** - v17 o superior
- **Maven** - v3.8 o superior
- **MySQL Server** - v8.0 o superior
- **Git** - para control de versiones
- **IDE** (opcional) - IntelliJ IDEA, VS Code o Eclipse

Verificar instalación:

```bash
java -version
mvn -version
mysql --version
```

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/nubix/nubix_market_backend.git
cd nubix_market_backend
```

### 2. Crear base de datos MySQL

```sql
CREATE DATABASE nubix_market;
CREATE USER 'nubix_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON nubix_market.* TO 'nubix_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar variables de entorno

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Editar `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/nubix_market
spring.datasource.username=nubix_user
spring.datasource.password=password123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT
jwt.secret=tu_clave_secreta_super_segura_aqui
jwt.expiration=86400000

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

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/nubix_market
spring.datasource.username=nubix_user
spring.datasource.password=password123

# JPA
spring.jpa.hibernate.ddl-auto=update

# JWT Secret (cambiar por una clave segura en producción)
jwt.secret=clave_secreta_super_segura
jwt.expiration=86400000

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
