package cl.duoc.paymentservice.exception;

public class PagoNotFoundException extends RuntimeException {

    public PagoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
