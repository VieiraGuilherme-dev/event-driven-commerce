package com.ecommerceKafka.pedidos.validator;


import com.ecommerceKafka.pedidos.client.ClientesClient;
import com.ecommerceKafka.pedidos.client.ProdutosClient;
import com.ecommerceKafka.pedidos.client.representation.ClienteRepresentation;
import com.ecommerceKafka.pedidos.client.representation.ProdutoRepresentation;
import com.ecommerceKafka.pedidos.model.ItemPedido;
import com.ecommerceKafka.pedidos.model.Pedido;
import com.ecommerceKafka.pedidos.model.exception.ValidationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
    }

    private void validarCliente(Long codigoCliente){
        try {
            var response = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = response.getBody();
            log.info("Cliente de código {} encontrado: {}", cliente.codigo(), cliente.nome());
        } catch (FeignException.NotFound e){
            var message = String.format("Cliente do código %d não encontrado.", codigoCliente);
            throw new ValidationException("códigoCliente",message);

        }
    }

    private void validarItem(ItemPedido item){
        try{
            var response = produtosClient.obterDados(item.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome());
        } catch (FeignException.NotFound e){
            var message = String.format("Produto de código %d não encontrado.", item.getCodigoProduto());
            throw new ValidationException("códigoProduto",message);
        }

    }
}
