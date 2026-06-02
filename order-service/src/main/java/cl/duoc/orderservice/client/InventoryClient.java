package cl.duoc.orderservice.client;

import cl.duoc.orderservice.config.FeignOkHttpConfig;
import cl.duoc.orderservice.dto.InventarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "inventory-client",
        url = "${inventory.service.url}",
        configuration = FeignOkHttpConfig.class
)
public interface InventoryClient {

    @GetMapping("/api/inventario/sku/{sku}")
    InventarioResponse buscarPorSku(@PathVariable("sku") String sku);

    @PatchMapping("/api/inventario/{id}/reservar")
    InventarioResponse reservarStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}
