package cl.duoc.paymentservice.controller;

import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listarActivos() {
        return ResponseEntity.ok(pagoService.listarActivos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.listarPorEstado(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> buscarPorPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.buscarPorPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(pago));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<Pago> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.aprobar(id));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.rechazar(id));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Pago> anular(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.anular(id));
    }
}
