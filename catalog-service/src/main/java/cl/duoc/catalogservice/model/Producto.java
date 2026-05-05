package cl.duoc.catalogservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private Boolean activo = true;

    public Producto() {
    }

    public Producto(Long id, String sku, String nombre, String descripcion, BigDecimal precio, String categoria, Boolean activo) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
