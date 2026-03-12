// AgendarConsultaPreCommand.java
package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AgendarConsultaPreCommand {
  private String pedidoId;
  private LocalDateTime dataHora;
  private String cuidados;
  private String horarios;
  private String observacoesEspeciais;
  private String usuario;
}