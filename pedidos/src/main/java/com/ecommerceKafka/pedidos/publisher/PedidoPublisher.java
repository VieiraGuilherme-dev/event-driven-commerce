package com.ecommerceKafka.pedidos.publisher;

import com.ecommerceKafka.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DetalhesPedidoMapper mapper;
    private final ObjectMapper objectMapper;

    @Value("${icompras.config.kafka.topics.pedidos-pagos}")
    private String topicPedidosPagos;

    public void publicarPedidoPago(Pedido pedido) {
        try {
            var detalhes = mapper.map(pedido);
            var payload = objectMapper.writeValueAsString(detalhes);
            kafkaTemplate.send(topicPedidosPagos, String.valueOf(pedido.getCodigo()), payload);
            log.info("Pedido {} publicado no tópico {}", pedido.getCodigo(), topicPedidosPagos);
        } catch (JacksonException e) {
            log.error("Erro ao serializar pedido {} para Kafka", pedido.getCodigo(), e);
            throw new RuntimeException("Erro ao publicar pedido no Kafka", e);
        }
    }
}
