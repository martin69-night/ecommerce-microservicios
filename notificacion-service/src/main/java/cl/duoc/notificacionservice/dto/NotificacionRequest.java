package cl.duoc.notificacionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class NotificacionRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    @Schema(description = "Campo usuarioId", example = "valor")
    private Long usuarioId;

    @NotBlank(message = "El canal es obligatorio")
    @Schema(description = "Campo canal", example = "valor")
    private String canal;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 3, max = 255, message = "El mensaje debe tener entre 3 y 255 caracteres")
    @Schema(description = "Campo mensaje", example = "valor")
    private String mensaje;

    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Campo estado", example = "valor")
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
