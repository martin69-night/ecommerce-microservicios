package cl.duoc.usuarioservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class UsuarioResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;

    @Schema(description = "Campo nombre", example = "valor")
    private String nombre;

    @Schema(description = "Campo email", example = "valor")
    private String email;

    @Schema(description = "Campo rol", example = "valor")
    private String rol;

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


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
