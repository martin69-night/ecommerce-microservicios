package cl.duoc.inventoryservice.exception;

import cl.duoc.inventoryservice.controller.InventarioController;
import cl.duoc.inventoryservice.model.Inventario;
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
                        new SkuInventarioDuplicadoException("SKU ya existe")
                );

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("status"));
        assertEquals("SKU duplicado", respuesta.getBody().get("error"));
        assertEquals("SKU ya existe", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarStockInsuficienteDebeRetornar409() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarStockInsuficiente(
                        new StockInsuficienteException("Stock disponible insuficiente")
                );

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("status"));
        assertEquals("Stock insuficiente", respuesta.getBody().get("error"));
        assertEquals("Stock disponible insuficiente", respuesta.getBody().get("mensaje"));
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
        Inventario inventario = new Inventario();

        BeanPropertyBindingResult resultado =
                new BeanPropertyBindingResult(inventario, "inventario");

        resultado.addError(new FieldError(
                "inventario",
                "sku",
                "El SKU es obligatorio"
        ));

        MethodParameter parametro = new MethodParameter(
                InventarioController.class.getMethod("crear", Inventario.class),
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
