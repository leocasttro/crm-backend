package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.AtualizarStatusCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.AtualizarStatusPedidoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.FindPedidoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.IniciarAnaliseUseCase;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoAnaliseController {

  private final FindPedidoUseCase findUseCase;
  private final AtualizarStatusPedidoUseCase analisarPedidoUseCase;
  private final IniciarAnaliseUseCase iniciarAnaliseUseCase;
  private final PedidoResponseMapper mapper;

  @PostMapping("/{id}/analise")
  public ResponseEntity<AnaliseResponse> analisar(
          @PathVariable String id,
          @Valid @RequestBody AnaliseRequest request) {

    AtualizarStatusCommand command = new AtualizarStatusCommand();
    command.setPedidoId(id);
    command.setUsuario(getUsuarioLogado());
    command.setObservacao(request.getObservacao());

    ResultadoOperacao<PedidoCirurgico> resultado = analisarPedidoUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(AnaliseResponse.erro(resultado.getMensagem(), resultado.getErros()));
    }

    return ResponseEntity.ok(AnaliseResponse.sucesso(
            request.isAprovado(),
            resultado.getMensagem(),
            mapper.toResponse(resultado.getDados())
    ));
  }

  @PostMapping("/{id}/analise/iniciar")
  public ResponseEntity<ResultadoOperacao<PedidoResponse>> iniciarAnalise(@PathVariable String id) {
    ResultadoOperacao<PedidoCirurgico> resultado = iniciarAnaliseUseCase.execute(id, getUsuarioLogado());

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(ResultadoOperacao.erro(resultado.getMensagem()));
    }

    return ResponseEntity.ok(ResultadoOperacao.sucesso(
            mapper.toResponse(resultado.getDados()),
            resultado.getMensagem()
    ));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<AtualizarStatusResponse> atualizarStatus(
          @PathVariable String id,
          @Valid @RequestBody AtualizarStatusRequest request) {

    AtualizarStatusCommand command = new AtualizarStatusCommand();
    command.setPedidoId(id);
    command.setNovoStatus(request.getStatus());
    command.setObservacao(request.getObservacao());
    command.setUsuario(getUsuarioLogado());

    PedidoCirurgico pedidoAntes = findUseCase.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

    String statusAnterior = pedidoAntes.getStatus().getTipo().name();

    ResultadoOperacao<PedidoCirurgico> resultado = analisarPedidoUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(AtualizarStatusResponse.erro(
                      resultado.getMensagem(),
                      resultado.getErros()
              ));
    }

    PedidoCirurgico pedido = resultado.getDados();

    return ResponseEntity.ok(AtualizarStatusResponse.sucesso(
            resultado.getMensagem(),
            pedido.getId().getValue().toString(),
            statusAnterior,
            pedido.getStatus().getTipo().name(),
            pedido.getPacienteId() != null ? pedido.getPacienteId().getValue().toString() : null,
            pedido.getProcedimento() != null ? pedido.getProcedimento().getDescricao() : null,
            getUsuarioLogado(),
            request.getObservacao()
    ));
  }

  private String getUsuarioLogado() {
    return "usuario_atual";
  }
}
