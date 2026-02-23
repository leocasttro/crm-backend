package org.br.ltec.crmbackend.crm.pedidos.application.command;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreatePedidoCommand {
  private CreatePacienteCommand paciente;
  // Campo existente - para usar paciente existente
  private String pacienteId;

  private String usuarioCriacao;

  // Médico Solicitante
  private String medicoSolicitanteNome;
  private String medicoSolicitanteCrm;
  private String medicoSolicitanteEspecialidade;

  // Médico Executor (opcional)
  private String medicoExecutorNome;
  private String medicoExecutorCrm;
  private String medicoExecutorEspecialidade;

  // Procedimento
  private String procedimentoCodigoTUSS;
  private String procedimentoDescricao;
  private String procedimentoCategoria;

  // Convênio
  private String convenioNome;
  private String convenioNumeroCarteira;
  private LocalDate convenioValidadeCarteira;
  private String convenioTipoPlano;

  // CID (opcional)
  private String cidCodigo;
  private String cidDescricao;

  // Agendamento (opcional)
  private LocalDateTime agendamentoDataHora;
  private String agendamentoSala;
  private Integer agendamentoDuracaoEstimada;
  private String agendamentoObservacoes;

  // Status e prioridade
  private StatusPedido.Tipo status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;

  // Data do pedido
  private LocalDate dataPedido;

  // Observações
  private List<String> observacoes;

  // Documentos anexados (URLs ou caminhos)
  private List<String> documentosAnexados;

  // Construtores
  public CreatePedidoCommand() {
    this.status = StatusPedido.Tipo.PENDENTE;
    this.dataPedido = LocalDate.now();
  }

  // Getters e Setters
  public String getPacienteId() {
    return pacienteId;
  }

  public void setPacienteId(String pacienteId) {
    this.pacienteId = pacienteId;
  }

  public String getUsuarioCriacao() {
    return usuarioCriacao;
  }

  public void setUsuarioCriacao(String usuarioCriacao) {
    this.usuarioCriacao = usuarioCriacao;
  }

  public String getMedicoSolicitanteNome() {
    return medicoSolicitanteNome;
  }

  public void setMedicoSolicitanteNome(String medicoSolicitanteNome) {
    this.medicoSolicitanteNome = medicoSolicitanteNome;
  }

  public String getMedicoSolicitanteCrm() {
    return medicoSolicitanteCrm;
  }

  public void setMedicoSolicitanteCrm(String medicoSolicitanteCrm) {
    this.medicoSolicitanteCrm = medicoSolicitanteCrm;
  }

  public String getMedicoSolicitanteEspecialidade() {
    return medicoSolicitanteEspecialidade;
  }

  public void setMedicoSolicitanteEspecialidade(String medicoSolicitanteEspecialidade) {
    this.medicoSolicitanteEspecialidade = medicoSolicitanteEspecialidade;
  }

  public String getMedicoExecutorNome() {
    return medicoExecutorNome;
  }

  public void setMedicoExecutorNome(String medicoExecutorNome) {
    this.medicoExecutorNome = medicoExecutorNome;
  }

  public String getMedicoExecutorCrm() {
    return medicoExecutorCrm;
  }

  public void setMedicoExecutorCrm(String medicoExecutorCrm) {
    this.medicoExecutorCrm = medicoExecutorCrm;
  }

  public String getMedicoExecutorEspecialidade() {
    return medicoExecutorEspecialidade;
  }

  public void setMedicoExecutorEspecialidade(String medicoExecutorEspecialidade) {
    this.medicoExecutorEspecialidade = medicoExecutorEspecialidade;
  }

  public String getProcedimentoCodigoTUSS() {
    return procedimentoCodigoTUSS;
  }

  public void setProcedimentoCodigoTUSS(String procedimentoCodigoTUSS) {
    this.procedimentoCodigoTUSS = procedimentoCodigoTUSS;
  }

  public String getProcedimentoDescricao() {
    return procedimentoDescricao;
  }

  public void setProcedimentoDescricao(String procedimentoDescricao) {
    this.procedimentoDescricao = procedimentoDescricao;
  }

  public String getProcedimentoCategoria() {
    return procedimentoCategoria;
  }

  public void setProcedimentoCategoria(String procedimentoCategoria) {
    this.procedimentoCategoria = procedimentoCategoria;
  }

  public String getConvenioNome() {
    return convenioNome;
  }

  public void setConvenioNome(String convenioNome) {
    this.convenioNome = convenioNome;
  }

  public String getConvenioNumeroCarteira() {
    return convenioNumeroCarteira;
  }

  public void setConvenioNumeroCarteira(String convenioNumeroCarteira) {
    this.convenioNumeroCarteira = convenioNumeroCarteira;
  }

  public LocalDate getConvenioValidadeCarteira() {
    return convenioValidadeCarteira;
  }

  public void setConvenioValidadeCarteira(LocalDate convenioValidadeCarteira) {
    this.convenioValidadeCarteira = convenioValidadeCarteira;
  }

  public String getConvenioTipoPlano() {
    return convenioTipoPlano;
  }

  public void setConvenioTipoPlano(String convenioTipoPlano) {
    this.convenioTipoPlano = convenioTipoPlano;
  }

  public String getCidCodigo() {
    return cidCodigo;
  }

  public void setCidCodigo(String cidCodigo) {
    this.cidCodigo = cidCodigo;
  }

  public String getCidDescricao() {
    return cidDescricao;
  }

  public void setCidDescricao(String cidDescricao) {
    this.cidDescricao = cidDescricao;
  }

  public LocalDateTime getAgendamentoDataHora() {
    return agendamentoDataHora;
  }

  public void setAgendamentoDataHora(LocalDateTime agendamentoDataHora) {
    this.agendamentoDataHora = agendamentoDataHora;
  }

  public String getAgendamentoSala() {
    return agendamentoSala;
  }

  public void setAgendamentoSala(String agendamentoSala) {
    this.agendamentoSala = agendamentoSala;
  }

  public Integer getAgendamentoDuracaoEstimada() {
    return agendamentoDuracaoEstimada;
  }

  public void setAgendamentoDuracaoEstimada(Integer agendamentoDuracaoEstimada) {
    this.agendamentoDuracaoEstimada = agendamentoDuracaoEstimada;
  }

  public String getAgendamentoObservacoes() {
    return agendamentoObservacoes;
  }

  public void setAgendamentoObservacoes(String agendamentoObservacoes) {
    this.agendamentoObservacoes = agendamentoObservacoes;
  }

  public StatusPedido.Tipo getStatus() {
    return status;
  }

  public void setStatus(StatusPedido.Tipo status) {
    this.status = status;
  }

  public Prioridade getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Prioridade prioridade) {
    this.prioridade = prioridade;
  }

  public Lateralidade getLateralidade() {
    return lateralidade;
  }

  public void setLateralidade(Lateralidade lateralidade) {
    this.lateralidade = lateralidade;
  }

  public LocalDate getDataPedido() {
    return dataPedido;
  }

  public void setDataPedido(LocalDate dataPedido) {
    this.dataPedido = dataPedido;
  }

  public List<String> getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(List<String> observacoes) {
    this.observacoes = observacoes;
  }

  public List<String> getDocumentosAnexados() {
    return documentosAnexados;
  }

  public void setDocumentosAnexados(List<String> documentosAnexados) {
    this.documentosAnexados = documentosAnexados;
  }

  public CreatePacienteCommand getPaciente() {
    return paciente;
  }

  public void setPaciente(CreatePacienteCommand paciente) {
    this.paciente = paciente;
  }
}