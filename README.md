# Reto Plazoleta - Plataforma de Gestión de Pedidos

Bienvenida a la documentación central del **Reto Plazoleta**. Este documento sirve como bitácora y guía de aprendizaje paso a paso para consolidar cada concepto técnico, patrón de diseño y avance desarrollado en cada Historia de Usuario (HU).

---

## 1. Arquitectura General del Sistema

El backend está concebido bajo una arquitectura basada en **Microservicios independientes**, cada uno con su propio almacenamiento de datos y responsabilidades bien delimitadas:

```
                               ┌────────────────────────────────────────┐
                               │               NAVEGADOR                │
                               │        (Administrador, Propietario,     │
                               │             Empleado, Cliente)         │
                               └───────────────────┬────────────────────┘
                                                   │
                         ┌─────────────────────────┴────────────────────────┐
                         │                                                  │
                         ▼ (HTTP / JSON)                                    ▼ (HTTP / JSON)
            ┌───────────────────────────┐                      ┌───────────────────────────┐
            │   Microservicio Plazoleta │                      │  Microservicio Usuarios   │
            │   (Spring Boot, Swagger)  │                      │   (Spring Boot, Swagger,  │
            │                           │                      │      Spring Security)     │
            └─────────────┬─────────────┘                      └─────────────┬─────────────┘
                          │                                                  │
              ┌───────────┼───────────┐                                      ▼
              ▼           ▼           ▼                                ┌───────────┐
         ┌─────────┐ ┌─────────┐ ┌─────────┐                           │    bd     │
         │   bd    │ │ Trazab. │ │ Mensaj. │                           │ USUARIOS  │
         │PLAZOLETA│ │(MongoDB)│ │(Twilio) │                           │  (MySQL)  │
         │ (MySQL) │ └─────────┘ └─────────┘                           └───────────┘
         └─────────┘
```

### Microservicios del Ecosistema:
1. **Microservicio Usuarios** *(En desarrollo actual)*: Administra usuarios, credenciales, encriptación y roles (`ADMINISTRADOR`, `PROPIETARIO`, `EMPLEADO`, `CLIENTE`). Base de datos relacional MySQL.
2. **Microservicio Plazoleta**: Administra restaurantes, platos, cartas y el flujo de los pedidos. Base de datos MySQL.
3. **Microservicio Mensajería**: Administra el envío de notificaciones por SMS/WhatsApp vía Twilio con códigos de seguridad para entrega de pedidos.
4. **Microservicio Trazabilidad**: Registra y consulta la auditoría y tiempos de los pedidos en cada estado. Base de datos NoSQL MongoDB.

---

## 2. Arquitectura Interna: Arquitectura Hexagonal (Puertos y Adaptadores)

Cada microservicio se estructura siguiendo la **Arquitectura Hexagonal**, cuyo principio central es la **independencia del negocio respecto a frameworks y tecnologías externas**:

```
                              ┌────────────────────────────────────────────────────────┐
                              │               INFRASTRUCTURE (Adaptadores)             │
                              │                                                        │
                              │   [REST Controller]               [JPA Adapter / MySQL]│
                              │   [OpenAPI/Swagger]               [BCrypt Adapter]     │
                              │          │                                  ▲          │
                              │          ▼                                  │          │
                              │   ┌──────────────────────────────────────────────┐     │
                              │   │                 APPLICATION                  │     │
                              │   │                                              │     │
                              │   │   [DTOs]  ↔  [Mappers]  ↔  [Handler]         │     │
                              │   │                              │               │     │
                              │   │                              ▼               │     │
                              │   │   ┌──────────────────────────────────────┐   │     │
                              │   │   │               DOMAIN                 │   │     │
                              │   │   │                                      │   │     │
                              │   │   │  [API Port]  →  [Use Case]  →  [SPI] │   │     │
                              │   │   │  (Entrada)     (Reglas)      (Salida)│   │     │
                              │   │   │  [Models]       [Exceptions]         │   │     │
                              │   │   └──────────────────────────────────────┘   │     │
                              │   └──────────────────────────────────────────────┘     │
                              └────────────────────────────────────────────────────────┘
```

