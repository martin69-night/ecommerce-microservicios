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
