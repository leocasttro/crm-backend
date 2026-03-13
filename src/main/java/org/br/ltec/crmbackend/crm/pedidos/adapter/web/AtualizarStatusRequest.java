package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Data;

@Data
public class AtualizarStatusRequest {
  private String status;
  private String observacao;
}
