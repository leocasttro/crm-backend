// AgendarConsultaPreRequest.java
package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AgendarConsultaPreRequest {
  private LocalDateTime dataHora;
  private String cuidados;
  private String horarios;
  private String observacoesEspeciais;
}