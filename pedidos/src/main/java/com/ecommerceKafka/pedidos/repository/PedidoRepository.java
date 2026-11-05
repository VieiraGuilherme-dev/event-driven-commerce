package com.ecommerceKafka.pedidos.repository;

import com.ecommerceKafka.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
