#!/usr/bin/env bash
set -e

python3 <<'PY'
from pathlib import Path
import shutil
import re

root = Path(".").resolve()

POM_ORIGEN = root / "order-service" / "pom.xml"
if not POM_ORIGEN.exists():
    POM_ORIGEN = root / "catalog-service" / "pom.xml"

if not POM_ORIGEN.exists():
    raise SystemExit("No encontré pom.xml base en order-service ni catalog-service")

services = [
    {
        "folder": "usuario-service",
        "pkg": "usuarioservice",
        "app": "UsuarioServiceApplication",
        "entity": "Usuario",
        "repo": "UsuarioRepository",
        "service": "UsuarioService",
        "controller": "UsuarioController",
        "request": "UsuarioRequest",
        "response": "UsuarioResponse",
        "endpoint": "/api/usuarios",
        "table": "usuarios",
        "api_container": "api_usuarios",
        "mysql_container": "mysql_usuarios",
        "api_port": "8086",
        "mysql_port": "3311",
        "db": "usuario_db",
        "fields": [
            ("String", "nombre", '@NotBlank(message = "El nombre es obligatorio")\n    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")'),
            ("String", "email", '@NotBlank(message = "El email es obligatorio")\n    @Email(message = "El email no tiene un formato valido")'),
            ("String", "rol", '@NotBlank(message = "El rol es obligatorio")'),
            ("Boolean", "activo", '@NotNull(message = "El estado activo es obligatorio")'),
        ],
        "feign": []
    },
    {
        "folder": "envio-service",
        "pkg": "envioservice",
        "app": "EnvioServiceApplication",
        "entity": "Envio",
        "repo": "EnvioRepository",
        "service": "EnvioService",
        "controller": "EnvioController",
        "request": "EnvioRequest",
        "response": "EnvioResponse",
        "endpoint": "/api/envios",
        "table": "envios",
        "api_container": "api_envios",
        "mysql_container": "mysql_envios",
        "api_port": "8087",
        "mysql_port": "3312",
        "db": "envio_db",
        "fields": [
            ("Long", "pedidoId", '@NotNull(message = "El pedidoId es obligatorio")'),
            ("String", "direccion", '@NotBlank(message = "La direccion es obligatoria")'),
            ("String", "ciudad", '@NotBlank(message = "La ciudad es obligatoria")'),
            ("String", "estado", '@NotBlank(message = "El estado es obligatorio")'),
        ],
        "feign": []
    },
    {
        "folder": "descuento-service",
        "pkg": "descuentoservice",
        "app": "DescuentoServiceApplication",
        "entity": "Descuento",
        "repo": "DescuentoRepository",
        "service": "DescuentoService",
        "controller": "DescuentoController",
        "request": "DescuentoRequest",
        "response": "DescuentoResponse",
        "endpoint": "/api/descuentos",
        "table": "descuentos",
        "api_container": "api_descuentos",
        "mysql_container": "mysql_descuentos",
        "api_port": "8088",
        "mysql_port": "3313",
        "db": "descuento_db",
        "fields": [
            ("String", "codigo", '@NotBlank(message = "El codigo es obligatorio")'),
            ("BigDecimal", "porcentaje", '@NotNull(message = "El porcentaje es obligatorio")\n    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a cero")\n    @DecimalMax(value = "100.00", message = "El porcentaje no puede superar 100")'),
            ("Boolean", "activo", '@NotNull(message = "El estado activo es obligatorio")'),
        ],
        "feign": []
    },
    {
        "folder": "favorito-service",
        "pkg": "favoritoservice",
        "app": "FavoritoServiceApplication",
        "entity": "Favorito",
        "repo": "FavoritoRepository",
        "service": "FavoritoService",
        "controller": "FavoritoController",
        "request": "FavoritoRequest",
        "response": "FavoritoResponse",
        "endpoint": "/api/favoritos",
        "table": "favoritos",
        "api_container": "api_favoritos",
        "mysql_container": "mysql_favoritos",
        "api_port": "8089",
        "mysql_port": "3314",
        "db": "favorito_db",
        "fields": [
            ("Long", "usuarioId", '@NotNull(message = "El usuarioId es obligatorio")'),
            ("Long", "productoId", '@NotNull(message = "El productoId es obligatorio")'),
            ("String", "sku", '@NotBlank(message = "El sku es obligatorio")'),
        ],
        "feign": ["usuario", "catalogo"]
    },
    {
        "folder": "notificacion-service",
        "pkg": "notificacionservice",
        "app": "NotificacionServiceApplication",
        "entity": "Notificacion",
        "repo": "NotificacionRepository",
        "service": "NotificacionService",
        "controller": "NotificacionController",
        "request": "NotificacionRequest",
        "response": "NotificacionResponse",
        "endpoint": "/api/notificaciones",
        "table": "notificaciones",
        "api_container": "api_notificaciones",
        "mysql_container": "mysql_notificaciones",
        "api_port": "8090",
        "mysql_port": "3315",
        "db": "notificacion_db",
        "fields": [
            ("Long", "usuarioId", '@NotNull(message = "El usuarioId es obligatorio")'),
            ("String", "canal", '@NotBlank(message = "El canal es obligatorio")'),
            ("String", "mensaje", '@NotBlank(message = "El mensaje es obligatorio")\n    @Size(min = 3, max = 255, message = "El mensaje debe tener entre 3 y 255 caracteres")'),
            ("String", "estado", '@NotBlank(message = "El estado es obligatorio")'),
        ],
        "feign": ["usuario"]
    },
]

