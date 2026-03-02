package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IniciarAnaliseUseCase {

  private final PedidoRepository pedidoRepository;

  public IniciarAnaliseUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  @Transactional
  public ResultadoOperacao<PedidoCirurgico> execute(String pedidoId, String usuario) {
    try {
      PedidoCirurgico pedido = pedidoRepository.buscarPorId(PedidoId.fromString(pedidoId))
              .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

      // Validação de negócio no domínio
      if (pedido.getStatus().getTipo() != StatusPedido.Tipo.PENDENTE) {
        return ResultadoOperacao.erro(
                "Não é possível iniciar análise. Status atual: " + pedido.getStatus().getTipo() + ". Esperado: PENDENTE"
        );
      }

      pedido.iniciarAnalise(usuario);

      PedidoCirurgico pedidoAtualizado = pedidoRepository.salvar(pedido);

      return ResultadoOperacao.sucesso(pedidoAtualizado, "Análise iniciada com sucesso");

    } catch (IllegalArgumentException e) {
      return ResultadoOperacao.erro("Erro ao iniciar análise: " + e.getMessage());
    }
  }
}