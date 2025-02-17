
package br.com.orderserver.service;

import br.com.orderserver.enums.OrderStatus;
import br.com.orderserver.model.CustomerOrder;
import br.com.orderserver.model.OrderItem;
import br.com.orderserver.model.Product;
import br.com.orderserver.repository.OrderRepository;
import br.com.orderserver.repository.ProductRepository;
import br.com.orderserver.service.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceTest.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void deveLancarErroQuandoPedidoDuplicado() {
        // Arrange
        CustomerOrder order = new CustomerOrder();
        order.setExternalOrderId("DUPLICADO");

        when(orderRepository.existsByExternalOrderId("DUPLICADO")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processOrder(order);
        });

        assertEquals("Pedido duplicado detectado. ID: DUPLICADO", exception.getMessage());
    }

    @Test
    public void deveLancarErroQuandoProdutoNaoForEncontrado() {
        CustomerOrder order = new CustomerOrder();
        order.setExternalOrderId("ABC123");

        OrderItem item = new OrderItem();
        Product product = new Product();
        product.setId(99L);
        item.setProduct(product);
        order.setItems(List.of(item));

        when(orderRepository.existsByExternalOrderId("ABC123")).thenReturn(false);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processOrder(order);
        });

        assertEquals("Produto não encontrado: 99", exception.getMessage());
    }

    @Test
    public void deveProcessarPedidoComSucesso() {
        CustomerOrder order = new CustomerOrder();
        order.setExternalOrderId("ABC123");

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("100.00"));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        order.setItems(List.of(item));

        when(orderRepository.existsByExternalOrderId("ABC123")).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerOrder processedOrder = orderService.processOrder(order);

        assertNotNull(processedOrder);
        assertEquals(OrderStatus.COMPLETED, processedOrder.getStatus());
        assertEquals(new BigDecimal("100.00"), processedOrder.getItems().get(0).getPrice());
        verify(orderRepository, times(2)).save(any(CustomerOrder.class));  // Salvo duas vezes (PROCESSING e COMPLETED)
    }


    @Test
    public void deveAlterarStatusDoPedido() {
        CustomerOrder order = new CustomerOrder();
        order.setExternalOrderId("ABC123");

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("100.00"));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        order.setItems(List.of(item));

        when(orderRepository.existsByExternalOrderId("ABC123")).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerOrder processedOrder = orderService.processOrder(order);

        assertEquals(OrderStatus.COMPLETED, processedOrder.getStatus());
    }

    @Test
    public void deveRetornarPedidoQuandoExistir() {
        CustomerOrder order = new CustomerOrder();
        order.setId(1L);
        order.setExternalOrderId("ABC123");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<CustomerOrder> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(order.getId(), result.get().getId());
    }

    @Test
    public void deveRetornarVazioQuandoPedidoNaoExistir() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CustomerOrder> result = orderService.getOrderById(1L);

        assertFalse(result.isPresent());
    }

}