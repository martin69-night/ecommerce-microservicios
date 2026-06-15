package cl.duoc.catalogservice.controller;

import cl.duoc.catalogservice.exception.ProductoNotFoundException;
import cl.duoc.catalogservice.exception.GlobalExceptionHandler;
import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ProductoController - Capa Controlador")
class ProductoControllerTest {

    private MockMvc mockMvc;
    private ProductoService productoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productoService = mock(ProductoService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductoController(productoService)).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /api/productos debe retornar 200 con lista")
    void listarActivosDebeRetornar200() throws Exception {
        Producto p = new Producto(1L, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        when(productoService.listarActivos()).thenReturn(List.of(p));
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} debe retornar 200 cuando existe")
    void buscarPorIdDebeRetornar200() throws Exception {
        Producto p = new Producto(1L, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        when(productoService.buscarPorId(1L)).thenReturn(p);
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} debe retornar 404 cuando no existe")
    void buscarPorIdDebeRetornar404() throws Exception {
        when(productoService.buscarPorId(99L)).thenThrow(new ProductoNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/productos debe retornar 201 al crear")
    void crearDebeRetornar201() throws Exception {
        Producto nuevo = new Producto(null, "SKU-NEW", "Teclado", "Desc", new BigDecimal("49.99"), "Acc", true);
        Producto creado = new Producto(1L, "SKU-NEW", "Teclado", "Desc", new BigDecimal("49.99"), "Acc", true);
        when(productoService.crear(any())).thenReturn(creado);
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
