# Evidencia de pruebas Postman - 10 microservicios

Se agregó una colección Postman para probar los 10 microservicios del proyecto.

Archivo:

- `postman/DSY1103-Ecommerce-10-Microservicios.postman_collection.json`

La colección usa la variable:

- `base_url = http://18.206.169.13`

Endpoints incluidos:

| Servicio | Endpoint | Puerto |
|---|---|---:|
| catalog-service | GET /api/productos | 8083 |
| inventory-service | GET /api/inventario | 8084 |
| cart-service | GET /api/carrito | 8085 |
| order-service | GET /api/pedidos | 8081 |
| payment-service | GET /api/pagos | 8082 |
| usuario-service | GET /api/usuarios | 8086 |
| envio-service | GET /api/envios | 8087 |
| descuento-service | GET /api/descuentos | 8088 |
| favorito-service | GET /api/favoritos | 8089 |
| notificacion-service | GET /api/notificaciones | 8090 |

Los microservicios fueron probados en dos grupos para evitar sobrecargar el entorno.
