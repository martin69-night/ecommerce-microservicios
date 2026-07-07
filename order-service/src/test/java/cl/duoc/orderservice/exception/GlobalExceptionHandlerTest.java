package cl.duoc.orderservice.exception;

import cl.duoc.orderservice.controller.PedidoController;
import cl.duoc.orderservice.model.Pedido;
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

@DisplayName("GlobalExceptionHandler - Pedido")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarPedidoNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarPedidoNoEncontrado(
                        new PedidoNotFoundException("Pedido no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Pedido no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
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
        Pedido pedido = new Pedido();

        BeanPropertyBindingResult resultado =
                new BeanPropertyBindingResult(pedido, "pedido");

        resultado.addError(new FieldError(
                "pedido",
                "sku",
                "El SKU es obligatorio"
        ));

        MethodParameter parametro = new MethodParameter(
                PedidoController.class.getMethod("crear", Pedido.class),
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
