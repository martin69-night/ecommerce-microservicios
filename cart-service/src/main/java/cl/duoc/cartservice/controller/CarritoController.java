package cl.duoc.cartservice.controller;

import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.service.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<CarritoItem>> listarActivos() {
        return ResponseEntity.ok(carritoService.listarActivos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoItem> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CarritoItem> agregarItem(@RequestBody CarritoItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(item));
    }

    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<CarritoItem> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, Object>> calcularTotal(@PathVariable Long usuarioId) {
        BigDecimal total = carritoService.calcularTotal(usuarioId);

        return ResponseEntity.ok(Map.of(
                "usuarioId", usuarioId,
                "total", total
        ));
    }
}