### Conceptos Clave Aprendidos:
* **Dominio (`domain`)**: Es el corazón del sistema. Código Java puro.
  * **Modelos**: Clases POJO de negocio sin anotaciones de persistencia (`UserModel`, `RoleModel`).
  * **Puertos de Entrada (`api`)**: Interfaces que definen qué operaciones ofrece el caso de uso (`IUserServicePort`).
  * **Puertos de Salida (`spi`)**: Interfaces que definen lo que el dominio necesita del exterior (`IUserPersistencePort`, `IRolePersistencePort`, `IPasswordEncoderPort`).
  * **Casos de Uso (`usecase`)**: Clases que contienen las reglas y validaciones de negocio (`UserUseCase`).
  * **Excepciones de Dominio**: Errores propios de negocio (`UsuarioMenorDeEdadException`, `CorreoYaExisteException`, etc.).
* **Aplicación (`application`)**: Puente orquestador.
  * **DTOs (`dto`)**: Objetos que reciben y envían información al cliente (`UserRequestDto`).
  * **Mappers (`mapper`)**: Traductores MapStruct entre DTOs y Modelos (`IUserRequestMapper`).
  * **Handlers (`handler`)**: Clases `@Service` que orquestan las transacciones y llaman al caso de uso (`UserHandler`).
* **Infraestructura (`infrastructure`)**: Adaptadores hacia librerías y tecnologías.
  * **`input.rest`**: Controladores Web `@RestController` que exponen los endpoints HTTP.
  * **`out.jpa`**: Entidades de base de datos (`UserEntity`, `RoleEntity`), repositorios Spring Data (`IUserRepository`, `IRoleRepository`) y adaptadores (`UserJpaAdapter`, `RoleJpaAdapter`).
  * **`out.security`**: Adaptador de encriptación (`BcryptPasswordEncoderAdapter`) con `BCryptPasswordEncoder`.
  * **`configuration`**: Registro manual de `@Bean` en `BeanConfiguration` para inyectar dependencias al caso de uso sin tocar el dominio.
  * **`exceptionhandler`**: Manejador global `@ControllerAdvice` (`ControllerAdvisor`) para transformar excepciones de dominio en respuestas JSON claras con códigos HTTP estándar (400, 404, 409).

---

## 3. Estado del Backlog de Historias de Usuario

| # HU | Rol | Historia de Usuario | Microservicio | Rama Git | Estado |
| :---: | :---: | :--- | :---: | :---: | :---: |
| **1.0** | Administrador | **Crear Propietario** | `powerup-usuarios` | `feature/HU-01-crear-propietario` | **Completada (Lista para pruebas)** |
| 2.0 | Administrador | Crear Restaurante | `powerup-plazoleta` | - | Pendiente |
| 3.0 | Propietario | Crear Plato | `powerup-plazoleta` | - | Pendiente |
| 4.0 | Propietario | Modificar Plato | `powerup-plazoleta` | - | Pendiente |
| 5.0 | Todos | Autenticación y Autorización (JWT) | `powerup-usuarios` / Todos | - | Pendiente |
| 6.0 | Propietario | Crear cuenta Empleado | `powerup-usuarios` | - | Pendiente |
| 7.0 | Propietario | Habilitar / Deshabilitar Plato | `powerup-plazoleta` | - | Pendiente |
| 8.0 | Cliente | Crear cuenta Cliente | `powerup-usuarios` | - | Pendiente |
| 9.0 | Cliente | Listar Restaurantes | `powerup-plazoleta` | - | Pendiente |
| 10.0 | Cliente | Listar Platos de un Restaurante | `powerup-plazoleta` | - | Pendiente |
| 11.0 | Cliente | Realizar Pedido | `powerup-plazoleta` | - | Pendiente |
| 12.0 | Empleado | Listar pedidos filtrando por estado | `powerup-plazoleta` | - | Pendiente |
| 13.0 | Empleado | Asignarse a pedido ("En preparación") | `powerup-plazoleta` | - | Pendiente |
| 14.0 | Empleado | Notificar pedido listo (SMS con PIN) | `powerup-mensajeria` | - | Pendiente |
| 15.0 | Empleado | Entregar pedido con PIN | `powerup-plazoleta` | - | Pendiente |
| 16.0 | Cliente | Cancelar pedido en estado Pendiente | `powerup-plazoleta` | - | Pendiente |
| 17.0 | Cliente | Consultar trazabilidad de pedido | `powerup-trazabilidad`| - | Pendiente |
| 18.0 | Propietario | Consultar eficiencia de pedidos | `powerup-trazabilidad`| - | Pendiente |

---

