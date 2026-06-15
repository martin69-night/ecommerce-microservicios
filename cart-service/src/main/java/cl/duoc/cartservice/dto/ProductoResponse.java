package cl.duoc.cartservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class ProductoResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;
    private String sku;
    @Schema(description = "Campo nombre", example = "valor")
    private String nombre;
    private String descripcion;
    @Schema(description = "Campo precio", example = "valor")
    private BigDecimal precio;
    private String categoria;
    @Schema(description = "Campo activo", example = "valor")
    private Boolean activo;

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
}
