package cl.duoc.catalogservice.exception;

import cl.duoc.catalogservice.controller.ProductoController;
import cl.duoc.catalogservice.model.Producto;
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

@DisplayName("GlobalExceptionHandler - Catálogo")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarProductoNoEncontradoDebeRetornar404() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarProductoNoEncontrado(
                        new ProductoNotFoundException("Producto no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(404, respuesta.getBody().get("status"));
        assertEquals("Producto no encontrado", respuesta.getBody().get("error"));
        assertNotNull(respuesta.getBody().get("timestamp"));
    }

    @Test
    void manejarSkuDuplicadoDebeRetornar409() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarSkuDuplicado(
                        new SkuDuplicadoException("SKU ya existe")
                );

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals(409, respuesta.getBody().get("status"));
        assertEquals("SKU duplicado", respuesta.getBody().get("error"));
        assertEquals("SKU ya existe", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarArgumentoInvalidoDebeRetornar400() {
        ResponseEntity<Map<String, Object>> respuesta =
                handler.manejarArgumentoInvalido(
                        new IllegalArgumentException("Precio invalido")
                );

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getBody().get("status"));
        assertEquals("Solicitud invalida", respuesta.getBody().get("error"));
        assertEquals("Precio invalido", respuesta.getBody().get("mensaje"));
    }

    @Test
    void manejarErrorValidacionDebeRetornarCamposInvalidos() throws NoSuchMethodException {
        Producto producto = new Producto();

        BeanPropertyBindingResult resultado =
                new BeanPropertyBindingResult(producto, "producto");

        resultado.addError(new FieldError(
                "producto",
                "sku",
                "El SKU es obligatorio"
        ));

        MethodParameter parametro = new MethodParameter(
                ProductoController.class.getMethod("crear", Producto.class),
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
