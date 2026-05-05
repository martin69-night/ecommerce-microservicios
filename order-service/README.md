# Order Service

Microservicio encargado de gestionar pedidos del sistema de e-commerce.

## Descripción

order-service permite crear pedidos, listar pedidos, consultar pedidos por usuario, filtrar por estado y cambiar el estado de un pedido.

Este servicio implementa arquitectura por capas:

- Controller
- Service
- Repository
- Model
- Exception Handler

La entidad principal del microservicio es Pedido.

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
| order-service | 8081 |

## Base de datos

| Motor | Base de datos | Usuario |
|---|---|---|
| MySQL | order_db | desarrollador |

## Entidad principal: Pedido

| Campo | Tipo | Descripción |
|---|---|---|
| id | Long | Identificador único |
| usuarioId | Long | ID del usuario |
| productoId | Long | ID del producto |
| sku | String | SKU del producto |
| nombreProducto | String | Nombre del producto |
| precioUnitario | BigDecimal | Precio unitario |
| cantidad | Integer | Cantidad comprada |
| total | BigDecimal | Total del pedido |
| estado | String | Estado del pedido |
| fechaCreacion | LocalDateTime | Fecha de creación |
| activo | Boolean | Estado lógico del registro |

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | / | Estado del microservicio |
| GET | /api/pedidos | Lista pedidos activos |
| GET | /api/pedidos/usuario/{usuarioId} | Lista pedidos por usuario |
| GET | /api/pedidos/estado/{estado} | Lista pedidos por estado |
| GET | /api/pedidos/{id} | Busca pedido por ID |
| POST | /api/pedidos | Crea pedido |
| PATCH | /api/pedidos/{id}/estado?estado=PAGADO | Cambia estado del pedido |
| PATCH | /api/pedidos/{id}/cancelar | Cancela pedido |

## Reglas de negocio

- No se puede crear pedido sin usuarioId.
- No se puede crear pedido sin productoId.
- No se puede crear pedido sin SKU.
- No se puede crear pedido sin nombre de producto.
- El precio unitario debe ser mayor a cero.
- La cantidad debe ser mayor a cero.
- El total se calcula multiplicando precioUnitario por cantidad.
- Un pedido creado inicia con estado CREADO.
- No se puede cancelar un pedido PAGADO.

## Pruebas realizadas

- Servicio iniciado correctamente en puerto 8081.
- Endpoint / responde estado OK.
- Endpoint POST /api/pedidos crea pedidos correctamente.
- Endpoint GET /api/pedidos lista pedidos desde MySQL.
- Endpoint PATCH /api/pedidos/1/estado cambia estado a PAGADO.
- Endpoint GET /api/pedidos/usuario/1 lista pedidos del usuario.
- Compilación validada con mvn clean compile -DskipTests.

## Requerimientos funcionales

- RF01: Crear pedidos.
- RF02: Listar pedidos activos.
- RF03: Buscar pedido por ID.
- RF04: Listar pedidos por usuario.
- RF05: Listar pedidos por estado.
- RF06: Cambiar estado del pedido.
- RF07: Cancelar pedido.

## Requerimientos no funcionales

- RNF01: El microservicio debe ejecutarse de forma independiente.
- RNF02: El microservicio debe conectarse a MySQL.
- RNF03: El microservicio debe exponer una API REST.
- RNF04: El código debe separar Controller, Service y Repository.
- RNF05: El servicio debe ser ejecutable en contenedor Docker.
