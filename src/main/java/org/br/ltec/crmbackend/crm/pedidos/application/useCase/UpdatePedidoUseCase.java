package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.UpdatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UpdatePedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public UpdatePedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  public PedidoCirurgico execute(UpdatePedidoCommand command) {
    // Buscar pedido existente
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
    PedidoCirurgico pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + command.getPedidoId()));

    // Atualizar médico executor se fornecido
    if (command.getMedicoExecutorNome() != null && !command.getMedicoExecutorNome().trim().isEmpty()) {
      Medico medicoExecutor = new Medico(
              command.getMedicoExecutorNome(),
              command.getMedicoExecutorCrm(),
              command.getMedicoExecutorEspecialidade()
      );
      pedido.atualizarMedicoExecutor(medicoExecutor, command.getUsuarioAtualizacao());
    }

    // Atualizar procedimento se fornecido
    if (command.getProcedimentoCodigoTUSS() != null && !command.getProcedimentoCodigoTUSS().trim().isEmpty()) {
      Procedimento procedimento = new Procedimento(
              command.getProcedimentoCodigoTUSS(),
              command.getProcedimentoDescricao(),
              command.getProcedimentoCategoria()
      );
      pedido.atualizarProcedimento(procedimento, command.getUsuarioAtualizacao());
    }

    // Atualizar convênio se fornecido
    if (command.getConvenioNome() != null && !command.getConvenioNome().trim().isEmpty()) {
      Convenio convenio = new Convenio(
              command.getConvenioNome(),
              command.getConvenioNumeroCarteira(),
              command.getConvenioValidadeCarteira(),
              command.getConvenioTipoPlano()
      );
      pedido.atualizarConvenio(convenio, command.getUsuarioAtualizacao());
    }

    // Atualizar CID se fornecido
    if (command.getCidCodigo() != null && !command.getCidCodigo().trim().isEmpty()) {
      CID cid = new CID(command.getCidCodigo(), command.getCidDescricao());
      pedido.atualizarCID(cid, command.getUsuarioAtualizacao());
    }

    // Atualizar agendamento se fornecido
    if (command.getAgendamentoDataHora() != null) {
      DataHoraAgendamento agendamento = new DataHoraAgendamento(
              command.getAgendamentoDataHora(),
              command.getAgendamentoSala(),
              command.getAgendamentoDuracaoEstimada(),
              command.getAgendamentoObservacoes()
      );
      pedido.atualizarAgendamento(agendamento, command.getUsuarioAtualizacao());
    }

    // Atualizar status se fornecido
    if (command.getStatus() != null && !command.getStatus().trim().isEmpty()) {
      atualizarStatusPedido(pedido, command.getStatus(),
              command.getStatusObservacao(), command.getUsuarioAtualizacao());
    }

    // Atualizar prioridade se fornecida
    if (command.getPrioridade() != null) {
      pedido.atualizarPrioridade(command.getPrioridade(), command.getUsuarioAtualizacao());
    }

    // Atualizar lateralidade se fornecida
    if (command.getLateralidade() != null) {
      pedido.atualizarLateralidade(command.getLateralidade(), command.getUsuarioAtualizacao());
    }

    // Adicionar observações se fornecidas
    if (command.getObservacoes() != null) {
      command.getObservacoes().forEach(obs ->
              pedido.adicionarObservacao(obs, command.getUsuarioAtualizacao()));
    }

    // Adicionar documentos se fornecidos
    if (command.getDocumentosAnexados() != null) {
      command.getDocumentosAnexados().forEach(doc ->
              pedido.adicionarDocumento(doc, command.getUsuarioAtualizacao()));
    }

    // Salvar pedido atualizado
    return pedidoRepository.salvar(pedido);
  }

  private void atualizarStatusPedido(PedidoCirurgico pedido, String novoStatusStr,
                                     String observacao, String usuario) {
    StatusPedido.Tipo novoTipo = StatusPedido.Tipo.fromString(novoStatusStr);

    switch (novoTipo) {
      case PENDENTE:
        pedido.enviarParaAnalise(usuario);
        break;
      case EM_ANALISE:
        pedido.iniciarAnalise(usuario);
        break;
      case AGENDADO:
        // Para agendar, precisa ter DataHoraAgendamento configurada
        throw new IllegalStateException("Use o caso de uso de agendamento para alterar status para AGENDADO");
      case CONFIRMADO:
        pedido.confirmar(usuario, observacao);
        break;
      case EM_PROGRESSO:
        pedido.iniciarProcedimento(usuario);
        break;
      case REALIZADO:
        pedido.finalizar(usuario, observacao);
        break;
      case CANCELADO:
        pedido.cancelar(usuario, observacao);
        break;
      case REJEITADO:
        pedido.rejeitar(usuario, observacao);
        break;
      case RASCUNHO:
        // Não pode voltar para rascunho
        throw new IllegalStateException("Não é possível voltar o pedido para status RASCUNHO");
      default:
        throw new IllegalArgumentException("Status não suportado: " + novoStatusStr);
    }
  }
}