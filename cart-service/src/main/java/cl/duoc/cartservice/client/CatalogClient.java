package cl.duoc.cartservice.client;

import cl.duoc.cartservice.dto.ProductoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-client", url = "${catalog.service.url}")
public interface CatalogClient {

    @GetMapping("/api/productos/sku/{sku}")
    ProductoResponse buscarPorSku(@PathVariable("sku") String sku);
}
