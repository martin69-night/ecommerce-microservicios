package cl.duoc.descuentoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class DescuentoRequest {

    @NotBlank(message = "El codigo es obligatorio")
    @Schema(description = "Campo codigo", example = "valor")
    private String codigo;

    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a cero")
    @DecimalMax(value = "100.00", message = "El porcentaje no puede superar 100")
    @Schema(description = "Campo porcentaje", example = "valor")
    private BigDecimal porcentaje;

    @NotNull(message = "El estado activo es obligatorio")
    @Schema(description = "Campo activo", example = "valor")
    private Boolean activo;



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

}
