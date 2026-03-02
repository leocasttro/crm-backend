package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class AnaliseRequest {
  private boolean aprovado;
  private String observacao;
  private String motivoRejeicao;

  @AssertTrue(message = "Motivo da reprovação é obrigatório quando reprovado")
  public boolean isMotivoRejeicaoValido() {
    // Se for aprovado, não valida motivoRejeicao
    if (aprovado) {
      return true;
    }
    // Se for reprovado, motivoRejeicao é obrigatório
    return motivoRejeicao != null && !motivoRejeicao.trim().isEmpty();
  }
}