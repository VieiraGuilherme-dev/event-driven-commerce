package com.ecommerceKafka.produtos.repository;

import com.ecommerceKafka.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
