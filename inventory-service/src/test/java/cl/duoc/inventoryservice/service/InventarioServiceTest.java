package cl.duoc.inventoryservice.service;

import cl.duoc.inventoryservice.exception.InventarioNotFoundException;
import cl.duoc.inventoryservice.exception.SkuInventarioDuplicadoException;
import cl.duoc.inventoryservice.exception.StockInsuficienteException;
import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.repository.InventarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Capa Servicio")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventarioValido() {
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
    @DisplayName("listarActivos debe retornar inventarios activos")
    void listarActivosDebeRetornarLista() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findByActivoTrue()).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals("SKU-001", resultado.get(0).getSku());
        verify(inventarioRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("listarTodos debe retornar todos los inventarios")
    void listarTodosDebeRetornarLista() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<Inventario> resultado = inventarioService.listarTodos();

        assertEquals(1, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId debe retornar inventario cuando existe")
    void buscarPorIdDebeRetornarInventarioCuandoExiste() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(inventarioRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        InventarioNotFoundException exception = assertThrows(
                InventarioNotFoundException.class,
                () -> inventarioService.buscarPorId(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(inventarioRepository).findById(99L);
    }

    @Test
    @DisplayName("buscarPorSku debe retornar inventario cuando existe")
    void buscarPorSkuDebeRetornarInventarioCuandoExiste() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findBySku("SKU-001")).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorSku("SKU-001");

        assertEquals("SKU-001", resultado.getSku());
        verify(inventarioRepository).findBySku("SKU-001");
    }

    @Test
    @DisplayName("buscarPorSku debe lanzar excepción cuando no existe")
    void buscarPorSkuDebeLanzarExcepcionCuandoNoExiste() {
        when(inventarioRepository.findBySku("SKU-404")).thenReturn(Optional.empty());

        InventarioNotFoundException exception = assertThrows(
                InventarioNotFoundException.class,
                () -> inventarioService.buscarPorSku("SKU-404")
        );

        assertTrue(exception.getMessage().contains("SKU-404"));
        verify(inventarioRepository).findBySku("SKU-404");
    }

    @Test
    @DisplayName("buscarPorProductoId debe retornar inventario cuando existe")
    void buscarPorProductoIdDebeRetornarInventarioCuandoExiste() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorProductoId(10L);

        assertEquals(10L, resultado.getProductoId());
        verify(inventarioRepository).findByProductoId(10L);
    }

    @Test
    @DisplayName("buscarPorProductoId debe lanzar excepción cuando no existe")
    void buscarPorProductoIdDebeLanzarExcepcionCuandoNoExiste() {
        when(inventarioRepository.findByProductoId(999L)).thenReturn(Optional.empty());

        InventarioNotFoundException exception = assertThrows(
                InventarioNotFoundException.class,
                () -> inventarioService.buscarPorProductoId(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(inventarioRepository).findByProductoId(999L);
    }

    @Test
    @DisplayName("crear debe lanzar excepción cuando el SKU ya existe")
    void crearDebeLanzarExcepcionSiSkuYaExiste() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.existsBySku("SKU-001")).thenReturn(true);

        SkuInventarioDuplicadoException exception = assertThrows(
                SkuInventarioDuplicadoException.class,
                () -> inventarioService.crear(inventario)
        );

        assertTrue(exception.getMessage().contains("SKU-001"));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe inicializar valores por defecto y guardar")
    void crearDebeInicializarValoresPorDefecto() {
        Inventario inventario = new Inventario(
                99L,
                10L,
                "SKU-NUEVO",
                null,
                null,
                "Bodega B",
                null
        );

        when(inventarioRepository.existsBySku("SKU-NUEVO")).thenReturn(false);
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.crear(inventario);

        assertNull(resultado.getId());
        assertEquals(0, resultado.getCantidadDisponible().intValue());
        assertEquals(0, resultado.getCantidadReservada().intValue());
        assertTrue(resultado.getActivo());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    @DisplayName("actualizar debe modificar inventario cuando el nuevo SKU no existe")
    void actualizarDebeModificarInventario() {
        Inventario existente = inventarioValido();
        Inventario datos = new Inventario(
                null,
                20L,
                "SKU-002",
                150,
                30,
                "Bodega C",
                true
        );

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(inventarioRepository.existsBySku("SKU-002")).thenReturn(false);
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.actualizar(1L, datos);

        assertEquals(20L, resultado.getProductoId());
        assertEquals("SKU-002", resultado.getSku());
        assertEquals(150, resultado.getCantidadDisponible().intValue());
        assertEquals(30, resultado.getCantidadReservada().intValue());
        assertEquals("Bodega C", resultado.getUbicacion());
        verify(inventarioRepository).save(existente);
    }

    @Test
    @DisplayName("actualizar debe lanzar excepción cuando el nuevo SKU pertenece a otro inventario")
    void actualizarDebeLanzarExcepcionSiSkuNuevoYaExiste() {
        Inventario existente = inventarioValido();
        Inventario datos = new Inventario(
                null,
                10L,
                "SKU-DUPLICADO",
                100,
                20,
                "Bodega A",
                true
        );

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(inventarioRepository.existsBySku("SKU-DUPLICADO")).thenReturn(true);

        assertThrows(
                SkuInventarioDuplicadoException.class,
                () -> inventarioService.actualizar(1L, datos)
        );

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("reservarStock debe aumentar la cantidad reservada cuando existe stock libre")
    void reservarStockDebeAumentarCantidadReservada() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.reservarStock(1L, 10);

        assertEquals(30, resultado.getCantidadReservada().intValue());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    @DisplayName("reservarStock debe lanzar excepción cuando no hay stock libre suficiente")
    void reservarStockDebeLanzarExcepcionSiStockInsuficiente() {
        Inventario inventario = new Inventario(
                1L, 10L, "SKU-001", 10, 8, "Bodega A", true
        );

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertThrows(
                StockInsuficienteException.class,
                () -> inventarioService.reservarStock(1L, 5)
        );

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("reservarStock debe rechazar cantidad nula o menor a uno")
    void reservarStockDebeRechazarCantidadInvalida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.reservarStock(1L, null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.reservarStock(1L, 0)
        );

        verify(inventarioRepository, never()).findById(any());
    }

    @Test
    @DisplayName("liberarReserva debe disminuir la cantidad reservada")
    void liberarReservaDebeDisminuirCantidadReservada() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.liberarReserva(1L, 5);

        assertEquals(15, resultado.getCantidadReservada().intValue());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    @DisplayName("liberarReserva debe lanzar excepción al intentar liberar más de lo reservado")
    void liberarReservaDebeLanzarExcepcionSiCantidadSuperaReserva() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventarioService.liberarReserva(1L, 21)
        );

        assertEquals("No se puede liberar mas stock del reservado", exception.getMessage());
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("descontarStock debe disminuir disponible y reservada cuando corresponde")
    void descontarStockDebeDisminuirDisponibleYReservada() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.descontarStock(1L, 10);

        assertEquals(90, resultado.getCantidadDisponible().intValue());
        assertEquals(10, resultado.getCantidadReservada().intValue());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    @DisplayName("descontarStock debe lanzar excepción cuando no hay stock suficiente")
    void descontarStockDebeLanzarExcepcionSiStockInsuficiente() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        assertThrows(
                StockInsuficienteException.class,
                () -> inventarioService.descontarStock(1L, 101)
        );

        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("reponerStock debe aumentar la cantidad disponible")
    void reponerStockDebeAumentarCantidadDisponible() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventario resultado = inventarioService.reponerStock(1L, 25);

        assertEquals(125, resultado.getCantidadDisponible().intValue());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    @DisplayName("desactivar debe dejar inventario inactivo")
    void desactivarDebeDejarInventarioInactivo() {
        Inventario inventario = inventarioValido();
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        inventarioService.desactivar(1L);

        assertFalse(inventario.getActivo());
        verify(inventarioRepository).save(inventario);
    }
}
