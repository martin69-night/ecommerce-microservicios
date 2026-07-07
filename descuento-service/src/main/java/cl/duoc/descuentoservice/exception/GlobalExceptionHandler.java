package cl.duoc.descuentoservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        logger.warn("evento=recurso_no_encontrado mensaje={}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                respuestaBase(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        logger.warn(
                "evento=error_validacion cantidadErrores={} campos={}",
                errores.size(),
                errores.keySet()
        );

        Map<String, Object> respuesta = respuestaBase(
                HttpStatus.BAD_REQUEST, "Error de validacion",
                "La solicitud contiene campos invalidos"
        );
        respuesta.put("errores", errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumento(IllegalArgumentException ex) {
        logger.warn("evento=argumento_invalido mensaje={}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                respuestaBase(HttpStatus.BAD_REQUEST, "Solicitud invalida", ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorInesperado(Exception ex) {
        logger.error(
                "evento=error_interno tipo={} mensaje={}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                respuestaBase(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error interno",
                        "Ocurrio un error inesperado"
                )
        );
    }

    private Map<String, Object> respuestaBase(HttpStatus estado, String error, String mensaje) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", estado.value());
        respuesta.put("error", error);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
