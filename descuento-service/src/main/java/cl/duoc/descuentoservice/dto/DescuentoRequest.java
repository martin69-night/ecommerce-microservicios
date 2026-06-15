package cl.duoc.descuentoservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DescuentoRequest {

    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a cero")
    @DecimalMax(value = "100.00", message = "El porcentaje no puede superar 100")
    private BigDecimal porcentaje;

    @NotNull(message = "El estado activo es obligatorio")
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