def cap(s):
    return s[0].upper() + s[1:]

def setter(field):
    return "set" + cap(field)

def getter(field):
    return "get" + cap(field)

def write(path, content):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content.strip() + "\n")

def getters_setters(fields):
    txt = []
    for typ, name in fields:
        txt.append(f"""
    public {typ} {getter(name)}() {{
        return {name};
    }}

    public void {setter(name)}({typ} {name}) {{
        this.{name} = {name};
    }}
""")
    return "\n".join(txt)

def update_pom(pom, folder, pkg, app):
    pom = pom.replace("order-service", folder)
    pom = pom.replace("OrderServiceApplication", app)
    pom = pom.replace("cl.duoc.orderservice", f"cl.duoc.{pkg}")
    pom = pom.replace("catalog-service", folder)
    pom = pom.replace("CatalogServiceApplication", app)
    pom = pom.replace("cl.duoc.catalogservice", f"cl.duoc.{pkg}")
    if "spring-cloud-starter-openfeign" not in pom and "openfeign" not in pom:
        pom = pom.replace("</dependencies>", """
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
</dependencies>""")
    return pom

def create_service(s):
    folder = root / s["folder"]
    if folder.exists():
        shutil.rmtree(folder)
    folder.mkdir()

    pom = update_pom(POM_ORIGEN.read_text(), s["folder"], s["pkg"], s["app"])
    write(folder / "pom.xml", pom)

    base = folder / "src" / "main" / "java" / "cl" / "duoc" / s["pkg"]
    resources = folder / "src" / "main" / "resources"

    enable_feign = "\nimport org.springframework.cloud.openfeign.EnableFeignClients;" if s["feign"] else ""
    enable_anno = "\n@EnableFeignClients" if s["feign"] else ""

    write(base / f"{s['app']}.java", f"""
package cl.duoc.{s['pkg']};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;{enable_feign}

@SpringBootApplication{enable_anno}
public class {s['app']} {{
    public static void main(String[] args) {{
        SpringApplication.run({s['app']}.class, args);
    }}
}}
""")

    entity_fields = [("Long", "id")] + [(t, n) for t, n, _ in s["fields"]] + [("LocalDateTime", "fechaCreacion")]
    entity_columns = ""
    for typ, name, *_ in entity_fields:
        if name == "id":
            entity_columns += """
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
"""
        elif typ == "BigDecimal":
            entity_columns += f"""
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal {name};
"""
        else:
            entity_columns += f"""
    @Column(nullable = false)
    private {typ} {name};
"""

    write(base / "model" / f"{s['entity']}.java", f"""
package cl.duoc.{s['pkg']}.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "{s['table']}")
public class {s['entity']} {{
{entity_columns}

{getters_setters(entity_fields)}
}}
""")

    request_fields = ""
    for typ, name, annotations in s["fields"]:
        request_fields += f"""
    {annotations}
    private {typ} {name};
"""
    write(base / "dto" / f"{s['request']}.java", f"""
package cl.duoc.{s['pkg']}.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class {s['request']} {{
{request_fields}

{getters_setters([(t, n) for t, n, _ in s["fields"]])}
}}
""")

    response_fields = ""
    for typ, name in entity_fields:
        response_fields += f"""
    private {typ} {name};
"""
    write(base / "dto" / f"{s['response']}.java", f"""
package cl.duoc.{s['pkg']}.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class {s['response']} {{
{response_fields}

{getters_setters(entity_fields)}
}}
""")

    write(base / "repository" / f"{s['repo']}.java", f"""
package cl.duoc.{s['pkg']}.repository;

import cl.duoc.{s['pkg']}.model.{s['entity']};
import org.springframework.data.jpa.repository.JpaRepository;

public interface {s['repo']} extends JpaRepository<{s['entity']}, Long> {{
}}
""")

    write(base / "exception" / "RecursoNoEncontradoException.java", f"""
package cl.duoc.{s['pkg']}.exception;

public class RecursoNoEncontradoException extends RuntimeException {{
    public RecursoNoEncontradoException(String mensaje) {{
        super(mensaje);
    }}
}}
""")

    write(base / "exception" / "GlobalExceptionHandler.java", f"""
package cl.duoc.{s['pkg']}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {{

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {{
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", "Recurso no encontrado",
                "mensaje", ex.getMessage()
        ));
    }}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {{
        String mensaje = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Solicitud invalida"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "Solicitud invalida",
                "mensaje", mensaje
        ));
    }}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumento(IllegalArgumentException ex) {{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "Solicitud invalida",
                "mensaje", ex.getMessage()
        ));
    }}
}}
""")

    client_imports = ""
    client_fields = ""
    constructor_params = f"{s['repo']} repository"
    constructor_body = "        this.repository = repository;\n"
    pre_validation = ""

    if "usuario" in s["feign"]:
        write(base / "client" / "UsuarioClient.java", f"""
package cl.duoc.{s['pkg']}.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-client", url = "${{usuario.service.url}}")
public interface UsuarioClient {{
    @GetMapping("/api/usuarios/{{id}}")
    Object buscarPorId(@PathVariable("id") Long id);
}}
""")
        client_imports += f"import cl.duoc.{s['pkg']}.client.UsuarioClient;\n"
        client_fields += "    private final UsuarioClient usuarioClient;\n"
        constructor_params += ", UsuarioClient usuarioClient"
        constructor_body += "        this.usuarioClient = usuarioClient;\n"
        pre_validation += """
        validarUsuario(dto.getUsuarioId());
"""

    if "catalogo" in s["feign"]:
        write(base / "client" / "CatalogClient.java", f"""
package cl.duoc.{s['pkg']}.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-client", url = "${{catalog.service.url}}")
public interface CatalogClient {{
    @GetMapping("/api/productos/{{id}}")
    Object buscarPorId(@PathVariable("id") Long id);
}}
""")
        client_imports += f"import cl.duoc.{s['pkg']}.client.CatalogClient;\n"
        client_fields += "    private final CatalogClient catalogClient;\n"
        constructor_params += ", CatalogClient catalogClient"
        constructor_body += "        this.catalogClient = catalogClient;\n"
        pre_validation += """
        validarProducto(dto.getProductoId());
"""

    set_create = ""
    for typ, name, _ in s["fields"]:
        set_create += f"        entity.{setter(name)}(dto.{getter(name)}());\n"
    set_create += "        entity.setFechaCreacion(LocalDateTime.now());\n"

    set_update = ""
    for typ, name, _ in s["fields"]:
        set_update += f"        entity.{setter(name)}(dto.{getter(name)}());\n"

    map_response = ""
    for typ, name in entity_fields:
        map_response += f"        dto.{setter(name)}(entity.{getter(name)}());\n"

    validation_methods = ""
    if "usuario" in s["feign"]:
        validation_methods += """
    private void validarUsuario(Long usuarioId) {
        try {
            usuarioClient.buscarPorId(usuarioId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("El usuario indicado no existe o no esta disponible");
        }
    }
"""
    if "catalogo" in s["feign"]:
        validation_methods += """
    private void validarProducto(Long productoId) {
        try {
            catalogClient.buscarPorId(productoId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("El producto indicado no existe o no esta disponible");
        }
    }
"""

    write(base / "service" / f"{s['service']}.java", f"""
package cl.duoc.{s['pkg']}.service;

import cl.duoc.{s['pkg']}.dto.{s['request']};
import cl.duoc.{s['pkg']}.dto.{s['response']};
import cl.duoc.{s['pkg']}.exception.RecursoNoEncontradoException;
import cl.duoc.{s['pkg']}.model.{s['entity']};
import cl.duoc.{s['pkg']}.repository.{s['repo']};
{client_imports}import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class {s['service']} {{

    private static final Logger log = LoggerFactory.getLogger({s['service']}.class);

    private final {s['repo']} repository;
{client_fields}
    public {s['service']}({constructor_params}) {{
{constructor_body}    }}

    public List<{s['response']}> listar() {{
        return repository.findAll().stream().map(this::toResponse).toList();
    }}

    public {s['response']} buscarPorId(Long id) {{
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("{s['entity']} no encontrado con id " + id));
    }}

    @Transactional
    public {s['response']} crear({s['request']} dto) {{
{pre_validation}
        {s['entity']} entity = new {s['entity']}();
{set_create}
        {s['entity']} guardado = repository.save(entity);
        log.info("{s['entity']} creado con id {{}}", guardado.getId());
        return toResponse(guardado);
    }}

    @Transactional
    public {s['response']} actualizar(Long id, {s['request']} dto) {{
{pre_validation}
        {s['entity']} entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("{s['entity']} no encontrado con id " + id));

{set_update}
        {s['entity']} actualizado = repository.save(entity);
        log.info("{s['entity']} actualizado con id {{}}", actualizado.getId());
        return toResponse(actualizado);
    }}

    @Transactional
    public void eliminar(Long id) {{
        if (!repository.existsById(id)) {{
            throw new RecursoNoEncontradoException("{s['entity']} no encontrado con id " + id);
        }}
        repository.deleteById(id);
        log.info("{s['entity']} eliminado con id {{}}", id);
    }}
{validation_methods}
    private {s['response']} toResponse({s['entity']} entity) {{
        {s['response']} dto = new {s['response']}();
{map_response}
        return dto;
    }}
}}
""")

    write(base / "controller" / f"{s['controller']}.java", f"""
package cl.duoc.{s['pkg']}.controller;

import cl.duoc.{s['pkg']}.dto.{s['request']};
import cl.duoc.{s['pkg']}.dto.{s['response']};
import cl.duoc.{s['pkg']}.service.{s['service']};
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("{s['endpoint']}")
public class {s['controller']} {{

    private final {s['service']} service;

    public {s['controller']}({s['service']} service) {{
        this.service = service;
    }}

    @GetMapping
    public ResponseEntity<List<{s['response']}>> listar() {{
        return ResponseEntity.ok(service.listar());
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{s['response']}> buscarPorId(@PathVariable Long id) {{
        return ResponseEntity.ok(service.buscarPorId(id));
    }}

    @PostMapping
    public ResponseEntity<{s['response']}> crear(@Valid @RequestBody {s['request']} request) {{
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }}

    @PutMapping("/{{id}}")
    public ResponseEntity<{s['response']}> actualizar(@PathVariable Long id, @Valid @RequestBody {s['request']} request) {{
        return ResponseEntity.ok(service.actualizar(id, request));
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {{
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }}
}}
""")

    write(resources / "application.properties", f"""
spring.application.name={s['folder']}
server.port={s['api_port']}

spring.datasource.url=${{SPRING_DATASOURCE_URL:jdbc:mysql://localhost:{s['mysql_port']}/{s['db']}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}}
spring.datasource.username=${{SPRING_DATASOURCE_USERNAME:desarrollador}}
spring.datasource.password=${{SPRING_DATASOURCE_PASSWORD:password_seguro_123}}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

eureka.client.enabled=false

usuario.service.url=${{USUARIO_SERVICE_URL:http://localhost:8086}}
catalog.service.url=${{CATALOG_SERVICE_URL:http://localhost:8083}}
""")

