package cl.duoc.inventoryservice.controller;

import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Inventario> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity.ok(inventarioService.buscarPorSku(sku));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarPorProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(inventario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.actualizar(id, inventario));
    }

    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Inventario> reservarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.reservarStock(id, cantidad));
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Inventario> liberarReserva(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.liberarReserva(id, cantidad));
    }

    @PatchMapping("/{id}/descontar")
    public ResponseEntity<Inventario> descontarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.descontarStock(id, cantidad));
    }

    @PatchMapping("/{id}/reponer")
    public ResponseEntity<Inventario> reponerStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.reponerStock(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        inventarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
