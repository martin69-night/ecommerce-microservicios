package cl.duoc.cartservice.exception;

import cl.duoc.cartservice.controller.CarritoController;
import cl.duoc.cartservice.model.CarritoItem;
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

@DisplayName("GlobalExceptionHandler - Carrito")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarItemNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarItemNoEncontrado(
                        new CarritoItemNotFoundException("Item no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Item de carrito no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
    }

    @Test
    void manejarCarritoVacioDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarCarritoVacio(
                        new CarritoVacioException("Carrito sin items")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Carrito vacio", respuesta.getBody().get("error"));
        assertEquals("Carrito sin items", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarArgumentoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarArgumentoInvalido(
                        new IllegalArgumentException("Cantidad invalida")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Solicitud invalida", respuesta.getBody().get("error"));
        assertEquals("Cantidad invalida", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarErrorValidacionDebeRetornarCamposInvalidos() throws NoSuchMethodException {
        CarritoItem item = new CarritoItem();

        BeanPropertyBindingResult resultado =
                new BeanPropertyBindingResult(item, "carritoItem");

        resultado.addError(new FieldError(
                "carritoItem",
                "sku",
                "El SKU es obligatorio"
        ));

        MethodParameter parametro = new MethodParameter(
                CarritoController.class.getMethod("agregarItem", CarritoItem.class),
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

        assertEquals("El SKU es obligatorio", errores.get("sku"));
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
