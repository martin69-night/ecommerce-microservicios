package cl.duoc.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a cero")
    @Column(nullable = false)
    private Long productoId;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50, message = "El SKU no puede superar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 120, message = "El nombre del producto no puede superar 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombreProducto;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    @Column(nullable = false, length = 30)
    private String estado;

    private LocalDateTime fechaCreacion;

    private Boolean activo = true;

    public Pedido() {
    }

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = "CREADO";
        }

        if (activo == null) {
            activo = true;
        }

        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }

        calcularTotal();
    }

    public void calcularTotal() {
        if (precioUnitario != null && cantidad != null) {
            total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getSku() {
        return sku;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
