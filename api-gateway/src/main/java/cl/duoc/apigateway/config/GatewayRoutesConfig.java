package cl.duoc.apigateway.config;

import java.net.URI;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("catalog-service", r -> r
                        .path("/api/productos/**")
                        .uri(URI.create("http://api_catalogo:8083")))
                .route("inventory-service", r -> r
                        .path("/api/inventario/**")
                        .uri(URI.create("http://api_inventario:8084")))
                .route("usuario-service", r -> r
                        .path("/api/usuarios/**")
                        .uri(URI.create("http://api_usuarios:8086")))
                .build();
    }
}
