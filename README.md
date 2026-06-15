# E-commerce Microservicios - DSY1103

Proyecto backend desarrollado para la asignatura DSY1103 - Desarrollo Full Stack 1.

El sistema representa un backend de e-commerce construido con microservicios independientes usando Java, Spring Boot, Maven, Spring Data JPA, MySQL, Docker Compose, OpenFeign, Postman y logs con SLF4J.

## Objetivo

Implementar una arquitectura de microservicios donde cada servicio tenga una responsabilidad propia, su propia base de datos y comunicación entre servicios mediante API REST.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven
- Spring Data JPA
- MySQL 8
- Docker Compose
- OpenFeign
- Postman
- SLF4J

## Microservicios principales

- catalog-service: puerto 8083, base catalog_db, gestiona productos.
- inventory-service: puerto 8084, base inventory_db, gestiona stock.
- cart-service: puerto 8085, base cart_db, gestiona carrito.
- order-service: puerto 8081, base order_db, gestiona pedidos.
- payment-service: puerto 8082, base payment_db, gestiona pagos.

El proyecto tambien incluye eureka-server en el puerto 8761, pero en esta entrega los servicios principales se comunican usando Feign Client con URL directa.

## Bases de datos independientes

Cada microservicio usa su propia base de datos:

- catalog-service usa catalog_db
- inventory-service usa inventory_db
- cart-service usa cart_db
- order-service usa order_db
- payment-service usa payment_db

En el despliegue final con Docker Compose, cada microservicio tiene su propio contenedor MySQL y su propia base de datos independiente.

Los microservicios no consultan directamente la base de datos de otro microservicio. Cuando necesitan informacion externa, se comunican por API REST usando Feign Client.

## Comunicacion entre microservicios

Comunicaciones implementadas:

1. cart-service consulta a catalog-service mediante Feign Client para obtener datos del producto por SKU antes de agregarlo al carrito.
2. order-service consulta a inventory-service mediante Feign Client para validar y reservar stock antes de crear un pedido.

## Arquitectura

El diagrama de arquitectura esta documentado en:

docs/arquitectura.md

Resumen:

Postman consume los endpoints REST de los cinco microservicios.

- catalog-service :8083 ---> catalog_db
- inventory-service :8084 ---> inventory_db
- cart-service :8085 ---> cart_db
- order-service :8081 ---> order_db
- payment-service :8082 ---> payment_db

Comunicaciones Feign:

- cart-service ---> catalog-service
- order-service ---> inventory-service

## Endpoints principales

### catalog-service

Base URL:

http://localhost:8083/api/productos

Endpoints:

- GET /api/productos
- GET /api/productos/{id}
- GET /api/productos/sku/{sku}
- POST /api/productos
- PUT /api/productos/{id}
- DELETE /api/productos/{id}

### inventory-service

Base URL:

http://localhost:8084/api/inventario

Endpoints:

- GET /api/inventario
- GET /api/inventario/{id}
- GET /api/inventario/sku/{sku}
- POST /api/inventario
- PUT /api/inventario/{id}
- PATCH /api/inventario/{id}/reservar?cantidad=2
- PATCH /api/inventario/{id}/liberar?cantidad=2
- PATCH /api/inventario/{id}/descontar?cantidad=2
- PATCH /api/inventario/{id}/reponer?cantidad=2
- DELETE /api/inventario/{id}

### cart-service

Base URL:

http://localhost:8085/api/carrito

Endpoints:

- GET /api/carrito
- GET /api/carrito/{id}
- GET /api/carrito/usuario/{usuarioId}
- POST /api/carrito
- PATCH /api/carrito/{id}/cantidad?cantidad=2
- DELETE /api/carrito/{id}
- DELETE /api/carrito/usuario/{usuarioId}

### order-service

Base URL:

http://localhost:8081/api/pedidos

Endpoints:

- GET /api/pedidos
- GET /api/pedidos/{id}
- GET /api/pedidos/usuario/{usuarioId}
- GET /api/pedidos/estado/{estado}
- POST /api/pedidos
- PATCH /api/pedidos/{id}/estado?estado=PAGADO
- PATCH /api/pedidos/{id}/cancelar

### payment-service

Base URL:

http://localhost:8082/api/pagos

Endpoints:

- GET /api/pagos
- GET /api/pagos/{id}
- GET /api/pagos/pedido/{pedidoId}
- POST /api/pagos
- PATCH /api/pagos/{id}/aprobar
- PATCH /api/pagos/{id}/rechazar

## Flujos probados con Postman

### Flujo 1: catalog-service y cart-service

1. Crear producto en catalog-service.
2. Agregar producto al carrito desde cart-service enviando solo usuarioId, sku y cantidad.
3. cart-service consulta a catalog-service mediante Feign Client.
4. cart-service completa productoId, nombreProducto, precioUnitario y subtotal.

### Flujo 2: inventory-service y order-service

1. Crear inventario en inventory-service.
2. Crear pedido en order-service.
3. order-service consulta a inventory-service mediante Feign Client.
4. order-service valida stock disponible.
5. order-service reserva stock.
6. order-service crea el pedido.

### Flujo 3: payment-service

1. Crear pago.
2. Consultar pagos.
3. Aprobar pago.

## Manejo de errores

El proyecto implementa manejo de errores con RestControllerAdvice.

Pruebas realizadas:

