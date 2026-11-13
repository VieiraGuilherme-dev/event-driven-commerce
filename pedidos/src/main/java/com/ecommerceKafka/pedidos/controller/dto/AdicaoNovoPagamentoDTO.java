package com.ecommerceKafka.pedidos.controller.dto;

import com.ecommerceKafka.pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(Long codigoPedido, String dadosCartao, TipoPagamento tipoPagamento) {
}
