package org.br.ltec.crmbackend.crm.pedidos.domain.model;

import lombok.Builder;
import lombok.Data;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.OpmeItemId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;

import java.util.List;

@Data
@Builder
public class OpmeItem {
  private final OpmeItemId id;
  private final PedidoId pedidoId;
  private String descricao;
  private Integer quantidade;
  private List<String> marcasAceitas;
  private String marcasNegadas;
  private String observacao;
}