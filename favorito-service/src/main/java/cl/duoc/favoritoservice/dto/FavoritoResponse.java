package cl.duoc.favoritoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class FavoritoResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;

    @Schema(description = "Campo usuarioId", example = "valor")
    private Long usuarioId;

    @Schema(description = "Campo productoId", example = "valor")
    private Long productoId;

    @Schema(description = "Campo sku", example = "valor")
    private String sku;

    @Schema(description = "Campo fechaCreacion", example = "valor")
    private LocalDateTime fechaCreacion;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
