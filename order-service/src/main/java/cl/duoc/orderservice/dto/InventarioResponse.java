package cl.duoc.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Inventario recibido desde inventory-service para validar y reservar stock")
public class InventarioResponse {

    @Schema(description = "Identificador del inventario", example = "1")
    private Long id;

    @Schema(description = "Identificador del producto", example = "100")
    private Long productoId;

    @Schema(description = "SKU del producto", example = "SKU-001")
    private String sku;

    @Schema(description = "Cantidad disponible", example = "20")
    private Integer cantidadDisponible;

    @Schema(description = "Cantidad reservada", example = "3")
    private Integer cantidadReservada;

    @Schema(description = "Stock disponible para nuevas reservas", example = "17")
    private Integer stockLibre;

    @Schema(description = "Ubicación física del inventario", example = "Bodega A")
    private String ubicacion;

    @Schema(description = "Indica si el inventario está activo", example = "true")
    private Boolean activo;

    public InventarioResponse() {
    }

    public InventarioResponse(
            Long id,
            Long productoId,
            String sku,
            Integer cantidadDisponible,
            Integer cantidadReservada,
            Integer stockLibre,
            String ubicacion,
            Boolean activo
    ) {
        this.id = id;
        this.productoId = productoId;
        this.sku = sku;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadReservada = cantidadReservada;
        this.stockLibre = stockLibre;
        this.ubicacion = ubicacion;
        this.activo = activo;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public void setStockLibre(Integer stockLibre) {
        this.stockLibre = stockLibre;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
