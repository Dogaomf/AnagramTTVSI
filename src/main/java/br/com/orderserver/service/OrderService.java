package br.com.orderserver.service;

import br.com.orderserver.model.CustomerOrder;

import java.util.Optional;

public interface OrderService {
    CustomerOrder processOrder(CustomerOrder order);
    Optional<CustomerOrder> getOrderById(Long id);
}
