package cl.duoc.paymentservice.controller;

import cl.duoc.paymentservice.exception.GlobalExceptionHandler;
import cl.duoc.paymentservice.exception.PagoNotFoundException;
import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.service.PagoService;
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

@DisplayName("PagoController - Capa Controlador")
class PagoControllerTest {

    private MockMvc mockMvc;
    private PagoService pagoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        pagoService = mock(PagoService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new PagoController(pagoService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET /api/pagos debe retornar 200 con lista")
    void listarActivosDebeRetornar200() throws Exception {
        Pago p = new Pago();
        p.id = 1L;
        p.estado = "PENDIENTE";
        when(pagoService.listarActivos()).thenReturn(List.of(p));
        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    @DisplayName("GET /api/pagos/{id} debe retornar 200 cuando existe")
    void buscarPorIdDebeRetornar200() throws Exception {
        Pago p = new Pago();
        p.id = 1L;
        p.estado = "PENDIENTE";
        when(pagoService.buscarPorId(1L)).thenReturn(p);
        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/pagos/{id} debe retornar 404 cuando no existe")
    void buscarPorIdDebeRetornar404() throws Exception {
        when(pagoService.buscarPorId(99L)).thenThrow(new PagoNotFoundException("No encontrado"));
        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/pagos debe retornar 201 al crear")
    void crearDebeRetornar201() throws Exception {
        Pago nuevo = new Pago();
        nuevo.pedidoId = 1L;
        nuevo.usuarioId = 2L;
        nuevo.monto = new BigDecimal("999.99");
        nuevo.metodoPago = "TARJETA";

        Pago creado = new Pago();
        creado.id = 1L;
        creado.estado = "PENDIENTE";

        when(pagoService.crear(any())).thenReturn(creado);
        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
