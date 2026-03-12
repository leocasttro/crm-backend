// ConsultaPreController.java
package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.*;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.*;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/consulta-pre")
@RequiredArgsConstructor
public class ConsultaPreController {

  private final AgendarConsultaPreUseCase agendarConsultaPreUseCase;

  @PostMapping("/agendar")
  public ResponseEntity<ConsultaPreResponse> agendar(
          @PathVariable String pedidoId,
          @Valid @RequestBody AgendarConsultaPreRequest request) {

    var command = AgendarConsultaPreCommand.builder()
            .pedidoId(pedidoId)
            .dataHora(request.getDataHora())
            .cuidados(request.getCuidados())
            .horarios(request.getHorarios())
            .observacoesEspeciais(request.getObservacoesEspeciais())
            .usuario(getUsuarioLogado())
            .build();

    PedidoCirurgico pedido = agendarConsultaPreUseCase.execute(command);

    return ResponseEntity.ok(
            ConsultaPreResponse.fromDomain(pedido.getConsultaPreOperatoria())
    );
  }

  @GetMapping
  public ResponseEntity<ConsultaPreResponse> buscar(@PathVariable String pedidoId) {
    // Implementar busca se necessário
    return ResponseEntity.ok().build();
  }

  private String getUsuarioLogado() {
    // TODO: Implementar com Spring Security
    return "usuario_atual";
  }
}