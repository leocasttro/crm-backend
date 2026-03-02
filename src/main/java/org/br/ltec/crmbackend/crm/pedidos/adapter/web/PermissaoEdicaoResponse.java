package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissaoEdicaoResponse {
  private boolean podeEditar;
  private String statusAtual;
  private String mensagem;

  public PermissaoEdicaoResponse(boolean podeEditar, String statusAtual) {
    this.podeEditar = podeEditar;
    this.statusAtual = statusAtual;
    this.mensagem = podeEditar ?
            "Pedido pode ser editado" :
            "Pedido não pode ser editado no status: " + statusAtual;
  }
}