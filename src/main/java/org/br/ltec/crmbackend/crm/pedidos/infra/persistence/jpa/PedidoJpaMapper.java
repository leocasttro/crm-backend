package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoJpaMapper {

  public PedidoJpaEntity toEntity(PedidoCirurgico pedido) {
    if (pedido == null) {
      return null;
    }

    PedidoJpaEntity entity = new PedidoJpaEntity();

    // ID
    if (pedido.getId() != null) {
      entity.setId(pedido.getId().getValue());
    }

    // Paciente
    entity.setPacienteId(pedido.getPacienteId().getValue());

    // Médico Solicitante
    entity.setMedicoSolicitanteNome(pedido.getMedicoSolicitante().getNome());
    entity.setMedicoSolicitanteCrm(pedido.getMedicoSolicitante().getCrm());
    entity.setMedicoSolicitanteEspecialidade(pedido.getMedicoSolicitante().getEspecialidade());

    // Médico Executor
    if (pedido.getMedicoExecutor() != null) {
      entity.setMedicoExecutorNome(pedido.getMedicoExecutor().getNome());
      entity.setMedicoExecutorCrm(pedido.getMedicoExecutor().getCrm());
      entity.setMedicoExecutorEspecialidade(pedido.getMedicoExecutor().getEspecialidade());
    }

    // Procedimento
    entity.setProcedimentoCodigoTuss(pedido.getProcedimento().getCodigoTUSS());
    entity.setProcedimentoDescricao(pedido.getProcedimento().getDescricao());
    entity.setProcedimentoCategoria(pedido.getProcedimento().getCategoria());

    // Convênio
    entity.setConvenioNome(pedido.getConvenio().getNome());
    entity.setConvenioNumeroCarteira(pedido.getConvenio().getNumeroCarteira());
    entity.setConvenioValidadeCarteira(pedido.getConvenio().getValidadeCarteira());
    entity.setConvenioTipoPlano(pedido.getConvenio().getTipoPlano());

    // CID
    if (pedido.getCid() != null) {
      entity.setCidCodigo(pedido.getCid().getCodigo());
      entity.setCidDescricao(pedido.getCid().getDescricao());
    }

    // Agendamento
    if (pedido.getAgendamento() != null) {
      entity.setAgendamentoDataHora(pedido.getAgendamento().getDataHora());
      entity.setAgendamentoSala(pedido.getAgendamento().getSala());
      entity.setAgendamentoDuracaoEstimada(pedido.getAgendamento().getDuracaoEstimada());
      entity.setAgendamentoObservacoes(pedido.getAgendamento().getObservacoes());
    }

    // Status
    entity.setStatus(pedido.getStatus().getTipo().name());
    entity.setStatusObservacao(pedido.getStatus().getObservacao());
    entity.setStatusUsuarioAlteracao(pedido.getStatus().getUsuarioAlteracao());

    // Prioridade
    entity.setPrioridade(pedido.getPrioridade().getTipo().name());
    entity.setPrioridadeJustificativa(pedido.getPrioridade().getJustificativa());

    // Lateralidade
    entity.setLateralidade(pedido.getLateralidade().getTipo().name());

    // Observações (serializar como JSON array string)
    if (!pedido.getObservacoes().isEmpty()) {
      String observacoesStr = String.join("|", pedido.getObservacoes());
      entity.setObservacoes(observacoesStr);
    }

    // Documentos anexados (serializar como JSON array string)
    if (!pedido.getDocumentosAnexados().isEmpty()) {
      String documentosStr = String.join("|", pedido.getDocumentosAnexados());
      entity.setDocumentosAnexados(documentosStr);
    }

    // Metadados
    entity.setCriadoEm(pedido.getCriadoEm());
    entity.setAtualizadoEm(pedido.getAtualizadoEm());
    entity.setUsuarioCriacao(pedido.getUsuarioCriacao());
    entity.setUsuarioAtualizacao(pedido.getUsuarioAtualizacao());
    entity.setDataPedido(pedido.getDataPedido());

    return entity;
  }

  public PedidoCirurgico toDomain(PedidoJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    try {
      // Criar Value Objects
      PedidoId id = PedidoId.fromString(entity.getId().toString());
      PacienteId pacienteId = PacienteId.fromString(entity.getPacienteId().toString());

      // Médico solicitante
      Medico medicoSolicitante = new Medico(
              entity.getMedicoSolicitanteNome(),
              entity.getMedicoSolicitanteCrm(),
              entity.getMedicoSolicitanteEspecialidade()
      );

      // Médico executor
      Medico medicoExecutor = null;
      if (entity.getMedicoExecutorNome() != null && !entity.getMedicoExecutorNome().trim().isEmpty()) {
        medicoExecutor = new Medico(
                entity.getMedicoExecutorNome(),
                entity.getMedicoExecutorCrm(),
                entity.getMedicoExecutorEspecialidade()
        );
      }

      // Procedimento
      Procedimento procedimento = new Procedimento(
              entity.getProcedimentoCodigoTuss(),
              entity.getProcedimentoDescricao(),
              entity.getProcedimentoCategoria()
      );

      // Convênio
      Convenio convenio = new Convenio(
              entity.getConvenioNome(),
              entity.getConvenioNumeroCarteira(),
              entity.getConvenioValidadeCarteira(),
              entity.getConvenioTipoPlano()
      );

      // CID
      CID cid = null;
      if (entity.getCidCodigo() != null && !entity.getCidCodigo().trim().isEmpty()) {
        cid = new CID(entity.getCidCodigo(), entity.getCidDescricao());
      }

      // Agendamento
      DataHoraAgendamento agendamento = null;
      if (entity.getAgendamentoDataHora() != null) {
        agendamento = new DataHoraAgendamento(
                entity.getAgendamentoDataHora(),
                entity.getAgendamentoSala(),
                entity.getAgendamentoDuracaoEstimada(),
                entity.getAgendamentoObservacoes()
        );
      }

      // Status
      StatusPedido status = new StatusPedido(
              StatusPedido.Tipo.valueOf(entity.getStatus()),
              entity.getStatusObservacao(),
              entity.getStatusUsuarioAlteracao()
      );

      // Prioridade
      Prioridade prioridade = new Prioridade(
              Prioridade.Tipo.valueOf(entity.getPrioridade()),
              entity.getPrioridadeJustificativa()
      );

      // Lateralidade
      Lateralidade lateralidade = new Lateralidade(Lateralidade.Tipo.valueOf(entity.getLateralidade()));

      // Observações
      List<String> observacoes = new ArrayList<>();
      if (entity.getObservacoes() != null && !entity.getObservacoes().trim().isEmpty()) {
        observacoes = Arrays.asList(entity.getObservacoes().split("\\|"));
      }

      // Documentos anexados
      List<String> documentosAnexados = new ArrayList<>();
      if (entity.getDocumentosAnexados() != null && !entity.getDocumentosAnexados().trim().isEmpty()) {
        documentosAnexados = Arrays.asList(entity.getDocumentosAnexados().split("\\|"));
      }

      // Criar pedido
      return new PedidoCirurgico(
              id,
              pacienteId,
              medicoSolicitante,
              medicoExecutor,
              procedimento,
              convenio,
              cid,
              agendamento,
              status,
              prioridade,
              lateralidade,
              observacoes,
              documentosAnexados,
              entity.getCriadoEm(),
              entity.getAtualizadoEm(),
              entity.getUsuarioCriacao(),
              entity.getUsuarioAtualizacao(),
              entity.getDataPedido()
      );

    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter entidade para domínio: " + e.getMessage(), e);
    }
  }
}