# Evidencia final - 10 microservicios

El proyecto queda compuesto por 10 microservicios Spring Boot y 10 bases de datos MySQL independientes.

## Microservicios principales

| Microservicio | Contenedor API | Puerto API | Base de datos | Contenedor MySQL | Puerto MySQL |
|---|---|---:|---|---|---:|
| catalog-service | api_catalogo | 8083 | catalog_db | mysql_catalogo | 3306 |
| inventory-service | api_inventario | 8084 | inventory_db | mysql_inventario | 3307 |
| cart-service | api_carrito | 8085 | cart_db | mysql_carrito | 3308 |
| order-service | api_ordenes | 8081 | order_db | mysql_ordenes | 3309 |
| payment-service | api_pagos | 8082 | payment_db | mysql_pagos | 3310 |
| usuario-service | api_usuarios | 8086 | usuario_db | mysql_usuarios | 3311 |
| envio-service | api_envios | 8087 | envio_db | mysql_envios | 3312 |
| descuento-service | api_descuentos | 8088 | descuento_db | mysql_descuentos | 3313 |
| favorito-service | api_favoritos | 8089 | favorito_db | mysql_favoritos | 3314 |
| notificacion-service | api_notificaciones | 8090 | notificacion_db | mysql_notificaciones | 3315 |

## Pruebas realizadas

Los microservicios se probaron en dos grupos para evitar sobrecargar el entorno.

### Grupo 1
- catalog-service
- inventory-service
- cart-service
- order-service
- payment-service

Endpoints probados:
- GET /api/productos
- GET /api/inventario
- GET /api/carrito
- GET /api/pedidos
- GET /api/pagos

Resultado: HTTP 200.

### Grupo 2
- usuario-service
- envio-service
- descuento-service
- favorito-service
- notificacion-service

Endpoints probados:
- GET /api/usuarios
- GET /api/envios
- GET /api/descuentos
- GET /api/favoritos
- GET /api/notificaciones

Resultado: HTTP 200.

## Comunicación entre servicios

Se mantiene comunicación entre microservicios mediante OpenFeign:
- cart-service consulta catalog-service.
- order-service consulta inventory-service.
- favorito-service valida usuario-service y catalog-service.
- notificacion-service valida usuario-service.

## Nota técnica

Cada base MySQL corre en su propio contenedor independiente. Internamente MySQL usa el puerto 3306, pero externamente cada base se expone en un puerto distinto desde 3306 hasta 3315.