for s in services:
    create_service(s)

compose_path = root / "docker-compose.yml"
compose = compose_path.read_text()

new_blocks = []
for s in services:
    if s["api_container"] in compose and s["mysql_container"] in compose:
        continue

    extra_env = ""
    extra_depends = ""

    if "usuario" in s["feign"]:
        extra_env += "\n      USUARIO_SERVICE_URL: http://api_usuarios:8086"
        extra_depends += """
      api_usuarios:
        condition: service_started"""
    if "catalogo" in s["feign"]:
        extra_env += "\n      CATALOG_SERVICE_URL: http://api_catalogo:8083"
        extra_depends += """
      api_catalogo:
        condition: service_started"""

    block = f"""

  {s['mysql_container']}:
    image: mysql:8.0
    container_name: {s['mysql_container']}
    ports:
      - "{s['mysql_port']}:3306"
    environment:
      MYSQL_DATABASE: {s['db']}
      MYSQL_USER: desarrollador
      MYSQL_PASSWORD: password_seguro_123
      MYSQL_ROOT_PASSWORD: root_seguro_123
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 10
    networks:
      - ecommerce-net

  {s['api_container']}:
    image: maven:3.9-eclipse-temurin-21
    container_name: {s['api_container']}
    working_dir: /app
    volumes:
      - ./{s['folder']}:/app
      - maven_repo:/root/.m2
    command: mvn spring-boot:run
    ports:
      - "{s['api_port']}:{s['api_port']}"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://{s['mysql_container']}:3306/{s['db']}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: desarrollador
      SPRING_DATASOURCE_PASSWORD: password_seguro_123
      EUREKA_CLIENT_ENABLED: "false"{extra_env}
    depends_on:
      {s['mysql_container']}:
        condition: service_healthy{extra_depends}
    networks:
      - ecommerce-net
"""
    new_blocks.append(block)

if new_blocks:
    marker = "\nnetworks:"
    pos = compose.rfind(marker)
    if pos == -1:
        raise SystemExit("No encontré bloque networks: en docker-compose.yml")
    compose = compose[:pos] + "".join(new_blocks) + compose[pos:]
    compose_path.write_text(compose)

print("Listo: servicios creados y docker-compose.yml actualizado")
PY
