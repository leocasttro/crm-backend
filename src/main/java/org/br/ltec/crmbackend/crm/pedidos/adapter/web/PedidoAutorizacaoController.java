package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.SalvarDadosAutorizacaoCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.SalvarDadosAutorizacaoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoAutorizacaoController {

  private final SalvarDadosAutorizacaoUseCase salvarDadosAutorizacaoUseCase;
  private final PedidoResponseMapper mapper;

  @PutMapping("/{id}/autorizacao")
  public ResponseEntity<?> salvarDadosAutorizacao(
          @PathVariable UUID id,
          @RequestBody SalvarDadosAutorizacaoRequest request
  ) {
    try {
      SalvarDadosAutorizacaoCommand command = new SalvarDadosAutorizacaoCommand(
              id,
              request.getStatusAutorizacao(),
              request.getNumeroGuiaAutorizacao(),
              request.getSenhaAutorizacao(),
              request.getValidadeAutorizacao(),
              request.getTipoAcomodacao(),
              getUsuarioLogado()
      );

      PedidoCirurgico pedido = salvarDadosAutorizacaoUseCase.execute(command);

      return ResponseEntity.ok(UpdateResponse.sucesso(
              "Dados de autorização salvos com sucesso",
              null,
              mapper.toResponse(pedido)
      ));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(UpdateResponse.erro(
              e.getMessage(),
              (String) null
      ));
    }
  }

  private String getUsuarioLogado() {
    return "usuario_atual";
  }

}
