package com.ecommerceKafka.pedidos.service;


import com.ecommerceKafka.pedidos.model.Pedido;
import com.ecommerceKafka.pedidos.repository.ItemPedidoRepository;
import com.ecommerceKafka.pedidos.repository.PedidoRepository;
import com.ecommerceKafka.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;

    public Pedido criarPedido(Pedido pedido){
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
        return pedido;

    }

}
