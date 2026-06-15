package cl.duoc.inventoryservice.controller;

import cl.duoc.inventoryservice.exception.GlobalExceptionHandler;
import cl.duoc.inventoryservice.exception.InventarioNotFoundException;
import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("InventarioController - Capa Controlador")
class InventarioControllerTest {

    private MockMvc mockMvc;
    private InventarioService inventarioService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        inventarioService = mock(InventarioService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new InventarioController(inventarioService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /api/inventario debe retornar 200 con lista")
    void listarActivosDebeRetornar200() throws Exception {
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        when(inventarioService.listarActivos()).thenReturn(List.of(inv));
        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-001"));
    }

    @Test
    @DisplayName("GET /api/inventario/{id} debe retornar 200 cuando existe")
    void buscarPorIdDebeRetornar200() throws Exception {
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        when(inventarioService.buscarPorId(1L)).thenReturn(inv);
        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @DisplayName("GET /api/inventario/{id} debe retornar 404 cuando no existe")
    void buscarPorIdDebeRetornar404() throws Exception {
        when(inventarioService.buscarPorId(99L)).thenThrow(new InventarioNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/inventario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/inventario debe retornar 201 al crear")
    void crearDebeRetornar201() throws Exception {
        Inventario nuevo = new Inventario(null, 10L, "SKU-NEW", 50, 0, "Bodega B", true);
        Inventario creado = new Inventario(1L, 10L, "SKU-NEW", 50, 0, "Bodega B", true);
        when(inventarioService.crear(any())).thenReturn(creado);
        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
