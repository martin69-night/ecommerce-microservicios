package cl.duoc.notificacionservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class NotificacionRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El canal es obligatorio")
    private String canal;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 3, max = 255, message = "El mensaje debe tener entre 3 y 255 caracteres")
    private String mensaje;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;



    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }


    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
