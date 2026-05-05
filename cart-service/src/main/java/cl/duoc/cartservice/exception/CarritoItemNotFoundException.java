package cl.duoc.cartservice.exception;

public class CarritoItemNotFoundException extends RuntimeException {

    public CarritoItemNotFoundException(String mensaje) {
        super(mensaje);
    }
}
