package cl.duoc.catalogservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "servicio", "catalog-service",
                "estado", "OK",
                "endpoint_productos", "/api/productos"
        );
    }
}
