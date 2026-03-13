package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoBuilder;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao.DadosAutorizacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    entity.setProcedimentos(pedido.getTodosProcedimentos());

    // 🔥 NOVOS CAMPOS DE TEXTO
    entity.setIndicacaoClinica(pedido.getIndicacaoClinica());
    entity.setRelatorioPreOperatorio(pedido.getRelatorioPreOperatorio());
    entity.setOrientacoes(pedido.getOrientacoes());
    entity.setTelefonePaciente(pedido.getTelefonePaciente());
    entity.setEnderecoPaciente(pedido.getEnderecoPaciente());

    // Convênio
    entity.setConvenioNome(pedido.getConvenio().getNome());
    entity.setConvenioNumeroCarteira(pedido.getConvenio().getNumeroCarteira());
    entity.setConvenioValidadeCarteira(pedido.getConvenio().getValidade());
    entity.setConvenioTipoPlano(pedido.getConvenio().getTipoPlano());

    // CID Principal
    if (pedido.getCid() != null) {
      entity.setCidCodigo(pedido.getCid().getCodigo());
      entity.setCidDescricao(pedido.getCid().getDescricao());
    }

    // 🔥 CIDs Secundários
    entity.setCidCodigo2(pedido.getCidCodigo2());
    entity.setCidCodigo3(pedido.getCidCodigo3());
    entity.setCidCodigo4(pedido.getCidCodigo4());

    // 🔥 Dados da Guia/Internação
    entity.setNumeroGuia(pedido.getNumeroGuia());
    entity.setRegistroAns(pedido.getRegistroAns());
    entity.setNumeroGuiaOperadora(pedido.getNumeroGuiaOperadora());
    entity.setCodigoOperadora(pedido.getCodigoOperadora());
    entity.setNomeContratado(pedido.getNomeContratado());
    entity.setCaraterAtendimento(pedido.getCaraterAtendimento());
    entity.setTipoInternacao(pedido.getTipoInternacao());
    entity.setRegimeInternacao(pedido.getRegimeInternacao());
    entity.setQtdDiariasSolicitadas(pedido.getQtdDiariasSolicitadas());

    // Agendamento
    if (pedido.getAgendamento() != null) {
      entity.setAgendamentoDataHora(pedido.getAgendamento().getDataHora());
      entity.setAgendamentoSala(pedido.getAgendamento().getSala());
      entity.setAgendamentoDuracaoEstimada(pedido.getAgendamento().getDuracaoEstimada());
      entity.setAgendamentoObservacoes(pedido.getAgendamento().getObservacoes());
    }

    if (pedido.getConsultaPreOperatoria() != null) {
      entity.setConsultaPreDataHora(pedido.getConsultaPreOperatoria().getDataHora());
      entity.setConsultaPreCuidados(pedido.getConsultaPreOperatoria().getCuidados());
      entity.setConsultaPreObservacoesEspeciais(pedido.getConsultaPreOperatoria().getObservacoesEspeciais());
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
    if (pedido.getObservacoes() != null && !pedido.getObservacoes().isEmpty()) {
      String observacoesStr = String.join("|", pedido.getObservacoes());
      entity.setObservacoes(observacoesStr);
    }

    // Documentos anexados (serializar como JSON array string)
    if (pedido.getDocumentosAnexados() != null && !pedido.getDocumentosAnexados().isEmpty()) {
      String documentosStr = String.join("|", pedido.getDocumentosAnexados());
      entity.setDocumentosAnexados(documentosStr);
    }

    // Metadados
    entity.setCriadoEm(pedido.getCriadoEm());
    entity.setAtualizadoEm(pedido.getAtualizadoEm());
    entity.setUsuarioCriacao(pedido.getUsuarioCriacao());
    entity.setUsuarioAtualizacao(pedido.getUsuarioAtualizacao());
    entity.setDataPedido(pedido.getDataPedido());

    // 🔥 🔥 NOVO - DADOS DE AUTORIZAÇÃO
    if (pedido.getDadosAutorizacao() != null) {
      DadosAutorizacao dados = pedido.getDadosAutorizacao();

      entity.setStatusAutorizacao(dados.getStatus() != null ? dados.getStatus().getValor() : null);
      entity.setNumeroGuiaAutorizacao(dados.getNumeroGuia() != null ? dados.getNumeroGuia().getValor() : null);
      entity.setSenhaAutorizacao(dados.getSenha() != null ? dados.getSenha().getValor() : null);
      entity.setValidadeAutorizacao(dados.getValidade() != null ? dados.getValidade().getValor() : null);
      entity.setTipoAcomodacao(dados.getTipoAcomodacao() != null ? dados.getTipoAcomodacao().getValor() : null);
    }

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

      // Procedimento principal
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

      // CID Principal
      CID cid = null;
      if (entity.getCidCodigo() != null && !entity.getCidCodigo().trim().isEmpty()) {
        cid = new CID(entity.getCidCodigo(), entity.getCidDescricao());
      }

      // Agendamento
      DataHoraAgendamento agendamento = null;
      if (entity.getAgendamentoDataHora() != null) {
        agendamento = DataHoraAgendamento.fromDatabase(
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

      // 🔥 🔥 DADOS DE AUTORIZAÇÃO
      DadosAutorizacao dadosAutorizacao = null;
      if (entity.getStatusAutorizacao() != null ||
              entity.getNumeroGuiaAutorizacao() != null ||
              entity.getSenhaAutorizacao() != null ||
              entity.getValidadeAutorizacao() != null ||
              entity.getTipoAcomodacao() != null) {

        dadosAutorizacao = new DadosAutorizacao.Builder()
                .status(entity.getStatusAutorizacao())
                .numeroGuia(entity.getNumeroGuiaAutorizacao())
                .senha(entity.getSenhaAutorizacao())
                .validade(entity.getValidadeAutorizacao())
                .tipoAcomodacao(entity.getTipoAcomodacao())
                .build();
      }

      // 🔥 CRIAR O PEDIDO COM O BUILDER
      PedidoCirurgico pedido = new PedidoBuilder()
              .comId(id)
              .comPacienteId(pacienteId)
              .comMedicoSolicitante(medicoSolicitante)
              .comMedicoExecutor(medicoExecutor)
              .comProcedimento(procedimento)
              .comConvenio(convenio)
              .comCid(cid)
              .comAgendamento(agendamento)
              .comStatus(status)
              .comPrioridade(prioridade)
              .comLateralidade(lateralidade)
              .comObservacoes(observacoes)
              .comDocumentosAnexados(documentosAnexados)
              .comCriadoEm(entity.getCriadoEm())
              .comAtualizadoEm(entity.getAtualizadoEm())
              .comUsuarioCriacao(entity.getUsuarioCriacao())
              .comUsuarioAtualizacao(entity.getUsuarioAtualizacao())
              .comDataPedido(entity.getDataPedido())
              .comIndicacaoClinica(entity.getIndicacaoClinica())
              .comRelatorioPreOperatorio(entity.getRelatorioPreOperatorio())
              .comOrientacoes(entity.getOrientacoes())
              .comTelefonePaciente(entity.getTelefonePaciente())
              .comEnderecoPaciente(entity.getEnderecoPaciente())
              .comCidsSecundarios(
                      entity.getCidCodigo2(),
                      entity.getCidCodigo3(),
                      entity.getCidCodigo4()
              )
              .comNumeroGuia(entity.getNumeroGuia())
              .comRegistroAns(entity.getRegistroAns())
              .comNumeroGuiaOperadora(entity.getNumeroGuiaOperadora())
              .comCodigoOperadora(entity.getCodigoOperadora())
              .comNomeContratado(entity.getNomeContratado())
              .comCaraterAtendimento(entity.getCaraterAtendimento())
              .comTipoInternacao(entity.getTipoInternacao())
              .comRegimeInternacao(entity.getRegimeInternacao())
              .comQtdDiariasSolicitadas(entity.getQtdDiariasSolicitadas())
              .dadosAutorizacao(dadosAutorizacao)  // 🔥 NOVO
              .build();

      // ✅ SETAR A LISTA DE PROCEDIMENTOS
      if (entity.getProcedimentos() != null) {
        pedido.setTodosProcedimentos(entity.getProcedimentos());
      }

      // 🔥 SETAR A CONSULTA PRÉ
      if (entity.getConsultaPreDataHora() != null) {
        ConsultaPreOperatoria consultaPre = ConsultaPreOperatoria.fromDatabase(
                entity.getConsultaPreDataHora(),
                entity.getConsultaPreCuidados(),
                entity.getConsultaPreObservacoesEspeciais()
        );
        pedido.setConsultaPreOperatoria(consultaPre);
      }

      return pedido;

    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter entidade para domínio: " + e.getMessage(), e);
    }
  }
}