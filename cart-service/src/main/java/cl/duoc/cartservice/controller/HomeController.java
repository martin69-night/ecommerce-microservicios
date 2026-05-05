package cl.duoc.cartservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "servicio", "cart-service",
                "estado", "OK",
                "endpoint_carrito", "/api/carrito"
        );
    }
}
