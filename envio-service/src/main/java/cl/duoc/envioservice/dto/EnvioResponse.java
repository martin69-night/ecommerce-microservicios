package cl.duoc.envioservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Schema(description = "Modelo de datos usado por la API")
public class EnvioResponse {

    @Schema(description = "Campo id", example = "valor")
    private Long id;

    @Schema(description = "Campo pedidoId", example = "valor")
    private Long pedidoId;

    @Schema(description = "Campo direccion", example = "valor")
    private String direccion;

    @Schema(description = "Campo ciudad", example = "valor")
    private String ciudad;

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


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
