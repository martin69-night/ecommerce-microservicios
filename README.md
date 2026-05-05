# E-commerce Microservicios

Proyecto Semestral - DSY1103 Desarrollo Full Stack 1
Duoc UC | 2026

## Descripción

Solución backend para un sistema de E-commerce basada en arquitectura de microservicios distribuidos. Cada microservicio implementa una lógica de negocio independiente y posee su propia base de datos, comunicándose a través de Eureka (Service Discovery) y un API Gateway centralizado.

## Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 4.0.6
- **Cloud:** Spring Cloud 2025.1.1
- **Persistencia:** Spring Data JPA + MySQL 8.0
- **Comunicación Sync:** OpenFeign
- **Comunicación Async:** Apache Kafka
- **Service Discovery:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **Seguridad:** Spring Security + JWT
- **Documentación:** SpringDoc OpenAPI (Swagger)
- **Testing:** JUnit 5 + Mockito
- **DevOps:** Docker + Docker Compose
- **Cloud:** AWS EC2 (Ubuntu 24.04)

## Arquitectura

| # | Microservicio | Puerto | Responsabilidad |
|---|---|---|---|
| - | eureka-server | 8761 | Service Discovery |
| - | api-gateway | 8080 | Punto de entrada + JWT |
| 1 | auth-service | 8081 | Autenticacion y JWT |
| 2 | catalog-service | 8082 | Catalogo de productos |
| 3 | inventory-service | 8083 | Gestion de stock |
| 4 | orders-service | 8084 | Ordenes y maquina de estados |
| 5 | notifications-service | 8085 | Notificaciones |

## Estado del Proyecto

| Componente | Estado |
|---|---|
| eureka-server | Implementado |
| api-gateway | En desarrollo |
| auth-service | En desarrollo |
| catalog-service | En desarrollo |
| inventory-service | En desarrollo |
| orders-service | En desarrollo |
| notifications-service | En desarrollo |

## Autor

Martin Vasquez - DSY1103 - 2026

## Estado de Microservicios

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| eureka-server | 8761 | En desarrollo | Service Discovery |
| catalog-service | 8083 | Funcional | CRUD de productos del catálogo |

## Catalog Service

El microservicio `catalog-service` implementa la gestión de productos del sistema e-commerce.

Evidencias realizadas:

- Servicio levantado en `http://localhost:8083`
- Endpoint `GET /api/productos` funcionando
- Endpoint `POST /api/productos` creando productos
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven

## Inventory Service

El microservicio `inventory-service` implementa la gestión de inventario y stock de productos.

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| inventory-service | 8084 | Funcional | Gestión de stock, reservas y reposición |

Evidencias realizadas:

- Servicio levantado en `http://localhost:8084`
- Endpoint `GET /api/inventario` funcionando
- Endpoint `POST /api/inventario` creando inventario
- Cálculo de `stockLibre`
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven

## Cart Service

El microservicio `cart-service` implementa la gestión del carrito de compras.

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| cart-service | 8085 | Funcional | Gestión de carrito de compras |

Evidencias realizadas:

- Servicio levantado en `http://localhost:8085`
- Endpoint `GET /api/carrito` funcionando
- Endpoint `POST /api/carrito` agregando productos
- Endpoint `GET /api/carrito/usuario/1/total` calculando total
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven

## Order Service

El microservicio order-service implementa la gestión de pedidos del sistema e-commerce.

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| order-service | 8081 | Funcional | Gestión de pedidos y estados |

Evidencias realizadas:

- Servicio levantado en http://localhost:8081
- Endpoint GET /api/pedidos funcionando
- Endpoint POST /api/pedidos creando pedidos
- Endpoint PATCH /api/pedidos/1/estado cambiando estado a PAGADO
- Endpoint GET /api/pedidos/usuario/1 listando pedidos por usuario
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven

## Order Service

El microservicio order-service implementa la gestión de pedidos del sistema e-commerce.

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| order-service | 8081 | Funcional | Gestión de pedidos y estados |

Evidencias realizadas:

- Servicio levantado en http://localhost:8081
- Endpoint GET /api/pedidos funcionando
- Endpoint POST /api/pedidos creando pedidos
- Endpoint PATCH /api/pedidos/1/estado cambiando estado a PAGADO
- Endpoint GET /api/pedidos/usuario/1 listando pedidos por usuario
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven

## Payment Service

El microservicio payment-service implementa la gestión de pagos del sistema e-commerce.

| Microservicio | Puerto | Estado | Descripción |
|---|---:|---|---|
| payment-service | 8082 | Funcional | Gestión de pagos y estados |

Evidencias realizadas:

- Servicio levantado en http://localhost:8082
- Endpoint GET /api/pagos funcionando
- Endpoint POST /api/pagos creando pagos
- Endpoint PATCH /api/pagos/1/aprobar cambiando estado a APROBADO
- Endpoint GET /api/pagos/pedido/1 buscando pago por pedido
- Endpoint GET /api/pagos/usuario/1 listando pagos por usuario
- Arquitectura por capas: Controller, Service, Repository, Model
- Compilación exitosa con Maven
