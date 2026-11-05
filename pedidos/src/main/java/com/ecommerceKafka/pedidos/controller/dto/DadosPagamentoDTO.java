package com.ecommerceKafka.pedidos.controller.dto;

import com.ecommerceKafka.pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(String dados, TipoPagamento tipoPagamento) {
}
