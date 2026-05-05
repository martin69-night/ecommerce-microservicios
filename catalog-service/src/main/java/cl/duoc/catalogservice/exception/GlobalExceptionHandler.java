package cl.duoc.catalogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarProductoNoEncontrado(ProductoNotFoundException ex) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.NOT_FOUND.value());
        respuesta.put("error", "Producto no encontrado");
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(SkuDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> manejarSkuDuplicado(SkuDuplicadoException ex) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.CONFLICT.value());
        respuesta.put("error", "SKU duplicado");
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(IllegalArgumentException ex) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Solicitud invalida");
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}
