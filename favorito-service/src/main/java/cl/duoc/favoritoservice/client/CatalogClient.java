package cl.duoc.favoritoservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-client", url = "${catalog.service.url}")
public interface CatalogClient {
    @GetMapping("/api/productos/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
