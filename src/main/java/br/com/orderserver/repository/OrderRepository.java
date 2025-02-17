package br.com.orderserver.repository;

import br.com.orderserver.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    boolean existsByExternalOrderId(String externalOrderId);
    Optional<CustomerOrder> findByExternalOrderId(String externalOrderId);
}