package cl.duoc.inventoryservice.exception;

public class InventarioNotFoundException extends RuntimeException {

    public InventarioNotFoundException(String mensaje) {
        super(mensaje);
    }
}
