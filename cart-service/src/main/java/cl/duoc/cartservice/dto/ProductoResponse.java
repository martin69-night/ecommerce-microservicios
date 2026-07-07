package cl.duoc.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Producto recibido desde catalog-service para agregarlo al carrito")
public class ProductoResponse {

    @Schema(description = "Identificador del producto", example = "1")
    private Long id;

    @Schema(description = "SKU del producto", example = "SKU-001")
    private String sku;

    @Schema(description = "Nombre del producto", example = "Laptop")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Notebook para trabajo")
    private String descripcion;

    @Schema(description = "Precio unitario del producto", example = "999.99")
    private BigDecimal precio;

    @Schema(description = "Categoría del producto", example = "Tecnologia")
    private String categoria;

    @Schema(description = "Indica si el producto está activo", example = "true")
    private Boolean activo;

    public ProductoResponse() {
    }

    public ProductoResponse(
            Long id,
            String sku,
            String nombre,
            String descripcion,
            BigDecimal precio,
            String categoria,
            Boolean activo
    ) {
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
