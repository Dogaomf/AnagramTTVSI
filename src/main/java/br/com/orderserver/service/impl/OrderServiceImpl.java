package br.com.orderserver.service.impl;


import br.com.orderserver.enums.OrderStatus;
import br.com.orderserver.model.CustomerOrder;
import br.com.orderserver.model.OrderItem;
import br.com.orderserver.model.Product;
import br.com.orderserver.repository.OrderRepository;
import br.com.orderserver.repository.ProductRepository;
import br.com.orderserver.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public CustomerOrder processOrder(CustomerOrder order) {
        if (orderRepository.existsByExternalOrderId(order.getExternalOrderId())) {
            throw new RuntimeException("Pedido duplicado detectado. ID: " + order.getExternalOrderId());
        }

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProduct().getId()));

            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setCustomerOrder(order);
        }

        order.setStatus(OrderStatus.PROCESSING);
        order = orderRepository.save(order);

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Override
    public Optional<CustomerOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}