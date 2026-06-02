# Diagrama de arquitectura

Postman consume los endpoints REST de los cinco microservicios.

## Microservicios y bases de datos

Postman
  ├── catalog-service :8083  ---> catalog_db
  ├── inventory-service :8084 ---> inventory_db
  ├── cart-service :8085 ---> cart_db
  ├── order-service :8081 ---> order_db
  └── payment-service :8082 ---> payment_db

## Comunicaciones entre microservicios

cart-service ---> Feign Client ---> catalog-service
order-service ---> Feign Client ---> inventory-service

## Explicación

Cada microservicio tiene su propia base de datos independiente.

- catalog-service usa catalog_db
- inventory-service usa inventory_db
- cart-service usa cart_db
- order-service usa order_db
- payment-service usa payment_db

Los microservicios no consultan directamente la base de datos de otro microservicio.

Cuando un servicio necesita información de otro, se comunica por API REST usando Feign Client.

Ejemplos implementados:

1. cart-service consulta a catalog-service para obtener datos del producto por SKU.
2. order-service consulta a inventory-service para validar y reservar stock antes de crear un pedido.
