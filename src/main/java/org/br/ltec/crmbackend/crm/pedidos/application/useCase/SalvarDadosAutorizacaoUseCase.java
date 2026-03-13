package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.br.ltec.crmbackend.crm.pedidos.application.command.SalvarDadosAutorizacaoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao.DadosAutorizacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalvarDadosAutorizacaoUseCase {

  private final PedidoRepository pedidoRepository;
  private final FindPedidoUseCase findPedidoUseCase;

  @Transactional
  public PedidoCirurgico execute(SalvarDadosAutorizacaoCommand command) {
    log.info("Iniciando salvamento de dados de autorização para pedido: {}", command.getPedidoId());

    // Converter UUID para String
    String pedidoIdString = command.getPedidoId().toString();
    log.debug("Pedido ID convertido: {}", pedidoIdString);

    // Buscar o pedido
    PedidoCirurgico pedido = findPedidoUseCase.findById(pedidoIdString)
            .orElseThrow(() -> {
              log.error("Pedido não encontrado com id: {}", pedidoIdString);
              return new RuntimeException("Pedido não encontrado com id: " + pedidoIdString);
            });

    log.info("Pedido encontrado: {}", pedido.getId());

    // Construir os dados de autorização
    log.debug("Construindo DadosAutorizacao com: status={}, numeroGuia={}, senha={}, validade={}, tipoAcomodacao={}",
            command.getStatusAutorizacao(),
            command.getNumeroGuia(),
            command.getSenhaAutorizacao() != null ? "***" : null,
            command.getValidadeAutorizacao(),
            command.getTipoAcomodacao());

    DadosAutorizacao dadosAutorizacao = new DadosAutorizacao.Builder()
            .status(command.getStatusAutorizacao())
            .numeroGuia(command.getNumeroGuia())
            .senha(command.getSenhaAutorizacao())
            .validade(command.getValidadeAutorizacao())
            .tipoAcomodacao(command.getTipoAcomodacao())
            .build();

    log.debug("DadosAutorizacao construído: {}", dadosAutorizacao);

    // Atualizar o pedido
    try {
      pedido.atualizarDadosAutorizacao(dadosAutorizacao);
      log.info("Dados de autorização atualizados no pedido");
    } catch (Exception e) {
      log.error("Erro ao atualizar dados de autorização no pedido: {}", e.getMessage(), e);
      throw e;
    }

    // Salvar
    PedidoCirurgico saved = pedidoRepository.salvar(pedido);
    log.info("Pedido salvo com sucesso");

    return saved;
  }
}