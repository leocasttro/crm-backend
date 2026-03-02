package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.Data;

@Data
public class AnalisarPedidoCommand {
  private String pedidoId;
  private String usuario;
  private boolean aprovado;
  private String observacao;
  private String motivoRejeicao;
}