package com.ecommerceKafka.pedidos.repository;

import com.ecommerceKafka.pedidos.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
