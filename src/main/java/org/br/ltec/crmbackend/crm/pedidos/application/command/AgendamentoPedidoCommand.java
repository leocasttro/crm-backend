package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.Builder;
import lombok.Value;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.DataHoraAgendamento;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AgendamentoPedidoCommand {
  String pedidoId;
  LocalDateTime dataAgendamento;
  String observacao;
}