package cl.duoc.paymentservice.exception;

import cl.duoc.paymentservice.controller.PagoController;
import cl.duoc.paymentservice.model.Pago;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler - Pago")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarPagoNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarPagoNoEncontrado(
                        new PagoNotFoundException("Pago no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Pago no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
    }

    @Test
    void manejarPagoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarPagoInvalido(
                        new PagoInvalidoException("Monto invalido")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Pago invalido", respuesta.getBody().get("error"));
        assertEquals("Monto invalido", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarArgumentoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarArgumentoInvalido(
                        new IllegalArgumentException("Estado invalido")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Solicitud invalida", respuesta.getBody().get("error"));
        assertEquals("Estado invalido", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarErrorValidacionDebeRetornarCamposInvalidos() throws NoSuchMethodException {
        Pago pago = new Pago();

        BeanPropertyBindingResult resultado = new BeanPropertyBindingResult(pago, "pago");
        resultado.addError(new FieldError(
                "pago",
                "monto",
                "El monto debe ser mayor a cero"
        ));

        MethodParameter parametro = new MethodParameter(
                PagoController.class.getMethod("crear", Pago.class),
                0
        );

        MethodArgumentNotValidException excepcion =
                new MethodArgumentNotValidException(parametro, resultado);

        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarErrorValidacion(excepcion);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Error de validacion", respuesta.getBody().get("error"));

        @SuppressWarnings("unchecked")
        Map<String, String> errores =
                (Map<String, String>) respuesta.getBody().get("errores");

        assertEquals("El monto debe ser mayor a cero", errores.get("monto"));
    }

    @Test
    void manejarErrorInesperadoDebeRetornar500() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarErrorInesperado(
                        new RuntimeException("Error controlado de prueba")
                );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals(500, respuesta.getBody().get("status"));
        assertEquals("Error interno", respuesta.getBody().get("error"));
        assertEquals("Ocurrio un error inesperado", respuesta.getBody().get("mensaje"));
    }
}
