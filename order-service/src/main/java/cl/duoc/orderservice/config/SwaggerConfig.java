package cl.duoc.orderservice.config;

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
                        .title("Order Service API")
                        .version("1.0.0")
                        .description("""
                                API responsable de registrar y administrar pedidos
                                del e-commerce.

                                Flujo principal:
                                1. Se recibe la información del pedido.
                                2. Order Service consulta Inventory Service por SKU.
                                3. Se valida el stock libre y se reserva la cantidad solicitada.
                                4. El pedido se registra inicialmente con estado CREADO.
                                """)
                        .contact(new Contact()
                                .name("Equipo E-commerce Duoc UC"))
                        .license(new License()
                                .name("Uso académico")));
    }
}
