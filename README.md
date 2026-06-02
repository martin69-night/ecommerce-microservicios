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

Todas las bases se ejecutan en el mismo contenedor MySQL por simplicidad de despliegue, pero cada microservicio tiene su propia base logica y sus propias tablas.

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
