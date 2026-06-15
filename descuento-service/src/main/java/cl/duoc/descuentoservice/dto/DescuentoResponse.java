package cl.duoc.descuentoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class DescuentoResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;

    @Schema(description = "Campo codigo", example = "valor")
    private String codigo;

    @Schema(description = "Campo porcentaje", example = "valor")
    private BigDecimal porcentaje;

    @Schema(description = "Campo activo", example = "valor")
    private Boolean activo;

    @Schema(description = "Campo fechaCreacion", example = "valor")
    private LocalDateTime fechaCreacion;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }


    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
