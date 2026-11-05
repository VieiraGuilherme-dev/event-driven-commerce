package com.ecommerceKafka.pedidos.controller.mappers;

import com.ecommerceKafka.pedidos.controller.dto.NovoPedidoDTO;
import com.ecommerceKafka.pedidos.model.Pedido;

public interface PedidoMapper {
    Pedido map(NovoPedidoDTO dto);
}
