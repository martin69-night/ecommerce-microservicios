# E-Commerce Microservicios

## DSY1103 — Desarrollo Fullstack I

Arquitectura de microservicios para una plataforma de e-commerce desarrollada con Spring Boot 4.x, Spring Cloud 2025.1.1 y Service Discovery con Netflix Eureka.

---

## Integrantes

| Nombre | Correo | GitHub |
|---|---|---|
| Martin Vásquez | mart.vasquezh@duocuc.cl | [martin69-night](https://github.com/martin69-night) |

---

## Arquitectura del Sistema

El sistema utiliza una arquitectura distribuida con API Gateway centralizado y Service Discovery mediante Eureka:

- **API Gateway** (puerto 8080) — único punto de entrada público. Enruta peticiones a los microservicios usando `lb://` (load balancing vía Eureka).
- **Eureka Server** (puerto 8761) — registro central de servicios. Los microservicios se registran automáticamente al iniciar.
- **10 microservicios** — cada uno con su base de datos MySQL independiente, documentación Swagger y pruebas unitarias.

---

## Microservicios

| Microservicio | Puerto | Responsabilidad | Swagger UI |
|---|---|---|---|
| ms-eureka | 8761 | Service Discovery (Eureka Server) | — |
| api-gateway | 8080 | Enrutamiento central vía Eureka | — |
| order-service | 8081 | Gestión de pedidos | http://localhost:8081/doc/swagger-ui.html |
| payment-service | 8082 | Gestión de pagos | http://localhost:8082/doc/swagger-ui.html |
| catalog-service | 8083 | Catálogo de productos | http://localhost:8083/doc/swagger-ui.html |
| inventory-service | 8084 | Control de inventario y stock | http://localhost:8084/doc/swagger-ui.html |
| cart-service | 8085 | Carrito de compras | http://localhost:8085/doc/swagger-ui.html |
| usuario-service | 8086 | Gestión de usuarios | http://localhost:8086/doc/swagger-ui.html |
| envio-service | 8087 | Gestión de envíos | http://localhost:8087/doc/swagger-ui.html |
| descuento-service | 8088 | Gestión de descuentos | http://localhost:8088/doc/swagger-ui.html |
| favorito-service | 8089 | Lista de favoritos | http://localhost:8089/doc/swagger-ui.html |
| notificacion-service | 8090 | Notificaciones | http://localhost:8090/doc/swagger-ui.html |

---

## Rutas del API Gateway

Todas las peticiones externas pasan por el Gateway (puerto 8080) y son enrutadas dinámicamente vía Eureka:

| Ruta Gateway | Microservicio destino | URI Eureka |
|---|---|---|
| `/api/pedidos/**` | order-service | `lb://order-service` |
| `/api/pagos/**` | payment-service | `lb://payment-service` |
| `/api/productos/**` | catalog-service | `lb://catalog-service` |
| `/api/inventario/**` | inventory-service | `lb://inventory-service` |
| `/api/carrito/**` | cart-service | `lb://cart-service` |

Filtros configurados:
- **Global:** `AddResponseHeader=X-Gateway, ecommerce-gateway` (aplicado a todas las rutas)
- **Por ruta:** `AddRequestHeader=X-Gateway-Source, api-gateway` (aplicado a order-service)

---

## Comunicación entre servicios

| Origen | Destino | Mecanismo | Propósito |
|---|---|---|---|
| order-service | inventory-service | Feign Client | Verificar stock al crear pedido |
| cart-service | catalog-service | Feign Client | Validar producto al agregar al carrito |
| favorito-service | catalog-service, usuario-service | Feign Client | Obtener datos de producto y usuario |
| notificacion-service | usuario-service | Feign Client | Obtener datos del usuario destinatario |

---

## Instrucciones de ejecución

### Requisitos
- Java 21
- Maven 3.9+
- MySQL 8.0
- Docker y Docker Compose

### Levantar con Docker Compose

```bash
# Levantar toda la infraestructura
docker compose up -d

# Levantar solo los servicios esenciales (menos carga)
docker compose up -d ms_eureka mysql_catalogo api_catalogo mysql_inventario api_inventario mysql_carrito api_carrito api_gateway
```

### Orden de arranque manual (sin Docker)

1. **ms-eureka** (primero, esperar ~20s hasta ver dashboard en http://localhost:8761)
2. Los microservicios (catalog, inventory, order, payment, cart)
3. **api-gateway** (último, necesita que los servicios estén registrados en Eureka)

```bash
cd ms-eureka && ./mvnw spring-boot:run
cd catalog-service && ./mvnw spring-boot:run
cd inventory-service && ./mvnw spring-boot:run
cd order-service && ./mvnw spring-boot:run
cd payment-service && ./mvnw spring-boot:run
cd cart-service && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
```

### Verificar Eureka

```bash
# Dashboard visual
http://localhost:8761

# Servicios registrados (JSON)
curl http://localhost:8761/eureka/apps -H "Accept: application/json"
```

### Ejecutar pruebas unitarias

```bash
cd catalog-service && mvn clean test
cd inventory-service && mvn clean test
cd order-service && mvn clean test
cd payment-service && mvn clean test
cd cart-service && mvn clean test
```

---

## Pruebas Unitarias

Pruebas implementadas en 5 microservicios cubriendo las 4 capas (Modelo, Servicio, Controlador, Repositorio) con JUnit 5 + Mockito. Cobertura medida con JaCoCo.

| Microservicio | Modelo | Servicio | Controller | Repositorio | Extras | Total |
|---|---|---|---|---|---|---|
| catalog-service | 4 | 5 | 4 | 3 | 2 | **18** |
| inventory-service | 4 | 6 | 4 | 3 | 2 | **19** |
| order-service | 4 | 5 | 4 | 3 | 3 | **19** |
| payment-service | 4 | 6 | 4 | 3 | 2 | **19** |
| cart-service | 4 | 6 | 4 | 3 | 3 | **20** |

Ver detalle en [TESTING_PLAN.md](./TESTING_PLAN.md)

---

## Colecciones Postman

- `postman/DSY1103-Ecommerce-Gateway-Postman.json` — Comunicación vía API Gateway (puerto 8080) con evidencia de interoperabilidad entre microservicios
- `postman/DSY1103-Ecommerce-Microservicios.postman_collection.json` — Peticiones directas a servicios individuales
- `postman/DSY1103-Ecommerce-10-Microservicios.postman_collection.json` — Verificación de los 10 microservicios

---

## Tecnologías utilizadas

- Spring Boot 4.0.6
- Spring Cloud 2025.1.1 (Oakwood)
- Spring Cloud Gateway Server WebFlux
- Spring Cloud Netflix Eureka (Server + Client)
- Spring Cloud OpenFeign
- Spring Data JPA + Hibernate
- MySQL 8.0
- JUnit 5 + Mockito + JaCoCo
- Bean Validation (Jakarta)
- Springdoc OpenAPI 3.0.3 (Swagger UI)
- Docker + Docker Compose
- AWS EC2 (Ubuntu)

## Tablero Trello

[Ver tablero de gestión del proyecto](./KANBAN.md)
