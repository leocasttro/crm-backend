package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendamentoPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.AgendarPedidoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.AjustarAgendamentoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.AprovarAgendamentoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.RejeitarAgendamentoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoAgendamentoController {

  private final AjustarAgendamentoUseCase ajustarAgendamentoUseCase;
  private final AgendarPedidoUseCase agendarPedidoUseCase;
  private final AprovarAgendamentoUseCase aprovarAgendamentoUseCase;
  private final RejeitarAgendamentoUseCase rejeitarAgendamentoUseCase;
  private final PedidoResponseMapper mapper;

  @PostMapping("/{pedidoId}/agendamento/solicitar")
  public ResponseEntity<PedidoResponse> solicitarAgendamento(
          @PathVariable String pedidoId,
          @RequestBody AgendamentoRequest request) {

    var command = AgendamentoPedidoCommand.builder()
            .pedidoId(pedidoId)
            .dataAgendamento(request.getDataAgendamento())
            .local(request.getLocal())
            .hospital(request.getHospital())
            .fornecedor(request.getFornecedor())
            .riscoCirurgico(request.getRiscoCirurgico())
            .duracaoEstimada(request.getDuracaoEstimada())
            .build();

    PedidoCirurgico pedido = agendarPedidoUseCase.execute(command);
    return ResponseEntity.ok(mapper.toResponse(pedido));
  }

  @PostMapping("/{pedidoId}/agendamento/aprovar")
  public ResponseEntity<PedidoResponse> aprovarAgendamento(
          @PathVariable String pedidoId,
          @RequestBody AgendamentoAprovadoRequest request) {

    PedidoCirurgico pedido = aprovarAgendamentoUseCase.execute(
            pedidoId,
            getUsuarioLogado(),
            request.getObservacao()
    );

    return ResponseEntity.ok(mapper.toResponse(pedido));
  }

  @PostMapping("/{pedidoId}/agendamento/rejeitar")
  public ResponseEntity<PedidoResponse> rejeitarAgendamento(
          @PathVariable String pedidoId,
          @RequestBody AgendamentoRejeicaoRequest request) {

    PedidoCirurgico pedido = rejeitarAgendamentoUseCase.execute(
            pedidoId,
            getUsuarioLogado(),
            request.getMotivo()
    );

    return ResponseEntity.ok(mapper.toResponse(pedido));
  }

  @PostMapping("/{pedidoId}/agendamento/ajustar")
  public ResponseEntity<PedidoResponse> ajustarAgendamento(
          @PathVariable String pedidoId,
          @RequestBody AgendamentoRequest request) {

    var command = AgendamentoPedidoCommand.builder()
            .pedidoId(pedidoId)
            .dataAgendamento(request.getDataAgendamento())
            .local(request.getLocal())
            .hospital(request.getHospital())
            .fornecedor(request.getFornecedor())
            .riscoCirurgico(request.getRiscoCirurgico())
            .duracaoEstimada(request.getDuracaoEstimada())
            .build();

    PedidoCirurgico pedido = ajustarAgendamentoUseCase.execute(command);
    return ResponseEntity.ok(mapper.toResponse(pedido));
  }

  private String getUsuarioLogado() {
    return "usuario_atual";
  }
}
