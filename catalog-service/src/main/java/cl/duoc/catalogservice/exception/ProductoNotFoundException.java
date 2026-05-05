package cl.duoc.catalogservice.exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
