package cl.duoc.catalogservice.service;

import cl.duoc.catalogservice.exception.ProductoNotFoundException;
import cl.duoc.catalogservice.exception.SkuDuplicadoException;
import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService - Capa Servicio")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoValido() {
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
    @DisplayName("listarActivos debe retornar productos activos")
    void listarActivosDebeRetornarLista() {
        Producto producto = productoValido();
        when(productoRepository.findByActivoTrue()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
        verify(productoRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("listarTodos debe retornar todos los productos")
    void listarTodosDebeRetornarLista() {
        Producto producto = productoValido();
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarTodos();

        assertEquals(1, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId debe retornar producto cuando existe")
    void buscarPorIdDebeRetornarProductoCuandoExiste() {
        Producto producto = productoValido();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(productoRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        ProductoNotFoundException exception = assertThrows(
                ProductoNotFoundException.class,
                () -> productoService.buscarPorId(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(productoRepository).findById(99L);
    }

    @Test
    @DisplayName("buscarPorSku debe retornar producto cuando existe")
    void buscarPorSkuDebeRetornarProductoCuandoExiste() {
        Producto producto = productoValido();
        when(productoRepository.findBySku("SKU-001")).thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorSku("SKU-001");

        assertEquals("SKU-001", resultado.getSku());
        verify(productoRepository).findBySku("SKU-001");
    }

    @Test
    @DisplayName("buscarPorSku debe lanzar excepción cuando no existe")
    void buscarPorSkuDebeLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.findBySku("SKU-404")).thenReturn(Optional.empty());

        ProductoNotFoundException exception = assertThrows(
                ProductoNotFoundException.class,
                () -> productoService.buscarPorSku("SKU-404")
        );

        assertTrue(exception.getMessage().contains("SKU-404"));
        verify(productoRepository).findBySku("SKU-404");
    }

    @Test
    @DisplayName("listarPorCategoria debe retornar productos activos de la categoría")
    void listarPorCategoriaDebeRetornarLista() {
        Producto producto = productoValido();
        when(productoRepository.findByCategoriaAndActivoTrue("Tecnologia"))
                .thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarPorCategoria("Tecnologia");

        assertEquals(1, resultado.size());
        assertEquals("Tecnologia", resultado.get(0).getCategoria());
        verify(productoRepository).findByCategoriaAndActivoTrue("Tecnologia");
    }

    @Test
    @DisplayName("buscarPorNombre debe retornar productos activos coincidentes")
    void buscarPorNombreDebeRetornarLista() {
        Producto producto = productoValido();
        when(productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue("lap"))
                .thenReturn(List.of(producto));

        List<Producto> resultado = productoService.buscarPorNombre("lap");

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
        verify(productoRepository).findByNombreContainingIgnoreCaseAndActivoTrue("lap");
    }

    @Test
    @DisplayName("crear debe lanzar excepción cuando SKU ya existe")
    void crearDebeLanzarExcepcionSiSkuYaExiste() {
        Producto producto = productoValido();
        when(productoRepository.existsBySku("SKU-001")).thenReturn(true);

        SkuDuplicadoException exception = assertThrows(
                SkuDuplicadoException.class,
                () -> productoService.crear(producto)
        );

        assertTrue(exception.getMessage().contains("SKU-001"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe inicializar activo y guardar producto")
    void crearDebeInicializarActivoYGuardarProducto() {
        Producto producto = new Producto(
                88L,
                "SKU-NUEVO",
                "Mouse",
                "Mouse inalámbrico",
                new BigDecimal("29.99"),
                "Accesorios",
                null
        );

        when(productoRepository.existsBySku("SKU-NUEVO")).thenReturn(false);
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.crear(producto);

        assertNull(resultado.getId());
        assertTrue(resultado.getActivo());
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("actualizar debe modificar producto manteniendo el mismo SKU")
    void actualizarDebeModificarProductoConMismoSku() {
        Producto existente = productoValido();
        Producto datos = new Producto(
                null,
                "SKU-001",
                "Laptop Pro",
                "Descripción actualizada",
                new BigDecimal("1200.00"),
                "Computacion",
                true
        );

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.actualizar(1L, datos);

        assertEquals("Laptop Pro", resultado.getNombre());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals(new BigDecimal("1200.00"), resultado.getPrecio());
        assertEquals("Computacion", resultado.getCategoria());
        verify(productoRepository, never()).existsBySku(anyString());
        verify(productoRepository).save(existente);
    }

    @Test
    @DisplayName("actualizar debe modificar producto cuando nuevo SKU está disponible")
    void actualizarDebeModificarProductoConNuevoSkuDisponible() {
        Producto existente = productoValido();
        Producto datos = new Producto(
                null,
                "SKU-002",
                "Laptop Nueva",
                "Nueva descripción",
                new BigDecimal("1100.00"),
                "Tecnologia",
                true
        );

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.existsBySku("SKU-002")).thenReturn(false);
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.actualizar(1L, datos);

        assertEquals("SKU-002", resultado.getSku());
        assertEquals("Laptop Nueva", resultado.getNombre());
        verify(productoRepository).existsBySku("SKU-002");
        verify(productoRepository).save(existente);
    }

    @Test
    @DisplayName("actualizar debe lanzar excepción si nuevo SKU pertenece a otro producto")
    void actualizarDebeLanzarExcepcionSiSkuNuevoYaExiste() {
        Producto existente = productoValido();
        Producto datos = new Producto(
                null,
                "SKU-DUPLICADO",
                "Producto duplicado",
                "Descripción",
                new BigDecimal("10.00"),
                "Otro",
                true
        );

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.existsBySku("SKU-DUPLICADO")).thenReturn(true);

        assertThrows(
                SkuDuplicadoException.class,
                () -> productoService.actualizar(1L, datos)
        );

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("desactivar debe dejar producto inactivo")
    void desactivarDebeDejarProductoInactivo() {
        Producto producto = productoValido();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.desactivar(1L);

        assertFalse(producto.getActivo());
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("reactivar debe dejar producto activo")
    void reactivarDebeDejarProductoActivo() {
        Producto producto = productoValido();
        producto.setActivo(false);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.reactivar(1L);

        assertTrue(resultado.getActivo());
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("eliminarFisicamente debe eliminar producto existente")
    void eliminarFisicamenteDebeEliminarProductoExistente() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminarFisicamente(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarFisicamente debe lanzar excepción cuando producto no existe")
    void eliminarFisicamenteDebeLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        ProductoNotFoundException exception = assertThrows(
                ProductoNotFoundException.class,
                () -> productoService.eliminarFisicamente(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(productoRepository, never()).deleteById(anyLong());
    }
}
