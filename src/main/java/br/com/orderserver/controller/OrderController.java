package br.com.orderserver.controller;

import br.com.orderserver.model.CustomerOrder;
import br.com.orderserver.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    /*@GetMapping("/{id}")
    public Optional<CustomerOrder> getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }*/
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long id) {
        Optional<CustomerOrder> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());  // Retorna 200 se encontrado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 se não encontrado
        }
    }


}