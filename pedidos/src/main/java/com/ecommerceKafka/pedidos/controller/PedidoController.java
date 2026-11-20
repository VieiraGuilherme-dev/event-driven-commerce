package com.ecommerceKafka.pedidos.controller;


import com.ecommerceKafka.pedidos.controller.dto.AdicaoNovoPagamentoDTO;
import com.ecommerceKafka.pedidos.controller.dto.NovoPedidoDTO;
import com.ecommerceKafka.pedidos.controller.mappers.PedidoMapper;
import com.ecommerceKafka.pedidos.model.ErroResposta;
import com.ecommerceKafka.pedidos.model.exception.ItemNaoEncontradoException;
import com.ecommerceKafka.pedidos.model.exception.ValidationException;
import com.ecommerceKafka.pedidos.publisher.DetalhesPedidoMapper;
import com.ecommerceKafka.pedidos.publisher.representation.DetalhePedidoRepresentation;
import com.ecommerceKafka.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;
    private final DetalhesPedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO dto) {
        try {
            var pedido = mapper.map(dto);
            var novoPedido = service.criarPedido(pedido);
            return ResponseEntity.ok(novoPedido.getCodigo());
        } catch (ValidationException e) {
            var erro = new ErroResposta("Erro validação", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(@RequestBody AdicaoNovoPagamentoDTO dto){
        try{
            service.adicionarNovoPagamento(dto.codigoPedido(), dto.dadosCartao(), dto.tipoPagamento());
            return ResponseEntity.noContent().build();
        }catch (ItemNaoEncontradoException e){
            var erro = new ErroResposta("Item não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("{codigo}")
    public ResponseEntity<DetalhePedidoRepresentation> obterDetalhesPedido(@PathVariable Long codigo){
        return service
                .carregarDadosCompletosPedido(codigo)
                .map(pedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

