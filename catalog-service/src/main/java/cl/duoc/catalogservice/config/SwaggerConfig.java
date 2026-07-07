package cl.duoc.catalogservice.config;

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
                        .title("Catalog Service API")
                        .version("1.0.0")
                        .description("""
                                API responsable de administrar el catálogo de productos
                                del e-commerce.

                                Operaciones principales:
                                1. Crear, consultar y actualizar productos.
                                2. Buscar productos por SKU, categoría o nombre.
                                3. Desactivar, reactivar o eliminar productos.
                                """)
                        .contact(new Contact()
                                .name("Equipo E-commerce Duoc UC"))
                        .license(new License()
                                .name("Uso académico")));
    }
}
