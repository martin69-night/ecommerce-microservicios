package cl.duoc.catalogservice.controller;

import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.service.ProductoService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoController - Capa Controlador")
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto() {
        return new Producto(
                1L,
                "SKU-001",
                "Laptop",
                "Notebook para trabajo",
                new BigDecimal("999.99"),
                "Tecnologia",
                true
        );
    }

    @Test
    void listarActivosDebeRetornarOk() {
        when(productoService.listarActivos()).thenReturn(List.of(producto()));

        ResponseEntity<List<Producto>> respuesta = productoController.listarActivos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(productoService).listarActivos();
    }

    @Test
    void listarTodosDebeRetornarOk() {
        when(productoService.listarTodos()).thenReturn(List.of(producto()));

        ResponseEntity<List<Producto>> respuesta = productoController.listarTodos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(productoService).listarTodos();
    }

    @Test
    void buscarPorIdDebeRetornarOk() {
        when(productoService.buscarPorId(1L)).thenReturn(producto());

        ResponseEntity<Producto> respuesta = productoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1L, respuesta.getBody().getId());
        verify(productoService).buscarPorId(1L);
    }

    @Test
    void buscarPorSkuDebeRetornarOk() {
        when(productoService.buscarPorSku("SKU-001")).thenReturn(producto());

        ResponseEntity<Producto> respuesta = productoController.buscarPorSku("SKU-001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("SKU-001", respuesta.getBody().getSku());
        verify(productoService).buscarPorSku("SKU-001");
    }

    @Test
    void listarPorCategoriaDebeRetornarOk() {
        when(productoService.listarPorCategoria("Tecnologia"))
                .thenReturn(List.of(producto()));

        ResponseEntity<List<Producto>> respuesta =
                productoController.listarPorCategoria("Tecnologia");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(productoService).listarPorCategoria("Tecnologia");
    }

    @Test
    void buscarPorNombreDebeRetornarOk() {
        when(productoService.buscarPorNombre("lap"))
                .thenReturn(List.of(producto()));

        ResponseEntity<List<Producto>> respuesta =
                productoController.buscarPorNombre("lap");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(productoService).buscarPorNombre("lap");
    }

    @Test
    void crearDebeRetornarCreated() {
        Producto producto = producto();
        when(productoService.crear(producto)).thenReturn(producto);

        ResponseEntity<Producto> respuesta = productoController.crear(producto);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(producto, respuesta.getBody());
        verify(productoService).crear(producto);
    }

    @Test
    void actualizarDebeRetornarOk() {
        Producto producto = producto();
        when(productoService.actualizar(1L, producto)).thenReturn(producto);

        ResponseEntity<Producto> respuesta =
                productoController.actualizar(1L, producto);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertSame(producto, respuesta.getBody());
        verify(productoService).actualizar(1L, producto);
    }

    @Test
    void desactivarDebeRetornarNoContent() {
        doNothing().when(productoService).desactivar(1L);

        ResponseEntity<Void> respuesta = productoController.desactivar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(productoService).desactivar(1L);
    }

    @Test
    void reactivarDebeRetornarOk() {
        Producto producto = producto();
        producto.setActivo(true);
        when(productoService.reactivar(1L)).thenReturn(producto);

        ResponseEntity<Producto> respuesta = productoController.reactivar(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().getActivo());
        verify(productoService).reactivar(1L);
    }

    @Test
    void eliminarFisicamenteDebeRetornarNoContent() {
        doNothing().when(productoService).eliminarFisicamente(1L);

        ResponseEntity<Void> respuesta =
                productoController.eliminarFisicamente(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
        verify(productoService).eliminarFisicamente(1L);
    }
}
