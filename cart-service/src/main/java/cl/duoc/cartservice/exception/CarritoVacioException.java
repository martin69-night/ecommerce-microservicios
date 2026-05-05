package cl.duoc.cartservice.exception;

public class CarritoVacioException extends RuntimeException {

    public CarritoVacioException(String mensaje) {
        super(mensaje);
    }
}
