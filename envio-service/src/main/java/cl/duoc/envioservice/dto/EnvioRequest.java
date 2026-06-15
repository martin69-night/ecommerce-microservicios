package cl.duoc.envioservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class EnvioRequest {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio")
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
