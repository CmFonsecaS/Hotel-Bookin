# 🏨 Hotel Booking Service (Microservicio de Reservas)

Microservicio desarrollado con **Spring Boot 3.3.0** y **Oracle Database Cloud**, diseñado para gestionar reservas de hotel.

## 🛠️ Tecnologías y Herramientas

- **Framework:** Spring Boot 3.3.0 (Java 17)
- **Persistencia:** Spring Data JPA con **Oracle JDBC 21.9.0.0**
- **Base de Datos:** Oracle Cloud Autonomous Database (Conexión vía Wallet)
- **Seguridad:** Spring Security (Basic Authentication & CORS)
- **Validación:** Jakarta Bean Validation (Hibernate Validator)
- **Utilidades:** Lombok y SLF4J (Logging)

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura de capas, separando las responsabilidades de forma clara:

1.  **DTO (Data Transfer Objects):** Capa encargada de la comunicación externa y validación de entrada. Evita la exposición directa de las entidades de base de datos.
2.  **Controller:** Capa de presentación que expone los endpoints RESTful.
3.  **Service:** Capa de lógica de negocio y mapeo entre DTOs y Entidades.
4.  **Repository:** Capa de persistencia que interactúa con las tablas de Oracle.
5.  **Exception Handling:** Manejador global de excepciones que estandariza las respuestas de error en formato JSON.

---

## 🔐 Seguridad y Autenticación

Todos los endpoints están protegidos mediante **Basic Auth**.

- **Usuario:** `hotelbooking`
- **Contraseña:** `SumativasFullStack2026`

---

## 📡 Endpoints de la API

| Método | Endpoint | Descripción | Requiere Body |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/reservations` | Listar todas las reservas | No |
| **GET** | `/api/reservations/availability` | Consultar reservas activas | No |
| **GET** | `/api/reservations/{id}` | Buscar reserva por ID | No |
| **POST** | `/api/reservations` | Crear nueva reserva (Usa DTO) | Sí |
| **PUT** | `/api/reservations/{id}` | Actualizar reserva existente | Sí |
| **DELETE** | `/api/reservations/{id}` | Eliminar una reserva | No |

### Ejemplo de JSON para POST/PUT:
```json
{
    "hotelId": 1,
    "guestName": "Juan Perez",
    "roomType": "Suite Premium",
    "checkInDate": "2026-12-31",
    "checkOutDate": "2027-01-05",
    "status": "ACTIVE",
    "paymentMethod": "CARD"
}
```

---

## ⚙️ Configuración de Base de Datos

El microservicio utiliza una **Oracle Wallet** para la conexión segura.
- **Ubicación:** Configurada en `application.properties` mediante la propiedad `TNS_ADMIN`.
- **Estructura:** Las tablas `HOTELS` y `RESERVATIONS` deben ser creadas previamente con el script SQL proporcionado.

## Script SQL de creación y carga inicial

```sql
-- ==========================================================
-- SCRIPT DE CREACIÓN Y CARGA INICIAL - HOTEL BOOKING SERVICE
-- ==========================================================

-- 1. Limpieza de tablas existentes (evitar conflictos)
DROP TABLE RESERVATIONS CASCADE CONSTRAINTS;
DROP TABLE HOTELS CASCADE CONSTRAINTS;

-- 2. Creación de la tabla de Hoteles
CREATE TABLE HOTELS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME VARCHAR2(100) NOT NULL,
    LOCATION VARCHAR2(100) NOT NULL,
    CATEGORY VARCHAR2(50)
);

-- 3. Creación de la tabla de Reservas
-- Relacionada con la tabla de Hoteles mediante HOTEL_ID
CREATE TABLE RESERVATIONS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    HOTEL_ID NUMBER,
    GUEST_NAME VARCHAR2(100) NOT NULL,
    ROOM_TYPE VARCHAR2(50) NOT NULL,
    CHECK_IN_DATE DATE NOT NULL,
    CHECK_OUT_DATE DATE NOT NULL,
    STATUS VARCHAR2(20) DEFAULT 'ACTIVE',
    PAYMENT_METHOD VARCHAR2(20),
    CONSTRAINT FK_HOTEL FOREIGN KEY (HOTEL_ID) REFERENCES HOTELS(ID)
);

