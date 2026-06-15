package cl.duoc.envioservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class EnvioRequest {

    @NotNull(message = "El pedidoId es obligatorio")
    @Schema(description = "Campo pedidoId", example = "valor")
    private Long pedidoId;

    @NotBlank(message = "La direccion es obligatoria")
    @Schema(description = "Campo direccion", example = "valor")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Campo ciudad", example = "valor")
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Campo estado", example = "valor")
    private String estado;



    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
