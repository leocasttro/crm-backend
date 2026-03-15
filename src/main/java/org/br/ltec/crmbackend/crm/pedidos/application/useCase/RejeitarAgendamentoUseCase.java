// RejeitarAgendamentoUseCase.java
package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RejeitarAgendamentoUseCase {

  private final PedidoRepository pedidoRepository;

  public PedidoCirurgico execute(String pedidoId, String usuario, String motivo) {
    PedidoId id = PedidoId.fromString(pedidoId);

    var pedido = pedidoRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

    pedido.rejeitarAgendamento(usuario, motivo);

    return pedidoRepository.salvar(pedido);
  }
}