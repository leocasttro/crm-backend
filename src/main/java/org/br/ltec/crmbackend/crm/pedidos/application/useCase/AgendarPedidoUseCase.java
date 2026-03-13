package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendamentoPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;

public interface AgendarPedidoUseCase {
  PedidoCirurgico execute(AgendamentoPedidoCommand command);
}