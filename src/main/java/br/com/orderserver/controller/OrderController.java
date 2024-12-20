package br.com.orderserver.controller;

import br.com.orderserver.model.Pedido;
import br.com.orderserver.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/pedidos")
    public ResponseEntity<Pedido> receberPedido(@RequestBody Pedido pedido) {
        Pedido processado = orderService.processarPedido(pedido);
        return ResponseEntity.ok(processado);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> consultarPedidos(@RequestParam String status) {
        List<Pedido> pedidos = orderService.consultarPedidos(status);
        return ResponseEntity.ok(pedidos);
    }
}