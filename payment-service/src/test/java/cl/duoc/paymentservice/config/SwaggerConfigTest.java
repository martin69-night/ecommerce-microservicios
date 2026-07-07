package cl.duoc.paymentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    @Test
    void customOpenAPIDebeConfigurarInformacionDelServicio() {
        OpenAPI openAPI = new SwaggerConfig().customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Payment Service API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getContact());
        assertNotNull(openAPI.getInfo().getLicense());
    }
}
