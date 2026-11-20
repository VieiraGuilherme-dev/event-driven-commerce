package com.ecommerceKafka.pedidos.service;


import com.ecommerceKafka.pedidos.client.ClientesClient;
import com.ecommerceKafka.pedidos.client.ProdutosClient;
import com.ecommerceKafka.pedidos.client.ServicoBancarioClient;
import com.ecommerceKafka.pedidos.model.DadosPagamento;
import com.ecommerceKafka.pedidos.model.ItemPedido;
import com.ecommerceKafka.pedidos.model.Pedido;
import com.ecommerceKafka.pedidos.model.enums.StatusPedido;
import com.ecommerceKafka.pedidos.model.enums.TipoPagamento;
import com.ecommerceKafka.pedidos.model.exception.ItemNaoEncontradoException;
import com.ecommerceKafka.pedidos.publisher.PedidoPublisher;
import com.ecommerceKafka.pedidos.repository.ItemPedidoRepository;
import com.ecommerceKafka.pedidos.repository.PedidoRepository;
import com.ecommerceKafka.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient clientesClient;
    private final ProdutosClient produtosClient;
    private final PedidoPublisher pedidoPublisher;

    @Transactional
    public Pedido criarPedido(Pedido pedido){
        validator.validar(pedido);
        realizarPersistencia(pedido);
        enviarSolicitacaoPagamento(pedido);
        return pedido;
    }

    private void enviarSolicitacaoPagamento(Pedido pedido) {
        var chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    public void atualizarStatusPagamento(Long codigoPedido, String chavePagamento, boolean sucess, String observacoes) {
        var pedidoEncontrado = repository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if(pedidoEncontrado.isEmpty()){
            var msg = String.format("Pedido não encontrado para o código %d e chave pagamento %s", codigoPedido, chavePagamento);
            log.error(msg);
            return;
        }

        Pedido pedido = pedidoEncontrado.get();
        if(sucess){
            pedido.setStatus(StatusPedido.PAGO);
            repository.save(pedido);
            var dadosCliente = clientesClient.obterDados(pedido.getCodigoCliente()).getBody();
            pedido.setDadosCliente(dadosCliente);
            pedidoPublisher.publicarPedidoPago(pedido);
        }else{
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
            repository.save(pedido);
        }
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao, TipoPagamento tipo){
        var pedidoEncontrado = repository.findById(codigoPedido);

        if(pedidoEncontrado.isEmpty()){
            throw new ItemNaoEncontradoException("Pedido não encontrado para o código informado");
        }

        var pedido = pedidoEncontrado.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setTipoPagamento(tipo);
        dadosPagamento.setDados(dadosCartao);

        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando o processamento.");

        repository.save(pedido);

        String novaChavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);
    }

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo) {
        Optional<Pedido> pedido = repository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarItensPedido);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido) {
        var dadosCliente = clientesClient.obterDados(pedido.getCodigoCliente()).getBody();
        pedido.setDadosCliente(dadosCliente);
    }

    private void carregarItensPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido item) {
        var produto = produtosClient.obterDados(item.getCodigoProduto()).getBody();
        item.setNome(produto.nome());
    }
}
