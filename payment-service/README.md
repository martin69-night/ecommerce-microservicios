# Payment Service

Microservicio encargado de gestionar pagos del sistema de e-commerce.

## Descripción

payment-service permite crear pagos, listar pagos, consultar pagos por usuario, buscar pagos por pedido y cambiar el estado de un pago.

Este servicio implementa arquitectura por capas:

- Controller
- Service
- Repository
- Model
- Exception Handler

La entidad principal del microservicio es Pago.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Docker / Docker Compose

## Puerto del servicio

| Servicio | Puerto |
|---|---:|
| payment-service | 8082 |

## Base de datos

| Motor | Base de datos | Usuario |
|---|---|---|
| MySQL | payment_db | desarrollador |

## Entidad principal: Pago

| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| pedidoId | Long | ID del pedido asociado |
| usuarioId | Long | ID del usuario |
| monto | BigDecimal | Monto del pago |
| metodoPago | String | Método de pago |
| estado | String | Estado del pago |
| codigoTransaccion | String | Código generado al aprobar pago |
| fechaCreacion | LocalDateTime | Fecha de creación |
| activo | Boolean | Estado lógico del registro |

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | / | Estado del microservicio |
| GET | /api/pagos | Lista pagos activos |
| GET | /api/pagos/usuario/{usuarioId} | Lista pagos por usuario |
| GET | /api/pagos/estado/{estado} | Lista pagos por estado |
| GET | /api/pagos/{id} | Busca pago por ID |
| GET | /api/pagos/pedido/{pedidoId} | Busca pago por pedido |
| POST | /api/pagos | Crea pago |
| PATCH | /api/pagos/{id}/aprobar | Aprueba pago |
| PATCH | /api/pagos/{id}/rechazar | Rechaza pago |
| PATCH | /api/pagos/{id}/anular | Anula pago |

## Reglas de negocio

- No se puede crear pago sin pedidoId.
- No se puede crear pago sin usuarioId.
- El monto debe ser mayor a cero.
- El método de pago es obligatorio.
- Todo pago creado inicia con estado PENDIENTE.
- Solo se pueden aprobar pagos pendientes.
- Solo se pueden rechazar pagos pendientes.
- Al aprobar un pago se genera un código de transacción.
- No se puede anular un pago aprobado.

## Pruebas realizadas

- Servicio iniciado correctamente en puerto 8082.
- Endpoint / responde estado OK.
- Endpoint POST /api/pagos crea pagos correctamente.
- Endpoint GET /api/pagos lista pagos desde MySQL.
- Endpoint PATCH /api/pagos/1/aprobar cambia estado a APROBADO.
- Endpoint GET /api/pagos/pedido/1 busca pago por pedido.
- Endpoint GET /api/pagos/usuario/1 lista pagos por usuario.
- Compilación validada con mvn clean compile -DskipTests.

## Requerimientos funcionales

- RF01: Crear pagos.
- RF02: Listar pagos activos.
- RF03: Buscar pago por ID.
- RF04: Buscar pago por pedido.
- RF05: Listar pagos por usuario.
- RF06: Aprobar pago.
- RF07: Rechazar pago.
- RF08: Anular pago.

## Requerimientos no funcionales

- RNF01: El microservicio debe ejecutarse de forma independiente.
- RNF02: El microservicio debe conectarse a MySQL.
- RNF03: El microservicio debe exponer una API REST.
- RNF04: El código debe separar Controller, Service y Repository.
- RNF05: El servicio debe ser ejecutable en contenedor Docker.
