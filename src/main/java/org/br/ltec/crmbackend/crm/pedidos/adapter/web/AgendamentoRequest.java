package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.DataHoraAgendamento;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequest {

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime dataAgendamento;
  private String observacao;
}