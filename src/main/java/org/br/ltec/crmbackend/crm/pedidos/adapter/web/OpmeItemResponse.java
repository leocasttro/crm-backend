package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpmeItemResponse {
  private String id;
  private String descricao;
  private Integer quantidade;
  private List<String> marcasAceitas;
  private String marcasNegadas;
  private String observacao;
}