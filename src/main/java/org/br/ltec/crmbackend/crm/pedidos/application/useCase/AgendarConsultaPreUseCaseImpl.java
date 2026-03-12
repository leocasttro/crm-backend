// AgendarConsultaPreUseCaseImpl.java (implementação)
package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.pedidos.application.command.AgendarConsultaPreCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgendarConsultaPreUseCaseImpl implements AgendarConsultaPreUseCase {

  private final PedidoRepository pedidoRepository;

  @Override
  public PedidoCirurgico execute(AgendarConsultaPreCommand command) {
    // 1. Buscar pedido
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
    PedidoCirurgico pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + command.getPedidoId()));

    // 2. Agendar consulta pré (validações dentro do domínio)
    pedido.agendarConsultaPre(
            command.getDataHora(),
            command.getCuidados(),
            command.getObservacoesEspeciais()
    );

    // 3. Salvar e retornar
    return pedidoRepository.salvar(pedido);
  }
}