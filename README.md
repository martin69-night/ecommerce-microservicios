# E-Commerce Microservicios
## DSY1103 — Desarrollo Fullstack I

Arquitectura de microservicios para una plataforma de e-commerce desarrollada con Spring Boot 4.x y Spring Cloud.

---

## Microservicios

| Microservicio | Puerto | Responsabilidad | Swagger UI |
|---|---|---|---|
| api-gateway | 8080 | Enrutamiento central de todos los servicios | — |
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

## Comunicación entre servicios

- **order-service** consulta stock a **inventory-service** via Feign Client
- **cart-service** consulta productos a **catalog-service** via Feign Client
- **api-gateway** enruta todas las peticiones externas a los microservicios correspondientes

---

## Instrucciones de ejecución local

### Requisitos
- Java 21
- Maven 3.9+
- MySQL 8.0
- Docker (opcional)

### Levantar con Docker Compose
```bash
docker-compose up -d
```

### Levantar manualmente cada servicio
```bash
cd catalog-service && ./mvnw spring-boot:run
cd inventory-service && ./mvnw spring-boot:run
cd order-service && ./mvnw spring-boot:run
cd payment-service && ./mvnw spring-boot:run
cd cart-service && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
```

### Ejecutar pruebas unitarias
```bash
cd catalog-service && mvn test
cd inventory-service && mvn test
cd order-service && mvn test
cd payment-service && mvn test
cd cart-service && mvn test
```

---

## Pruebas Unitarias

Todos los microservicios principales tienen las 4 capas de test implementadas:

| Microservicio | Modelo | Servicio | Controller | Repositorio | Total |
|---|---|---|---|---|---|
| catalog-service | 4 | 5 | 4 | 3 | **16** |
| inventory-service | 4 | 6 | 4 | 3 | **17** |
| order-service | 4 | 5 | 4 | 3 | **16** |
| payment-service | 4 | 6 | 4 | 3 | **17** |
| cart-service | 4 | 6 | 4 | 3 | **17** |
| **TOTAL** | **20** | **28** | **20** | **15** | **83** |

Ver detalle en [TESTING_PLAN.md](./TESTING_PLAN.md)

---

## Tecnologías utilizadas

- Spring Boot 4.0.6
- Spring Cloud 2025.1.1
- Spring Cloud Gateway (WebFlux)
- Spring Cloud OpenFeign
- Spring Data JPA
- MySQL 8.0
- JUnit 5 + Mockito
- Springdoc OpenAPI 3.0.3 (Swagger UI)
- Docker + Docker Compose
