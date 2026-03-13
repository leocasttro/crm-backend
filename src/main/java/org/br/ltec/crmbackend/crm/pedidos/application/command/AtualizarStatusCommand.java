package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.Data;

@Data
public class AtualizarStatusCommand {
  private String pedidoId;
  private String novoStatus;
  private String observacao;
  private String usuario;
}