package cl.duoc.inventoryservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .version("1.0.0")
                        .description("""
                                API responsable de administrar el inventario
                                disponible para los productos del e-commerce.

                                Operaciones principales:
                                1. Crear y consultar registros de inventario.
                                2. Reservar, liberar, descontar y reponer stock.
                                3. Controlar stock disponible para el flujo de pedidos.
                                """)
                        .contact(new Contact()
                                .name("Equipo E-commerce Duoc UC"))
                        .license(new License()
                                .name("Uso académico")));
    }
}
