package cl.duoc.cartservice.controller;

import cl.duoc.cartservice.exception.CarritoItemNotFoundException;
import cl.duoc.cartservice.exception.GlobalExceptionHandler;
import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.service.CarritoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

@DisplayName("CarritoController - Capa Controlador")
class CarritoControllerTest {

    private MockMvc mockMvc;
    private CarritoService carritoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        carritoService = mock(CarritoService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new CarritoController(carritoService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET /api/carrito debe retornar 200 con lista")
    void listarActivosDebeRetornar200() throws Exception {
        CarritoItem item = new CarritoItem(1L, 1L, 2L, "SKU-001", "Laptop", new BigDecimal("999.99"), 1, true, null);
        when(carritoService.listarActivos()).thenReturn(List.of(item));
        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-001"));
    }

    @Test
    @DisplayName("GET /api/carrito/{id} debe retornar 200 cuando existe")
    void buscarPorIdDebeRetornar200() throws Exception {
        CarritoItem item = new CarritoItem(1L, 1L, 2L, "SKU-001", "Laptop", new BigDecimal("999.99"), 1, true, null);
        when(carritoService.buscarPorId(1L)).thenReturn(item);
        mockMvc.perform(get("/api/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @DisplayName("GET /api/carrito/{id} debe retornar 404 cuando no existe")
    void buscarPorIdDebeRetornar404() throws Exception {
        when(carritoService.buscarPorId(99L)).thenThrow(new CarritoItemNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/carrito/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/carrito debe retornar 201 al agregar item")
    void agregarItemDebeRetornar201() throws Exception {
        CarritoItem nuevo = new CarritoItem(null, 1L, 2L, "SKU-001", "Laptop", new BigDecimal("999.99"), 1, true, null);
        CarritoItem creado = new CarritoItem(1L, 1L, 2L, "SKU-001", "Laptop", new BigDecimal("999.99"), 1, true, null);
        when(carritoService.agregarItem(any())).thenReturn(creado);
        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
