package br.com.orderserver.controller;

import br.com.orderserver.model.CustomerOrder;
import br.com.orderserver.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOrder createOrder(@RequestBody CustomerOrder order) {
        return orderService.processOrder(order);
    }

    @GetMapping("/{id}")
    public Optional<CustomerOrder> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

/*    @GetMapping("/{id}/total")
    public BigDecimal getOrderTotal(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(CustomerOrder::calculateTotal)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }*/
}