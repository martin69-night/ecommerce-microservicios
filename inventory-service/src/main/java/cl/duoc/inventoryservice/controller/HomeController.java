package cl.duoc.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "API Inventory Service", description = "Documentación del inventario")
@RestController
public class HomeController {

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "servicio", "inventory-service",
                "estado", "OK",
                "endpoint_inventario", "/api/inventario"
        );
    }
}