## 4. Detalle de Implementación: HU-01 (Crear Propietario)

### Objetivo
Como Administrador de la plataforma, necesito poder crear en el sistema la cuenta para un propietario para posteriormente asignarle un restaurante.

### Criterios de Aceptación Implementados:
1. **Campos obligatorios**: Nombre, Apellido, DocumentoDeIdentidad, Celular, FechaNacimiento, Correo y Clave.
2. **Validaciones de formato**:
   * Correo con estructura válida de email.
   * Teléfono con un máximo de 13 caracteres, permitiendo el prefijo numérico internacional con el símbolo `+` (Ejemplo: `+573005698325`).
   * Documento de identidad **únicamente numérico**.
3. **Mayoría de edad**: Se calcula la edad respecto a la fecha actual y se exige ser mayor o igual a 18 años.
4. **Unicidad**: El correo y el documento de identidad no deben existir previamente en la base de datos.
5. **Rol automático**: El usuario queda asignado con el rol de **PROPIETARIO** (ID `2`).
6. **Seguridad**: La contraseña se almacena de forma segura encriptada con algoritmo **BCrypt**.

---

### Mapa de Archivos Desarrollados en `powerup-usuarios`:

```
powerup-usuarios/
├── build.gradle (Agregada dependencia spring-security-crypto)
└── src/
    ├── main/
    │   ├── java/com/pragma/powerup/
    │   │   ├── domain/                                          # CAPA DE DOMINIO
    │   │   │   ├── model/
    │   │   │   │   ├── UserModel.java                           # Modelo puro de Usuario
    │   │   │   │   └── RoleModel.java                           # Modelo puro de Rol
    │   │   │   ├── api/
    │   │   │   │   └── IUserServicePort.java                    # Puerto de Entrada
    │   │   │   ├── spi/
    │   │   │   │   ├── IUserPersistencePort.java                # Puerto de Salida Persistencia Usuario
    │   │   │   │   ├── IRolePersistencePort.java                # Puerto de Salida Persistencia Rol
    │   │   │   │   └── IPasswordEncoderPort.java                # Puerto de Salida Encriptador
    │   │   │   ├── exception/                                   # Excepciones de Negocio
    │   │   │   │   ├── UsuarioMenorDeEdadException.java
    │   │   │   │   ├── DocumentoInvalidoException.java
    │   │   │   │   ├── FormatoCelularInvalidoException.java
    │   │   │   │   ├── FormatoCorreoInvalidoException.java
    │   │   │   │   ├── CorreoYaExisteException.java
    │   │   │   │   ├── DocumentoYaExisteException.java
    │   │   │   │   └── RolNoEncontradoException.java
    │   │   │   └── usecase/
    │   │   │       └── UserUseCase.java                         # Reglas de negocio de HU-01
    │   │   │
    │   │   ├── application/                                     # CAPA DE APLICACIÓN
    │   │   │   ├── dto/request/
    │   │   │   │   └── UserRequestDto.java                      # DTO de entrada con Bean Validation
    │   │   │   ├── mapper/
    │   │   │   │   └── IUserRequestMapper.java                  # MapStruct: DTO ↔ Model
    │   │   │   └── handler/
    │   │   │       ├── IUserHandler.java
    │   │   │       └── impl/UserHandler.java                    # Orquestador @Service @Transactional
    │   │   │
    │   │   └── infrastructure/                                  # CAPA DE INFRAESTRUCTURA
    │   │       ├── configuration/
    │   │       │   └── BeanConfiguration.java                   # Inyección de Beans del Hexágono
    │   │       ├── input/rest/
    │   │       │   └── UserRestController.java                  # Endpoint POST /api/v1/user/propietario
    │   │       ├── exceptionhandler/
    │   │       │   ├── ControllerAdvisor.java                   # @ControllerAdvice manejo HTTP 400/404/409
    │   │       │   └── ExceptionResponse.java                   # Mensajes de error en JSON
    │   │       ├── out/jpa/                                     # Persistencia MySQL
    │   │       │   ├── entity/
    │   │       │   │   ├── RoleEntity.java                      # @Entity tabla 'roles'
    │   │       │   │   └── UserEntity.java                      # @Entity tabla 'usuarios'
    │   │       │   ├── repository/
    │   │       │   │   ├── IRoleRepository.java                 # JpaRepository de roles
    │   │       │   │   └── IUserRepository.java                 # JpaRepository de usuarios
    │   │       │   ├── mapper/
    │   │       │   │   ├── IRoleEntityMapper.java               # MapStruct: RoleEntity ↔ RoleModel
    │   │       │   │   └── IUserEntityMapper.java               # MapStruct: UserEntity ↔ UserModel
    │   │       │   └── adapter/
    │   │       │       ├── RoleJpaAdapter.java                  # Implementa IRolePersistencePort
    │   │       │       └── UserJpaAdapter.java                  # Implementa IUserPersistencePort
    │   │       └── out/security/adapter/
    │   │           └── BcryptPasswordEncoderAdapter.java        # Implementa IPasswordEncoderPort (BCrypt)
    │   │
    │   └── resources/
    │       ├── application.yml                                  # Configuración MySQL y JPA
    │       └── data.sql                                         # Precarga de los 4 roles del sistema
    │
    └── test/java/com/pragma/powerup/                            # PRUEBAS UNITARIAS (Mockito)
        ├── domain/usecase/
        │   └── UserUseCaseTest.java                             # 8 pruebas unitarias del Dominio
        └── application/handler/impl/
            └── UserHandlerTest.java                             # 1 prueba unitaria de Aplicación
```

