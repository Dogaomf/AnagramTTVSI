package br.com.orderserver.controller;

import br.com.orderserver.model.CustomerOrder;
import br.com.orderserver.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void deveCriarPedidoComSucesso() throws Exception {
        CustomerOrder order = new CustomerOrder();
        order.setExternalOrderId("ABC123");

        when(orderService.processOrder(any(CustomerOrder.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalOrderId").value("ABC123"));
    }

    @Test
    void deveRetornarPedidoPorId() throws Exception {
        CustomerOrder order = criarPedido(1L, "ABC123");

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.externalOrderId").value("ABC123"));
    }

    private CustomerOrder criarPedido(Long id, String externalOrderId) {
        CustomerOrder order = new CustomerOrder();
        order.setId(id);
        order.setExternalOrderId(externalOrderId);
        return order;
    }

    @Test
    void deveRetornar404SePedidoNaoForEncontrado() throws Exception {
        when(orderService.getOrderById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/99"))
                .andExpect(status().isNotFound());
    }
}
