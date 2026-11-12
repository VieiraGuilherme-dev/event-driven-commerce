package com.ecommerceKafka.pedidos.validator;


import com.ecommerceKafka.pedidos.client.ClientesClient;
import com.ecommerceKafka.pedidos.client.ProdutosClient;
import com.ecommerceKafka.pedidos.client.representation.ProdutoRepresentation;
import com.ecommerceKafka.pedidos.model.ItemPedido;
import com.ecommerceKafka.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
    }

    private void validarCliente(Long codigoCliente){

    }

    private void validarItem(ItemPedido item){

    }
}
