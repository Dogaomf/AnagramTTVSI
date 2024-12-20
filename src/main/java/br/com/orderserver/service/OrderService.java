package br.com.orderserver.service;

import br.com.orderserver.exception.PedidoDuplicadoException;
import br.com.orderserver.model.Pedido;
import br.com.orderserver.model.Produto;
import br.com.orderserver.repository.PedidoRepository;
import br.com.orderserver.repository.ProdutoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public OrderService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Pedido processarPedido(Pedido pedido) {
        // Verifica se já existe um pedido com o mesmo código
        Optional<Pedido> pedidoExistente = pedidoRepository.findByCodigo(pedido.getCodigo());
        if (pedidoExistente.isPresent()) {
            throw new PedidoDuplicadoException("Pedido com o código " + pedido.getCodigo() + " já existe.");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Produto produto : pedido.getProdutos()) {
            Produto produtoEstoque = buscarProdutoNoEstoque(produto.getId());

            if (produtoEstoque.getQuantidade() < produto.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto " + produto.getNome());
            }

            produtoEstoque.setQuantidade(produtoEstoque.getQuantidade() - produto.getQuantidade());
            try {
                produtoRepository.save(produtoEstoque);
            } catch (OptimisticLockingFailureException e) {
                throw new IllegalStateException("O estoque foi modificado por outra transação, tente novamente.");
            }

            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(produto.getQuantidade())));
        }

        pedido.setTotal(total);
        pedido.setStatus("PROCESSADO");

        // Salve o pedido após a atualização do total e status
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> consultarPedidos(String status) {
        return pedidoRepository.findByStatus(status);
    }

    @Cacheable(value = "produtosCache", key = "#id")
    public Produto buscarProdutoNoEstoque(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no estoque"));
    }

    @Cacheable(value = "produtosCache", key = "#id")
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}