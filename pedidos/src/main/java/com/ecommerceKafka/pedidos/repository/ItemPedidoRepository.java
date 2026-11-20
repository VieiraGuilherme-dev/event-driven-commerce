package com.ecommerceKafka.pedidos.repository;

import com.ecommerceKafka.pedidos.model.ItemPedido;
import com.ecommerceKafka.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}
