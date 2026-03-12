package org.br.ltec.crmbackend.crm.pedidos.application.useCase;


import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendarConsultaPreCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;

public interface AgendarConsultaPreUseCase {
  PedidoCirurgico execute(AgendarConsultaPreCommand command);
}
