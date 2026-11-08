package com.ecommerceKafka.pedidos.controller.mappers;

import com.ecommerceKafka.pedidos.controller.dto.ItemPedidoDTO;
import com.ecommerceKafka.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    ItemPedido map(ItemPedidoDTO dto);
}
