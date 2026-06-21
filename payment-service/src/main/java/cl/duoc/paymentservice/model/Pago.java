package cl.duoc.paymentservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long pedidoId;
    @Column(nullable = false)
    public Long usuarioId;

    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal monto;

    @Column(nullable = false, length = 40)
    public String metodoPago;

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
