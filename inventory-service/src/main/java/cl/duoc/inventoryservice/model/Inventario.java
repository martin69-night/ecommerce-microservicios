package cl.duoc.inventoryservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventarios")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    private Integer cantidadDisponible;

    private Integer cantidadReservada;

    private String ubicacion;

    private Boolean activo = true;

    public Inventario() {
    }

    public Inventario(Long id, Long productoId, String sku, Integer cantidadDisponible, Integer cantidadReservada, String ubicacion, Boolean activo) {
        this.id = id;
        this.productoId = productoId;
        this.sku = sku;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadReservada = cantidadReservada;
        this.ubicacion = ubicacion;
        this.activo = activo;
    }

    @PrePersist
    public void prePersist() {
        if (cantidadDisponible == null) {
            cantidadDisponible = 0;
        }

        if (cantidadReservada == null) {
            cantidadReservada = 0;
        }

        if (activo == null) {
            activo = true;
        }
    }

    public Integer getStockLibre() {
        int disponible = cantidadDisponible == null ? 0 : cantidadDisponible;
        int reservada = cantidadReservada == null ? 0 : cantidadReservada;
        return disponible - reservada;
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

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
