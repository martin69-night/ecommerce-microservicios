package cl.duoc.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "inventarios")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    private Long productoId;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50, message = "El SKU no puede superar 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadDisponible;

    @PositiveOrZero(message = "La cantidad reservada no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadReservada;

    @Size(max = 120, message = "La ubicacion no puede superar 120 caracteres")
    private String ubicacion;

    private Boolean activo = true;

    public Inventario() {
    }

    public Inventario(
            Long id,
            Long productoId,
            String sku,
            Integer cantidadDisponible,
            Integer cantidadReservada,
            String ubicacion,
            Boolean activo
    ) {
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
