/*
package br.com.orderserver.service;

import br.com.orderserver.exception.PedidoDuplicadoException;
import br.com.orderserver.model.Pedido;
import br.com.orderserver.model.Item;
import br.com.orderserver.repository.PedidoRepository;
import br.com.orderserver.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private OrderService orderService;

    private Pedido pedido;

    private Item item;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus("PENDENTE");

        // Atualizando os produtos com o campo versao como Integer
        pedido.setProdutos(Arrays.asList(
                new Item("Produto 1", BigDecimal.valueOf(10), 2, 1),
                new Item("Produto 2", BigDecimal.valueOf(20), 1, 2)
        ));
    }

    @Test
    public void testProcessarPedido_QuandoProdutoNaoExistir_DeveLancarExcecao() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Long id = 15L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            orderService.processarPedido(pedido);
        });

        Assert.assertEquals("Produto não encontrado no estoque", exception.getMessage());
    }

    @Test
    public void testProcessarPedido_QuandoPedidoNaoExistir_DeveProcessarComSucesso() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Item itemEmEstoque = new Item("Produto 1", BigDecimal.valueOf(10), 10, 1);
        itemEmEstoque.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEmEstoque));

        List<Item> items = new ArrayList<>();
        items.add(itemEmEstoque);
        pedido.setProdutos(items);

        Pedido resultado = orderService.processarPedido(pedido);

        Assert.assertEquals(1, resultado.getItems().get(0).getVersao().intValue());

        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testProcessarPedido_QuandoPedidoExistir_DeveLancarExcecao() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        PedidoDuplicadoException exception = Assert.assertThrows(PedidoDuplicadoException.class, () -> {
            orderService.processarPedido(pedido);
        });

        Assert.assertEquals("Pedido com o identificador 1 já existe.", exception.getMessage());
        verify(pedidoRepository, times(0)).save(pedido);
    }

    @Test
    public void testConsultarPedidos() {
        Pedido pedido1 = new Pedido(1L, Arrays.asList(), BigDecimal.valueOf(100), "PROCESSADO",1);
        Pedido pedido2 = new Pedido(2L, Arrays.asList(), BigDecimal.valueOf(200), "PROCESSADO",1);

        when(pedidoRepository.findByStatus("PROCESSADO")).thenReturn(Arrays.asList(pedido1, pedido2));

        List<Pedido> pedidos = orderService.consultarPedidos("PROCESSADO");

        Assert.assertEquals(2, pedidos.size());
        Assert.assertTrue(pedidos.contains(pedido1));
        Assert.assertTrue(pedidos.contains(pedido2));
    }

    @Test
    public void testProcessarPedido_Sucesso() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus("PENDENTE");
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setProdutos(new ArrayList<>());

        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.empty());

        Item itemEmEstoque = new Item("Produto 1", BigDecimal.valueOf(10), 10, 1);
        itemEmEstoque.setId(1L);
        itemEmEstoque.setQuantidade(10);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEmEstoque));

        Item itemNoPedido = new Item("Produto 1", BigDecimal.valueOf(10), 3, 1);
        itemNoPedido.setId(1L);

        pedido.getItems().add(itemNoPedido);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        Pedido pedidoSalvo = orderService.processarPedido(pedido);

        Assert.assertNotNull(pedidoSalvo);
        Assert.assertEquals("PROCESSADO", pedidoSalvo.getStatus());
        Assert.assertTrue(pedidoSalvo.getTotal().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testProcessarPedido_PedidoDuplicado() {
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        Assert.assertThrows(PedidoDuplicadoException.class, () -> orderService.processarPedido(pedido));
    }

    @Test
    public void testBuscarPedidoPorId_PedidoNaoEncontrado() {
        Long idPedido = 2L;
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> {
            pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        });

        Assert.assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @Test
    public void testProcessarPedido_EstoqueInsuficiente() {
        Item itemEstoque = new Item("Produto 1", BigDecimal.valueOf(10), 3, 1);
        itemEstoque.setId(1L);

        Item itemPedido = new Item("Produto 1", BigDecimal.valueOf(10), 5, 1);
        itemPedido.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEstoque));

        pedido.setProdutos(Collections.singletonList(itemPedido));

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            orderService.processarPedido(pedido);
        });

        Assert.assertEquals("Estoque insuficiente para o produto Produto 1", exception.getMessage());
    }

   @Test
    public void testProcessarPedido_OptimisticLockingFailure() {
        Item itemEstoque = new Item("Produto 1", BigDecimal.valueOf(10), 10, 1);
        itemEstoque.setId(1L);

        Item itemPedido = new Item("Produto 1", BigDecimal.valueOf(10), 5, 1);
        itemPedido.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEstoque));
        doThrow(OptimisticLockingFailureException.class).when(itemRepository).save(itemEstoque);

        pedido.setProdutos(Collections.singletonList(itemPedido));

        IllegalStateException exception = Assert.assertThrows(IllegalStateException.class, () -> {
            orderService.processarPedido(pedido);
        });

        Assert.assertEquals("O estoque foi modificado por outra transação, tente novamente.", exception.getMessage());
    }
}*/
