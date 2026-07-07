package cl.duoc.cartservice.config;

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
                        .title("Cart Service API")
                        .version("1.0.0")
                        .description("""
                                API responsable de administrar los ítems del carrito
                                de compras del e-commerce.

                                Flujo principal:
                                1. El usuario agrega un SKU y una cantidad al carrito.
                                2. Cart Service consulta Catalog Service para validar y completar
                                   los datos del producto.
                                3. El carrito permite modificar cantidades, calcular totales
                                   y eliminar ítems.
                                """)
                        .contact(new Contact()
                                .name("Equipo E-commerce Duoc UC"))
                        .license(new License()
                                .name("Uso académico")));
    }
}
