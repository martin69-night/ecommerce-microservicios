package cl.duoc.paymentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "servicio", "payment-service",
                "estado", "OK",
                "endpoint_pagos", "/api/pagos"
        );
    }
}
