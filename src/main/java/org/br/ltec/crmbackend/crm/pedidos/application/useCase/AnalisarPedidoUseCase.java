package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.AnalisarPedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalisarPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public AnalisarPedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  @Transactional
  public ResultadoOperacao<PedidoCirurgico> execute(AnalisarPedidoCommand command) {
    try {
      // 1. Buscar pedido
      PedidoCirurgico pedido = pedidoRepository.buscarPorId(
              PedidoId.fromString(command.getPedidoId())
      ).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

      // 2. Domain puro - valida e retorna o novo status
      StatusPedido.Tipo novoStatus = pedido.analisar(
              command.isAprovado(),
              command.getMotivoRejeicao()
      );

      // 3. Aplicar as mudanças baseado na análise
      if (command.isAprovado()) {
        // Aprovado: mantém EM_ANALISE, só adiciona observação
        pedido.adicionarObservacao(
                "Análise aprovada: " + (command.getObservacao() != null ? command.getObservacao() : ""),
                command.getUsuario()
        );
      } else {
        // Reprovado: usa o método existente rejeitar
        pedido.rejeitar(command.getUsuario(), command.getMotivoRejeicao());
      }

      // 4. Salvar e retornar
      PedidoCirurgico pedidoAtualizado = pedidoRepository.salvar(pedido);

      String mensagem = command.isAprovado() ?
              "Pedido aprovado com sucesso" :
              "Pedido reprovado com sucesso";

      return ResultadoOperacao.sucesso(pedidoAtualizado, mensagem);

    } catch (IllegalStateException | IllegalArgumentException e) {
      return ResultadoOperacao.erro(
              "Erro na análise do pedido",
              e.getMessage()
      );
    } catch (Exception e) {
      return ResultadoOperacao.erro(
              "Erro inesperado",
              "Ocorreu um erro ao processar a análise: " + e.getMessage()
      );
    }
  }
}