package com.ecommerceKafka.pedidos.controller.mappers;

import com.ecommerceKafka.pedidos.controller.dto.NovoPedidoDTO;
import com.ecommerceKafka.pedidos.model.ItemPedido;
import com.ecommerceKafka.pedidos.model.Pedido;
import com.ecommerceKafka.pedidos.model.enums.StatusPedido;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {

    Pedido map(NovoPedidoDTO dto);

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido) {
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(LocalDateTime.now());

        var total = pedido.getItens().stream()
                .map(item -> item.getValorUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
    }
}