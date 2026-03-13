package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SalvarDadosAutorizacaoRequest {
  private String statusAutorizacao;
  private String numeroGuia;
  private String senhaAutorizacao;
  private LocalDate validadeAutorizacao;
  private String tipoAcomodacao;
}
