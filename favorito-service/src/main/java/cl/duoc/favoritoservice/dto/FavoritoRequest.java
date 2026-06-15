package cl.duoc.favoritoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class FavoritoRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    @Schema(description = "Campo usuarioId", example = "valor")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Schema(description = "Campo productoId", example = "valor")
    private Long productoId;

    @NotBlank(message = "El sku es obligatorio")
    @Schema(description = "Campo sku", example = "valor")
    private String sku;



    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }


    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
