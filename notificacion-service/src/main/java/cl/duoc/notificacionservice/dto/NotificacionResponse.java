package cl.duoc.notificacionservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class NotificacionResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;

    @Schema(description = "Campo usuarioId", example = "valor")
    private Long usuarioId;

    @Schema(description = "Campo canal", example = "valor")
    private String canal;

    @Schema(description = "Campo mensaje", example = "valor")
    private String mensaje;

    @Schema(description = "Campo estado", example = "valor")
    private String estado;

    @Schema(description = "Campo fechaCreacion", example = "valor")
    private LocalDateTime fechaCreacion;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
