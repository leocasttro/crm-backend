package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendamentoPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.DataHoraAgendamento;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendarPedidoUseCaseImpl implements AgendarPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  @Override
  public PedidoCirurgico execute(AgendamentoPedidoCommand command) {
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());

    var pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + command.getPedidoId()));

    DataHoraAgendamento dataAgendamento = DataHoraAgendamento.criar(command.getDataAgendamento());

    pedido.agendar(dataAgendamento, command.getObservacao());

    return pedidoRepository.salvar(pedido);
  }
}