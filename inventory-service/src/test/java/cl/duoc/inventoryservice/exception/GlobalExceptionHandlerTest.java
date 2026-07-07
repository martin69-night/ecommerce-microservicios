package cl.duoc.inventoryservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler - Inventario")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarInventarioNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarInventarioNoEncontrado(
                        new InventarioNotFoundException("Inventario no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Inventario no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
    }

    @Test
    void manejarSkuDuplicadoDebeRetornar409() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarSkuDuplicado(
                        new SkuInventarioDuplicadoException("SKU duplicado")
                );

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("status"));
        assertEquals("SKU duplicado", respuesta.getBody().get("error"));
        assertEquals("SKU duplicado", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarStockInsuficienteDebeRetornar409() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarStockInsuficiente(
                        new StockInsuficienteException("Stock insuficiente")
                );

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("status"));
        assertEquals("Stock insuficiente", respuesta.getBody().get("error"));
        assertEquals("Stock insuficiente", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarArgumentoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarArgumentoInvalido(
                        new IllegalArgumentException("Cantidad inválida")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Solicitud invalida", respuesta.getBody().get("error"));
        assertEquals("Cantidad inválida", respuesta.getBody().get("mensaje"));
    }
}
