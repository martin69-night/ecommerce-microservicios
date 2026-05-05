package cl.duoc.catalogservice.exception;

public class SkuDuplicadoException extends RuntimeException {

    public SkuDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
