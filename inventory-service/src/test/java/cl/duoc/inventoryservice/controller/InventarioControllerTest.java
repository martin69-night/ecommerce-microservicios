package cl.duoc.inventoryservice.controller;

import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.service.InventarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioController - Capa Controlador")
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private Inventario inventario() {
        return new Inventario(
                1L,
                10L,
                "SKU-001",
                100,
                20,
                "Bodega A",
                true
        );
    }

    @Test
    void listarActivosDebeRetornarOk() {
        when(inventarioService.listarActivos()).thenReturn(List.of(inventario()));

        ResponseEntity<List<Inventario>> respuesta = inventarioController.listarActivos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(inventarioService).listarActivos();
    }

    @Test
    void listarTodosDebeRetornarOk() {
        when(inventarioService.listarTodos()).thenReturn(List.of(inventario()));

        ResponseEntity<List<Inventario>> respuesta = inventarioController.listarTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(inventarioService).listarTodos();
    }

    @Test
    void buscarPorIdDebeRetornarOk() {
        when(inventarioService.buscarPorId(1L)).thenReturn(inventario());

        ResponseEntity<Inventario> respuesta = inventarioController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1L, respuesta.getBody().getId());
        verify(inventarioService).buscarPorId(1L);
    }

    @Test
    void buscarPorSkuDebeRetornarOk() {
        when(inventarioService.buscarPorSku("SKU-001")).thenReturn(inventario());

        ResponseEntity<Inventario> respuesta = inventarioController.buscarPorSku("SKU-001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("SKU-001", respuesta.getBody().getSku());
        verify(inventarioService).buscarPorSku("SKU-001");
    }

    @Test
    void buscarPorProductoIdDebeRetornarOk() {
        when(inventarioService.buscarPorProductoId(10L)).thenReturn(inventario());

        ResponseEntity<Inventario> respuesta = inventarioController.buscarPorProductoId(10L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(10L, respuesta.getBody().getProductoId());
        verify(inventarioService).buscarPorProductoId(10L);
    }

    @Test
    void crearDebeRetornarCreated() {
        Inventario inventario = inventario();
        when(inventarioService.crear(inventario)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.crear(inventario);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(inventario, respuesta.getBody());
        verify(inventarioService).crear(inventario);
    }

    @Test
    void actualizarDebeRetornarOk() {
        Inventario inventario = inventario();
        when(inventarioService.actualizar(1L, inventario)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.actualizar(1L, inventario);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(inventario, respuesta.getBody());
        verify(inventarioService).actualizar(1L, inventario);
    }

    @Test
    void reservarStockDebeRetornarOk() {
        Inventario inventario = inventario();
        when(inventarioService.reservarStock(1L, 5)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.reservarStock(1L, 5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(inventarioService).reservarStock(1L, 5);
    }

    @Test
    void liberarReservaDebeRetornarOk() {
        Inventario inventario = inventario();
        when(inventarioService.liberarReserva(1L, 5)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.liberarReserva(1L, 5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(inventarioService).liberarReserva(1L, 5);
    }

    @Test
    void descontarStockDebeRetornarOk() {
        Inventario inventario = inventario();
        when(inventarioService.descontarStock(1L, 5)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.descontarStock(1L, 5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(inventarioService).descontarStock(1L, 5);
    }

    @Test
    void reponerStockDebeRetornarOk() {
        Inventario inventario = inventario();
        when(inventarioService.reponerStock(1L, 5)).thenReturn(inventario);

        ResponseEntity<Inventario> respuesta = inventarioController.reponerStock(1L, 5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(inventarioService).reponerStock(1L, 5);
    }

    @Test
    void desactivarDebeRetornarNoContent() {
        doNothing().when(inventarioService).desactivar(1L);

        ResponseEntity<Void> respuesta = inventarioController.desactivar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(inventarioService).desactivar(1L);
    }
}
