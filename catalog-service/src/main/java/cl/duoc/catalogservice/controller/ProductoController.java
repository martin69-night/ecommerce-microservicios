package cl.duoc.catalogservice.controller;

import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity.ok(productoService.buscarPorSku(sku));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Producto> reactivar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.reactivar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFisicamente(@PathVariable Long id) {
        productoService.eliminarFisicamente(id);
        return ResponseEntity.noContent().build();
    }
}
