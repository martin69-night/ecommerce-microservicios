package cl.duoc.orderservice.dto;

public class InventarioResponse {

    private Long id;
    private Long productoId;
    private String sku;
    private Integer cantidadDisponible;
    private Integer cantidadReservada;
    private Integer stockLibre;
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
