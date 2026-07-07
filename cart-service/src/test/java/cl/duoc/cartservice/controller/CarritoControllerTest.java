package cl.duoc.cartservice.controller;

import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.service.CarritoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoController - Capa Controlador")
class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    private CarritoItem item() {
        CarritoItem item = new CarritoItem();
        item.setId(1L);
        item.setUsuarioId(10L);
        item.setProductoId(100L);
        item.setSku("SKU-001");
        item.setNombreProducto("Laptop");
        item.setPrecioUnitario(new BigDecimal("100.00"));
        item.setCantidad(2);
        item.setActivo(true);
        return item;
    }

    @Test
    void listarActivosDebeRetornarOk() {
        when(carritoService.listarActivos()).thenReturn(List.of(item()));

        ResponseEntity<List<CarritoItem>> respuesta = carritoController.listarActivos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(carritoService).listarActivos();
    }

    @Test
    void listarPorUsuarioDebeRetornarOk() {
        when(carritoService.listarPorUsuario(10L)).thenReturn(List.of(item()));

        ResponseEntity<List<CarritoItem>> respuesta = carritoController.listarPorUsuario(10L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(carritoService).listarPorUsuario(10L);
    }

    @Test
    void buscarPorIdDebeRetornarOk() {
        when(carritoService.buscarPorId(1L)).thenReturn(item());

        ResponseEntity<CarritoItem> respuesta = carritoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1L, respuesta.getBody().getId());
        verify(carritoService).buscarPorId(1L);
    }

    @Test
    void agregarItemDebeRetornarCreated() {
        CarritoItem item = item();
        when(carritoService.agregarItem(item)).thenReturn(item);

        ResponseEntity<CarritoItem> respuesta = carritoController.agregarItem(item);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(item, respuesta.getBody());
        verify(carritoService).agregarItem(item);
    }

    @Test
    void actualizarCantidadDebeRetornarOk() {
        CarritoItem item = item();
        item.setCantidad(5);
        when(carritoService.actualizarCantidad(1L, 5)).thenReturn(item);

        ResponseEntity<CarritoItem> respuesta = carritoController.actualizarCantidad(1L, 5);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(5, respuesta.getBody().getCantidad());
        verify(carritoService).actualizarCantidad(1L, 5);
    }

    @Test
    void eliminarItemDebeRetornarNoContent() {
        doNothing().when(carritoService).eliminarItem(1L);

        ResponseEntity<Void> respuesta = carritoController.eliminarItem(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(carritoService).eliminarItem(1L);
    }

    @Test
    void vaciarCarritoDebeRetornarNoContent() {
        doNothing().when(carritoService).vaciarCarrito(10L);

        ResponseEntity<Void> respuesta = carritoController.vaciarCarrito(10L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(carritoService).vaciarCarrito(10L);
    }

    @Test
    void calcularTotalDebeRetornarUsuarioYTotal() {
        when(carritoService.calcularTotal(10L)).thenReturn(new BigDecimal("250.00"));

        ResponseEntity<Map<String, Object>> respuesta = carritoController.calcularTotal(10L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(10L, respuesta.getBody().get("usuarioId"));
        assertEquals(new BigDecimal("250.00"), respuesta.getBody().get("total"));
        verify(carritoService).calcularTotal(10L);
    }
}
