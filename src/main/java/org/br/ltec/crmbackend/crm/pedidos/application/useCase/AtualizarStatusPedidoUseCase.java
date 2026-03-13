package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.AtualizarStatusCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarStatusPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public AtualizarStatusPedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  @Transactional
  public ResultadoOperacao<PedidoCirurgico> execute(AtualizarStatusCommand command) {
    try {
      // 1. Busca o pedido
      PedidoCirurgico pedido = pedidoRepository.buscarPorId(
              PedidoId.fromString(command.getPedidoId())
      ).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

      // 2. Converte a string para enum (valida se é um status válido)
      StatusPedido.Tipo novoStatus;
      try {
        novoStatus = StatusPedido.Tipo.valueOf(command.getNovoStatus());
      } catch (IllegalArgumentException e) {
        return ResultadoOperacao.erro("Status inválido: " + command.getNovoStatus());
      }

      // 3. Usa o método genérico do domínio para atualizar
      pedido.atualizarStatus(novoStatus, command.getUsuario(), command.getObservacao());

      // 4. Salva
      PedidoCirurgico pedidoAtualizado = pedidoRepository.salvar(pedido);

      return ResultadoOperacao.sucesso(
              pedidoAtualizado,
              "Status atualizado para: " + novoStatus
      );

    } catch (IllegalStateException e) {
      return ResultadoOperacao.erro("Regra de negócio: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResultadoOperacao.erro("Erro na requisição: " + e.getMessage());
    } catch (Exception e) {
      return ResultadoOperacao.erro("Erro interno: " + e.getMessage());
    }
  }
}