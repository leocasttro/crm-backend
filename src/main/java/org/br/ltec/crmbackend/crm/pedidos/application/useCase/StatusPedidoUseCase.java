package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.StatusPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatusPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public StatusPedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  public PedidoCirurgico execute(StatusPedidoCommand command) {
    // Buscar pedido
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
    PedidoCirurgico pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + command.getPedidoId()));

    StatusPedido.Tipo novoTipo = StatusPedido.Tipo.fromString(command.getStatus());

    // Executar transição de status apropriada
    switch (novoTipo) {
      case PENDENTE:
        pedido.enviarParaAnalise(command.getUsuario());
        break;
      case EM_ANALISE:
        pedido.iniciarAnalise(command.getUsuario());
        break;
      case CONFIRMADO:
        pedido.confirmar(command.getUsuario(), command.getObservacao());
        break;
      case EM_PROGRESSO:
        pedido.iniciarProcedimento(command.getUsuario());
        break;
      case REALIZADO:
        pedido.finalizar(command.getUsuario(), command.getObservacao());
        break;
      case CANCELADO:
        pedido.cancelar(command.getUsuario(), command.getObservacao());
        break;
      case REJEITADO:
        pedido.rejeitar(command.getUsuario(), command.getObservacao());
        break;
      case AGENDADO:
        throw new IllegalStateException("Use o caso de uso de agendamento para alterar status para AGENDADO");
      case RASCUNHO:
        throw new IllegalStateException("Não é possível voltar o pedido para status RASCUNHO");
      default:
        throw new IllegalArgumentException("Status não suportado: " + command.getStatus());
    }

    // Salvar pedido atualizado
    return pedidoRepository.salvar(pedido);
  }
}