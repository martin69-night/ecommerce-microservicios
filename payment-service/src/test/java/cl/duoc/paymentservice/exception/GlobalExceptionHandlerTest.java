package cl.duoc.paymentservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler - Manejo de errores")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarPagoNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarPagoNoEncontrado(new PagoNotFoundException("Pago no encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Pago no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
    }

    @Test
    void manejarPagoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarPagoInvalido(new PagoInvalidoException("Estado inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Pago invalido", respuesta.getBody().get("error"));
        assertEquals("Estado inválido", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarArgumentoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarArgumentoInvalido(new IllegalArgumentException("Monto inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Solicitud invalida", respuesta.getBody().get("error"));
        assertEquals("Monto inválido", respuesta.getBody().get("mensaje"));
    }
}