---

## 5. Pruebas Unitarias

La HU-01 cuenta con **9 pruebas unitarias automatizadas** usando **JUnit 5** y **Mockito**, garantizando el 100% de los escenarios esperados:

1. `guardarPropietario_exitoso`: Verifica que los datos válidos se encripten en BCrypt, se asigne el rol de propietario y se invoque el guardado en base de datos.
2. `guardarPropietario_usuarioMenorDeEdad_lanzaExcepcion`: Falla si el usuario tiene menos de 18 años.
3. `guardarPropietario_documentoInvalidoConLetras_lanzaExcepcion`: Falla si el documento contiene letras.
4. `guardarPropietario_celularInvalidoMayorA13Caracteres_lanzaExcepcion`: Falla si el celular supera 13 caracteres.
5. `guardarPropietario_correoInvalido_lanzaExcepcion`: Falla si el correo no tiene formato estándar `@`.
6. `guardarPropietario_correoYaExiste_lanzaExcepcion`: Falla si el correo ya está registrado en la base de datos.
7. `guardarPropietario_documentoYaExiste_lanzaExcepcion`: Falla si el documento de identidad ya está registrado.
8. `guardarPropietario_rolNoEncontrado_lanzaExcepcion`: Falla si el rol ID 2 no existe en la base de datos.
9. `guardarPropietario_debeMapearYLlamarAlPuertoDelDominio`: Verifica la correcta orquestación en la capa de aplicación (`UserHandler`).

### Ejecución de Pruebas:
Para correr las pruebas unitarias en consola:
```powershell
cd powerup-usuarios
.\gradlew test --tests com.pragma.powerup.domain.usecase.UserUseCaseTest --tests com.pragma.powerup.application.handler.impl.UserHandlerTest
```

---

## 6. Guía de Ejecución y Pruebas en Vivo (Manual)

### Requisitos Previos:
1. Tener corriendo MySQL en el puerto `3306`.
2. Crear la base de datos inicial:
   ```sql
   CREATE DATABASE IF NOT EXISTS powerup;
   ```
3. Credenciales en `application.yml`:
   * **URL**: `jdbc:mysql://localhost:3306/powerup`
   * **Usuario**: `root`
   * **Contraseña**: `1234` *(o la correspondiente en tu entorno local)*

### Iniciar el Microservicio:
```powershell
cd powerup-usuarios
.\gradlew bootRun
```

### Documentación y Pruebas con Swagger:
Abre tu navegador en:
👉 **[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)**

### Ejemplo de Payload para Crear un Propietario (`POST /api/v1/user/propietario`):
```json
{
  "nombre": "Valentina",
  "apellido": "Pinto",
  "documentoDeIdentidad": "1098765432",
  "celular": "+573101234567",
  "fechaNacimiento": "2000-01-15",
  "correo": "valentina.propietario@plazoleta.com",
  "clave": "MiClaveSegura2026*"
}
```

* **Respuesta Exitosa**: `201 Created`
* **En Base de Datos**: Podrás consultar la tabla `usuarios` y comprobarás que el campo `clave` está almacenado como un hash BCrypt (ej. `$2a$10$...`) y el campo `id_rol` apunta al rol `2` (`PROPIETARIO`).
