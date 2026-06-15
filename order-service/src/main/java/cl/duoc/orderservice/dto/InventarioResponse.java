package cl.duoc.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de datos usado por la API")
public class InventarioResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;
    private Long productoId;
    @Schema(description = "Campo sku", example = "valor")
    private String sku;
    private Integer cantidadDisponible;
    @Schema(description = "Campo cantidadReservada", example = "valor")
    private Integer cantidadReservada;
    private Integer stockLibre;
    @Schema(description = "Campo ubicacion", example = "valor")
    private String ubicacion;
    private Boolean activo;

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getSku() {
        return sku;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    public Integer getStockLibre() {
        return stockLibre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Boolean getActivo() {
        return activo;
    }
}
