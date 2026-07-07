package cl.duoc.paymentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Positive(message = "El pedidoId debe ser mayor a cero")
    @Column(nullable = false)
    public Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a cero")
    @Column(nullable = false)
    public Long usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal monto;

    @NotBlank(message = "El metodoPago es obligatorio")
    @Size(max = 40, message = "El metodoPago no puede superar 40 caracteres")
    @Column(nullable = false, length = 40)
    public String metodoPago;

    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    @Column(nullable = false, length = 30)
    public String estado;

    public String codigoTransaccion;
    public LocalDateTime fechaCreacion;
    public Boolean activo = true;

    public Pago() {
    }

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = "PENDIENTE";
        }

        if (activo == null) {
            activo = true;
        }

        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