- GET a recurso inexistente devuelve 404 Not Found.
- POST con datos invalidos devuelve 400 Bad Request.
- DELETE exitoso devuelve 204 No Content.
- Errores de negocio como SKU duplicado o stock insuficiente devuelven respuesta controlada.

## Ejemplos de pruebas

Crear producto:

POST http://localhost:8083/api/productos

JSON:

{
  "sku": "SKU-FEIGN-001",
  "nombre": "Notebook Lenovo",
  "descripcion": "Notebook para estudiantes",
  "precio": 599990,
  "categoria": "Computacion",
  "activo": true
}

Agregar producto al carrito:

POST http://localhost:8085/api/carrito

JSON:

{
  "usuarioId": 1,
  "sku": "SKU-FEIGN-001",
  "cantidad": 2
}

Crear inventario:

POST http://localhost:8084/api/inventario

JSON:

{
  "productoId": 1,
  "sku": "SKU-ORDER-001",
  "cantidadDisponible": 10,
  "cantidadReservada": 0,
  "ubicacion": "Bodega Central",
  "activo": true
}

Crear pedido:

POST http://localhost:8081/api/pedidos

JSON:

{
  "usuarioId": 1,
  "productoId": 1,
  "sku": "SKU-ORDER-001",
  "nombreProducto": "Notebook Lenovo",
  "precioUnitario": 599990,
  "cantidad": 2
}

Crear pago:

POST http://localhost:8082/api/pagos

JSON:

{
  "pedidoId": 1,
  "usuarioId": 1,
  "monto": 1199980,
  "metodoPago": "TARJETA"
}

Error 404 controlado:

GET http://localhost:8083/api/productos/99999

Error 400 controlado:

POST http://localhost:8085/api/carrito

JSON:

{
  "usuarioId": 1,
  "sku": "SKU-FEIGN-001",
  "cantidad": 0
}

DELETE exitoso:

DELETE http://localhost:8084/api/inventario/2

Respuesta esperada:

204 No Content

## Ejecucion del proyecto

Entrar al proyecto:

cd /home/ubuntu/entorno-desarrollo/codigo-fuente

Levantar contenedores:

docker compose up -d

Verificar contenedores:

docker ps

Ejecutar un microservicio:

cd /app/catalog-service
mvn spring-boot:run

Ejecutar los demas servicios cambiando la carpeta:

cd /app/inventory-service
mvn spring-boot:run

cd /app/cart-service
mvn spring-boot:run

cd /app/order-service
mvn spring-boot:run

cd /app/payment-service
mvn spring-boot:run

## Compilacion

Compilar un servicio:

cd /app/catalog-service
mvn clean compile -DskipTests

Compilar los cinco servicios principales:

cd /app

for s in catalog-service inventory-service cart-service order-service payment-service
do
  echo "Compilando $s"
  cd /app/$s
  mvn clean compile -DskipTests || exit 1
done

## Coleccion Postman

La coleccion Postman debe guardarse en la carpeta:

postman/

Nombre sugerido:

DSY1103-Ecommerce-Microservicios.postman_collection.json

## Repositorio

https://github.com/martin69-night/ecommerce-microservicios

## Notas importantes

- No se usa Kafka.
- No se usa API Gateway.
- No se usa JWT.
- No se usa Spring Security.
- No se usa Swagger.
- Eureka esta incluido, pero no se usa en el flujo principal de esta entrega.

## Despliegue final con Docker Compose

El despliegue final se ejecuta con 10 contenedores:

- api_catalogo usa mysql_catalogo y la base catalog_db.
- api_inventario usa mysql_inventario y la base inventory_db.
- api_carrito usa mysql_carrito y la base cart_db.
- api_ordenes usa mysql_ordenes y la base order_db.
- api_pagos usa mysql_pagos y la base payment_db.

Los contenedores con imagen Maven/Java corresponden a los microservicios Spring Boot.
Los contenedores con imagen MySQL corresponden a las bases de datos independientes.

Las comunicaciones entre microservicios implementadas con Feign Client son:

- api_carrito se comunica con api_catalogo.
- api_ordenes se comunica con api_inventario.


## Nota sobre URLs

Los ejemplos del README usan `localhost` para facilitar las pruebas locales o mediante puertos reenviados desde VS Code.

En el despliegue sobre EC2 también se pueden consumir los servicios usando la IP pública de la instancia:

- http://100.30.103.13:8083/api/productos
- http://100.30.103.13:8084/api/inventario
- http://100.30.103.13:8085/api/carrito
- http://100.30.103.13:8081/api/pedidos
- http://100.30.103.13:8082/api/pagos

En la demostración se pueden usar tanto `localhost` como la IP pública, dependiendo de si se prueba desde la misma máquina, desde VS Code con puertos reenviados o directamente contra la EC2.

---

## Actualización final: 10 microservicios independientes

El proyecto fue ampliado a 10 microservicios independientes, cada uno con su propio contenedor API y su propia base de datos MySQL.

| Microservicio | API | Puerto API | Base de datos | MySQL | Puerto MySQL |
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

Las APIs se exponen desde el puerto 8081 hasta el 8090.  
Las bases MySQL se exponen desde el puerto 3306 hasta el 3315.

La evidencia técnica se encuentra en:

- `docs/evidencias/docker_ps_10_microservicios.txt`
- `docs/evidencias/resumen_10_microservicios.md`

