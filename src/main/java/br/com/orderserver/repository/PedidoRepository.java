package br.com.orderserver.repository;

import br.com.orderserver.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatus(String status);
    Optional<Pedido> findByCodigo(Integer codigo);
}