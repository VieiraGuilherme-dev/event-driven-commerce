package com.ecommerceKafka.pedidos.publisher.representation;

import com.ecommerceKafka.pedidos.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalhePedidoRepresentation(
    Long codigo,
    Long codigoCliente,
    String nome,
    String cpf,
    String logradouro,
    String numero,
    String bairro,
    String email,
    String telefone,
    LocalDateTime dataPedido,
    BigDecimal total,
    StatusPedido status,
    List<DetalheItemPedidoRepresentation> itens
) {
}
