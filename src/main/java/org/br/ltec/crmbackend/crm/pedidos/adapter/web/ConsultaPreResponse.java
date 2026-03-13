// ConsultaPreResponse.java
package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Data;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ConsultaPreOperatoria;
import java.time.LocalDateTime;

@Data
@Builder
public class ConsultaPreResponse {
  private LocalDateTime dataHora;
  private String cuidados;
  private String observacoesEspeciais;


  public static ConsultaPreResponse fromDomain(ConsultaPreOperatoria consulta) {
    if (consulta == null) return null;

    return ConsultaPreResponse.builder()
            .dataHora(consulta.getDataHora())
            .cuidados(consulta.getCuidados())
            .observacoesEspeciais(consulta.getObservacoesEspeciais())
            .build();
  }
}