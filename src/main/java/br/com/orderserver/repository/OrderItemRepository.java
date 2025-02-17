package br.com.orderserver.repository;

import br.com.orderserver.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}