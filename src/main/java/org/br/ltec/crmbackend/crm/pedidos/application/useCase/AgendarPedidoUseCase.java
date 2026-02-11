package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendamentoPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.DataHoraAgendamento;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgendarPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public AgendarPedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  public PedidoCirurgico execute(AgendamentoPedidoCommand command) {
    // Buscar pedido
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
    PedidoCirurgico pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + command.getPedidoId()));

    // Criar agendamento
    DataHoraAgendamento agendamento = new DataHoraAgendamento(
            command.getDataHora(),
            command.getSala(),
            command.getDuracaoEstimada(),
            command.getObservacoes()
    );

    // Agendar pedido
    pedido.agendar(agendamento, command.getUsuario());

    // Salvar pedido atualizado
    return pedidoRepository.salvar(pedido);
  }
}