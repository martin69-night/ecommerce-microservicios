package cl.duoc.paymentservice.exception;

public class PagoInvalidoException extends RuntimeException {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
