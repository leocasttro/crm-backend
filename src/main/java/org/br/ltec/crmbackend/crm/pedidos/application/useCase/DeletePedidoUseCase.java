package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeletePedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public DeletePedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  public void execute(String id) {
    PedidoId pedidoId = PedidoId.fromString(id);

    // Verificar se pedido existe
    boolean existe = pedidoRepository.existePorId(pedidoId);
    if (!existe) {
      throw new IllegalArgumentException("Pedido não encontrado com ID: " + id);
    }

    // Excluir pedido
    pedidoRepository.excluir(pedidoId);
  }
}