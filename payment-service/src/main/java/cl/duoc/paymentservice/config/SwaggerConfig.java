package cl.duoc.paymentservice.config;

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
                        .title("Payment Service API")
                        .version("1.0.0")
                        .description("""
                                API responsable de registrar y administrar pagos
                                asociados a los pedidos del e-commerce.

                                Flujo principal:
                                1. Se crea un pago en estado PENDIENTE.
                                2. El pago puede aprobarse, rechazarse o anularse.
                                3. Las solicitudes inválidas devuelven errores estructurados.
                                """)
                        .contact(new Contact()
                                .name("Equipo E-commerce Duoc UC"))
                        .license(new License()
                                .name("Uso académico")));
    }
}
