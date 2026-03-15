// AjustarAgendamentoUseCase.java
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
public class AjustarAgendamentoUseCase {

  private final PedidoRepository pedidoRepository;

  public PedidoCirurgico execute(AgendamentoPedidoCommand command) {
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());

    var pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + command.getPedidoId()));

    DataHoraAgendamento dataAgendamento = DataHoraAgendamento.criar(
            command.getDataAgendamento(),
            command.getLocal(),
            command.getHospital(),
            command.getFornecedor(),
            command.getRiscoCirurgico(),
            command.getDuracaoEstimada()
    );

    pedido.ajustarAgendamento(dataAgendamento, "usuario");

    if (command.getObservacao() != null && !command.getObservacao().trim().isEmpty()) {
      pedido.adicionarObservacao(
              "Ajuste no agendamento: " + command.getObservacao(),
              "usario"
      );
    }

    return pedidoRepository.salvar(pedido);
  }
}