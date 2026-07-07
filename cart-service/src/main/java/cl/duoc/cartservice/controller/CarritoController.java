package cl.duoc.cartservice.controller;

import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.service.CarritoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Carrito de compras",
        description = "Operaciones para agregar productos, calcular totales y administrar ítems del carrito"
)
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @Operation(
            summary = "Listar ítems activos del carrito",
            description = "Obtiene todos los ítems activos registrados en los carritos de compra."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ítems activos obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CarritoItem.class)
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CarritoItem>> listarActivos() {
        return ResponseEntity.ok(carritoService.listarActivos());
    }

    @Operation(
            summary = "Listar carrito de un usuario",
            description = "Obtiene todos los ítems activos asociados a un usuario específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Carrito del usuario obtenido correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CarritoItem.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador de usuario inválido"
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> listarPorUsuario(
            @Parameter(
                    description = "Identificador del usuario propietario del carrito",
                    required = true,
                    example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Buscar ítem de carrito por ID",
            description = "Obtiene el detalle de un ítem de carrito usando su identificador interno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ítem de carrito encontrado",
                    content = @Content(schema = @Schema(implementation = CarritoItem.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un ítem de carrito con el identificador indicado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoItem> buscarPorId(
            @Parameter(
                    description = "Identificador interno del ítem del carrito",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    @Operation(
            summary = "Agregar producto al carrito",
            description = """
                    Agrega un producto al carrito de un usuario.

                    El cliente debe enviar usuarioId, SKU y cantidad. Cart Service consulta
                    Catalog Service para validar el producto, recuperar su identificador,
                    nombre y precio unitario.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto agregado correctamente al carrito",
                    content = @Content(schema = @Schema(implementation = CarritoItem.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida, campos obligatorios ausentes o cantidad no válida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un producto activo con el SKU indicado"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "No fue posible comunicarse con Catalog Service"
            )
    })
    @PostMapping
    public ResponseEntity<CarritoItem> agregarItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Usuario, SKU del producto y cantidad que se desea agregar",
                    content = @Content(
                            schema = @Schema(implementation = CarritoItem.class),
                            examples = @ExampleObject(
                                    name = "Agregar producto",
                                    value = """
                                            {
                                              "usuarioId": 1,
                                              "sku": "NOTEBOOK-001",
                                              "cantidad": 2
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody CarritoItem item
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(item));
    }

    @Operation(
            summary = "Actualizar cantidad de un ítem",
            description = "Cambia la cantidad de un producto que ya se encuentra agregado al carrito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cantidad actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = CarritoItem.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La cantidad debe ser mayor a cero"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ítem de carrito no encontrado"
            )
    })
    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<CarritoItem> actualizarCantidad(
            @Parameter(
                    description = "Identificador del ítem a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Nueva cantidad del producto",
                    required = true,
                    example = "3"
            )
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @Operation(
            summary = "Eliminar ítem del carrito",
            description = "Elimina un ítem específico del carrito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Ítem eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ítem de carrito no encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(
                    description = "Identificador del ítem a eliminar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Vaciar carrito de usuario",
            description = "Elimina todos los ítems activos asociados al carrito de un usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Carrito vaciado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador de usuario inválido"
            )
    })
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(
            @Parameter(
                    description = "Identificador del usuario cuyo carrito se vaciará",
                    required = true,
                    example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Calcular total del carrito",
            description = "Calcula el monto total de los ítems activos del carrito de un usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Total calculado correctamente",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Total de carrito",
                                    value = """
                                            {
                                              "usuarioId": 1,
                                              "total": 1599980.00
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El carrito está vacío o el identificador de usuario es inválido"
            )
    })
    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, Object>> calcularTotal(
            @Parameter(
                    description = "Identificador del usuario propietario del carrito",
                    required = true,
                    example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        BigDecimal total = carritoService.calcularTotal(usuarioId);

        return ResponseEntity.ok(Map.of(
                "usuarioId", usuarioId,
                "total", total
        ));
    }
}
