# 📦 Microservicio de Reservas

El **Microservicio de Reservas** expone endpoints para consultar información de reservas a través de una API REST.

Actualmente el servicio implementa **únicamente controladores de tipo `GET`**, destinados a la consulta de datos.  
Todas las respuestas del servicio se entregan en **formato JSON**.

---

## 🚀 Funcionalidades disponibles

El microservicio ofrece **tres endpoints principales**:

### 1️⃣ Listar todas las reservas

Permite obtener la lista completa de reservas registradas en el sistema.

**Endpoint**

```http
GET /api/reservations
```

**URL de prueba**

```
http://localhost:8080/api/reservations
```

**Respuesta**

Retorna un **JSON con los 8 registros completos de reservas** almacenadas.

---

### 2️⃣ Consultar disponibilidad

Permite obtener únicamente las reservas que se encuentran activas.

**Endpoint**

```http
GET /api/reservations/availability
```

**URL de prueba**

```
http://localhost:8080/api/reservations/availability
```

**Respuesta**

Retorna un **JSON filtrado con las reservas cuyo estado es `"ACTIVA"`**.

---

### 3️⃣ Buscar reserva por ID

Permite consultar la información de una reserva específica utilizando su identificador.

**Endpoint**

```http
GET /api/reservations/{id}
```

**URL de prueba**

```
http://localhost:8080/api/reservations/1
```

**Respuesta**

Retorna un **JSON con los datos detallados de la reserva correspondiente al ID solicitado**.

---

## 📄 Formato de respuesta

Todos los endpoints devuelven respuestas en **formato JSON**.
