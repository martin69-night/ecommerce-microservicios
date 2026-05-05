# Catalog Service

Microservicio encargado de gestionar el catalogo de productos del sistema de e-commerce.

## Arquitectura

Este servicio usa arquitectura por capas:

- Controller
- Service
- Repository
- Model
- Exception Handler

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Docker

## Puerto

catalog-service: 8083

## Entidad principal

Producto:

- id
- sku
- nombre
- descripcion
- precio
- categoria
- activo

## Endpoints

| Metodo | Endpoint | Descripcion |
|---|---|---|
| GET | / | Estado del servicio |
| GET | /api/productos | Lista productos activos |
| GET | /api/productos/todos | Lista todos los productos |
| GET | /api/productos/{id} | Busca producto por ID |
| GET | /api/productos/sku/{sku} | Busca producto por SKU |
| GET | /api/productos/categoria/{categoria} | Lista productos por categoria |
| GET | /api/productos/buscar?nombre=texto | Busca productos por nombre |
| POST | /api/productos | Crea un producto |
| PUT | /api/productos/{id} | Actualiza un producto |
| PATCH | /api/productos/{id}/desactivar | Desactiva un producto |
| PATCH | /api/productos/{id}/reactivar | Reactiva un producto |
| DELETE | /api/productos/{id} | Elimina fisicamente un producto |

## Pruebas realizadas

- El servicio responde en http://localhost:8083
- El endpoint GET /api/productos lista productos correctamente
- El endpoint POST /api/productos crea productos correctamente
- El proyecto compila con mvn clean compile -DskipTests

## Requerimientos funcionales

- RF01: Crear productos.
- RF02: Listar productos activos.
- RF03: Buscar productos por ID.
- RF04: Buscar productos por SKU.
- RF05: Filtrar productos por categoria.
- RF06: Desactivar productos sin eliminarlos fisicamente.

## Requerimientos no funcionales

- RNF01: El microservicio debe ejecutarse de forma independiente.
- RNF02: El microservicio debe conectarse a MySQL.
- RNF03: El microservicio debe exponer una API REST.
- RNF04: El codigo debe separar Controller, Service y Repository.
- RNF05: El servicio debe ser ejecutable en contenedor Docker.
