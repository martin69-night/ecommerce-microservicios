package cl.duoc.notificacionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-client", url = "${usuario.service.url}")
public interface UsuarioClient {
    @GetMapping("/api/usuarios/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