-- 4. Inserción de 3 registros en HOTELS
INSERT INTO HOTELS (NAME, LOCATION, CATEGORY) VALUES ('Hotel Plaza', 'Santiago', '5 Estrellas');
INSERT INTO HOTELS (NAME, LOCATION, CATEGORY) VALUES ('Hotel Central', 'Viña del Mar', '4 Estrellas');
INSERT INTO HOTELS (NAME, LOCATION, CATEGORY) VALUES ('Hotel Estelar', 'Concepción', '3 Estrellas');

-- 5. Inserción de 3 registros en RESERVATIONS
INSERT INTO RESERVATIONS (HOTEL_ID, GUEST_NAME, ROOM_TYPE, CHECK_IN_DATE, CHECK_OUT_DATE, STATUS, PAYMENT_METHOD) 
VALUES (1, 'Juan Perez', 'Suite', TO_DATE('2026-05-01','YYYY-MM-DD'), TO_DATE('2026-05-05','YYYY-MM-DD'), 'ACTIVE', 'CARD');

INSERT INTO RESERVATIONS (HOTEL_ID, GUEST_NAME, ROOM_TYPE, CHECK_IN_DATE, CHECK_OUT_DATE, STATUS, PAYMENT_METHOD) 
VALUES (2, 'Maria Gomez', 'Double', TO_DATE('2026-04-20','YYYY-MM-DD'), TO_DATE('2026-04-25','YYYY-MM-DD'), 'ACTIVE', 'CASH');

INSERT INTO RESERVATIONS (HOTEL_ID, GUEST_NAME, ROOM_TYPE, CHECK_IN_DATE, CHECK_OUT_DATE, STATUS, PAYMENT_METHOD) 
VALUES (3, 'Carlos Diaz', 'Single', TO_DATE('2026-06-10','YYYY-MM-DD'), TO_DATE('2026-06-12','YYYY-MM-DD'), 'ACTIVE', 'TRANSFER');

COMMIT;
```

---

## 🚀 Pruebas Unitarias (Testing)

El proyecto incluye pruebas unitarias automatizadas desarrolladas con **JUnit 5** y **Mockito** para garantizar la fiabilidad de la lógica de negocio aislando la base de datos.
- **Ubicación:** `src/test/java/.../service/ReservationServiceTest.java`
- **Ejecución:**
  ```bash
  ./mvnw test
  ```

### Pruebas de Negocio:
- `testGetAllReservations()`: Prueba que lista las reservas.
- `testCreateReservation()`: Prueba que crea una reserva usando DTO.
- `testGetReservationById()`: Prueba que busca una reserva específica por su ID.
- `testDeleteReservation()`: Prueba que elimina una reserva.

---

## 🔗 Nivel de Madurez REST (HATEOAS)

La API cuenta con documentación auto-generada e incrustada en las respuestas JSON gracias a **Spring HATEOAS**. 
Las respuestas retornan objetos que incluyen un bloque `_links` que provee URLs dinámicas (como `self` o `all-reservations`), facilitando la navegación y la interacción desde aplicaciones cliente.

---

## 🐳 Despliegue en Cloud (Dockerización)

El microservicio está preparado para ser desplegado en la nube (como Docker Lab) mediante un entorno contenerizado. Utiliza un proceso de construcción *multi-stage* que compila el código y empaca la Oracle Wallet automáticamente.

### Ejecución con Docker (Recomendado)

1.  Asegúrate de tener la carpeta de la Wallet (`Wallet_MIDBDUOC`) en la raíz del proyecto.
2.  Construye la imagen Docker:
    ```bash
    docker build -t hotel-booking-service .
    ```
3.  Levanta el contenedor (o usa `docker-compose up -d`):
    ```bash
    docker run -p 8081:8080 hotel-booking-service
    ```

### Ejecución Local

1.  Compilar el proyecto:
    ```bash
    ./mvnw clean compile
    ```
2.  Iniciar la aplicación (la Wallet local debe estar configurada en `application.properties`):
    ```bash
    ./mvnw spring-boot:run
    ```

---
*Desarrollado para el proyecto final de Integración de Sistemas y Bases de Datos.*
