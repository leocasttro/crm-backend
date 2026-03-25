package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusCommand {
  private String pedidoId;
  private String novoStatus;
  private String observacao;
  private String usuario;
}