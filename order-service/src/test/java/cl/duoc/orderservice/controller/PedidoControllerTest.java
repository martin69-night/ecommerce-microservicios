package cl.duoc.orderservice.controller;

import cl.duoc.orderservice.exception.GlobalExceptionHandler;
import cl.duoc.orderservice.exception.PedidoNotFoundException;
import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.service.PedidoService;
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

@DisplayName("PedidoController - Capa Controlador")
class PedidoControllerTest {

    private MockMvc mockMvc;
    private PedidoService pedidoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        pedidoService = mock(PedidoService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new PedidoController(pedidoService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET /api/pedidos debe retornar 200 con lista")
    void listarActivosDebeRetornar200() throws Exception {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setEstado("CREADO");
        when(pedidoService.listarActivos()).thenReturn(List.of(p));
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("CREADO"));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} debe retornar 200 cuando existe")
    void buscarPorIdDebeRetornar200() throws Exception {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setEstado("CREADO");
        when(pedidoService.buscarPorId(1L)).thenReturn(p);
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} debe retornar 404 cuando no existe")
    void buscarPorIdDebeRetornar404() throws Exception {
        when(pedidoService.buscarPorId(99L)).thenThrow(new PedidoNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/pedidos debe retornar 201 al crear")
    void crearDebeRetornar201() throws Exception {
        Pedido nuevo = new Pedido();
        nuevo.setUsuarioId(1L);
        nuevo.setProductoId(2L);
        nuevo.setSku("SKU-001");
        nuevo.setNombreProducto("Laptop");
        nuevo.setPrecioUnitario(new BigDecimal("999.99"));
        nuevo.setCantidad(1);

        Pedido creado = new Pedido();
        creado.setId(1L);
        creado.setEstado("CREADO");

        when(pedidoService.crear(any())).thenReturn(creado);
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
