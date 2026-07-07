package cl.duoc.catalogservice.controller;

import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Catálogo de productos",
        description = "Operaciones para administrar y consultar los productos del e-commerce"
)
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(
            summary = "Listar productos activos",
            description = "Obtiene todos los productos que se encuentran activos en el catálogo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos activos obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Producto.class)
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene todos los productos, incluyendo los que están desactivados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Producto.class)
                            )
                    )
            )
    })
    @GetMapping("/todos")
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene el detalle de un producto mediante su identificador interno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un producto con el identificador indicado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(
            @Parameter(
                    description = "Identificador interno del producto",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar producto por SKU",
            description = "Obtiene un producto usando su SKU único."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado por SKU",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un producto con el SKU indicado"
            )
    })
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> buscarPorSku(
            @Parameter(
                    description = "SKU único del producto",
                    required = true,
                    example = "NOTEBOOK-001"
            )
            @PathVariable String sku
    ) {
        return ResponseEntity.ok(productoService.buscarPorSku(sku));
    }

    @Operation(
            summary = "Listar productos por categoría",
            description = "Obtiene los productos asociados a una categoría específica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos de la categoría obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Producto.class)
                            )
                    )
            )
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(
            @Parameter(
                    description = "Categoría de productos a consultar",
                    required = true,
                    example = "Tecnología"
            )
            @PathVariable String categoria
    ) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    @Operation(
            summary = "Buscar productos por nombre",
            description = "Busca productos activos cuyo nombre contiene el texto indicado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Producto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El texto de búsqueda es inválido"
            )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(
            @Parameter(
                    description = "Texto que se buscará en el nombre del producto",
                    required = true,
                    example = "notebook"
            )
            @RequestParam String nombre
    ) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @Operation(
            summary = "Crear producto",
            description = "Registra un producto nuevo en el catálogo. El SKU y el nombre son obligatorios; el precio debe ser mayor a cero."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o campos obligatorios ausentes"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El SKU ya se encuentra registrado"
            )
    })
    @PostMapping
    public ResponseEntity<Producto> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos necesarios para crear un producto",
                    content = @Content(
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(
                                    name = "Producto nuevo",
                                    value = """
                                            {
                                              "sku": "NOTEBOOK-001",
                                              "nombre": "Notebook Gamer 15 pulgadas",
                                              "descripcion": "Notebook con procesador Intel y tarjeta gráfica dedicada",
                                              "precio": 799990.00,
                                              "categoria": "Tecnología"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Producto producto
    ) {
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o precio no válido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El SKU ya pertenece a otro producto"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(
                    description = "Identificador del producto a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos actualizados del producto",
                    content = @Content(
                            schema = @Schema(implementation = Producto.class),
                            examples = @ExampleObject(
                                    name = "Producto actualizado",
                                    value = """
                                            {
                                              "sku": "NOTEBOOK-001",
                                              "nombre": "Notebook Gamer 15 pulgadas - Edición 2026",
                                              "descripcion": "Equipo actualizado con 16 GB de memoria RAM",
                                              "precio": 849990.00,
                                              "categoria": "Tecnología"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Producto producto
    ) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @Operation(
            summary = "Desactivar producto",
            description = "Realiza una desactivación lógica del producto. El registro permanece almacenado, pero deja de aparecer entre los productos activos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto desactivado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @Parameter(
                    description = "Identificador del producto a desactivar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reactivar producto",
            description = "Vuelve a activar un producto que fue desactivado previamente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto reactivado correctamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Producto> reactivar(
            @Parameter(
                    description = "Identificador del producto a reactivar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productoService.reactivar(id));
    }

    @Operation(
            summary = "Eliminar producto físicamente",
            description = "Elimina de forma permanente un producto desde la base de datos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFisicamente(
            @Parameter(
                    description = "Identificador del producto a eliminar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        productoService.eliminarFisicamente(id);
        return ResponseEntity.noContent().build();
    }
}
