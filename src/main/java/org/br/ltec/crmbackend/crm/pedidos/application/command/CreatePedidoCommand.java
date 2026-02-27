package org.br.ltec.crmbackend.crm.pedidos.application.command;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Procedimento;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreatePedidoCommand {
  private CreatePacienteCommand paciente;
  private String pacienteId;
  private String usuarioCriacao;

  private String medicoSolicitanteNome;
  private String medicoSolicitanteCrm;
  private String medicoSolicitanteEspecialidade;

  private String medicoExecutorNome;
  private String medicoExecutorCrm;
  private String medicoExecutorEspecialidade;

  private String procedimentoCodigoTUSS;
  private String procedimentoDescricao;
  private String procedimentoCategoria;
  private List<Procedimento> procedimentos = new ArrayList<>();

  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;

  private String convenioNome;
  private String convenioNumeroCarteira;
  private LocalDate convenioValidadeCarteira;
  private String convenioTipoPlano;

  private String cidCodigo;
  private String cidDescricao;

  private String cidCodigo2;
  private String cidCodigo3;
  private String cidCodigo4;

  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;

  private String telefonePaciente;
  private String enderecoPaciente;
  private String cpfPaciente;
  private String emailPaciente;
  private String sexoPaciente;

  private LocalDateTime agendamentoDataHora;
  private String agendamentoSala;
  private Integer agendamentoDuracaoEstimada;
  private String agendamentoObservacoes;

  private StatusPedido.Tipo status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;

  private LocalDate dataPedido;

  private List<String> observacoes;
  private List<String> documentosAnexados;

  public CreatePedidoCommand() {
    this.status = StatusPedido.Tipo.PENDENTE;
    this.dataPedido = LocalDate.now();
  }

  // ==================== GETTERS E SETTERS ====================

  public String getPacienteId() { return pacienteId; }
  public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

  public String getUsuarioCriacao() { return usuarioCriacao; }
  public void setUsuarioCriacao(String usuarioCriacao) { this.usuarioCriacao = usuarioCriacao; }

  public String getMedicoSolicitanteNome() { return medicoSolicitanteNome; }
  public void setMedicoSolicitanteNome(String medicoSolicitanteNome) { this.medicoSolicitanteNome = medicoSolicitanteNome; }

  public String getMedicoSolicitanteCrm() { return medicoSolicitanteCrm; }
  public void setMedicoSolicitanteCrm(String medicoSolicitanteCrm) { this.medicoSolicitanteCrm = medicoSolicitanteCrm; }

  public String getMedicoSolicitanteEspecialidade() { return medicoSolicitanteEspecialidade; }
  public void setMedicoSolicitanteEspecialidade(String medicoSolicitanteEspecialidade) { this.medicoSolicitanteEspecialidade = medicoSolicitanteEspecialidade; }

  public String getMedicoExecutorNome() { return medicoExecutorNome; }
  public void setMedicoExecutorNome(String medicoExecutorNome) { this.medicoExecutorNome = medicoExecutorNome; }

  public String getMedicoExecutorCrm() { return medicoExecutorCrm; }
  public void setMedicoExecutorCrm(String medicoExecutorCrm) { this.medicoExecutorCrm = medicoExecutorCrm; }

  public String getMedicoExecutorEspecialidade() { return medicoExecutorEspecialidade; }
  public void setMedicoExecutorEspecialidade(String medicoExecutorEspecialidade) { this.medicoExecutorEspecialidade = medicoExecutorEspecialidade; }

  public String getProcedimentoCodigoTUSS() { return procedimentoCodigoTUSS; }
  public void setProcedimentoCodigoTUSS(String procedimentoCodigoTUSS) { this.procedimentoCodigoTUSS = procedimentoCodigoTUSS; }

  public String getProcedimentoDescricao() { return procedimentoDescricao; }
  public void setProcedimentoDescricao(String procedimentoDescricao) { this.procedimentoDescricao = procedimentoDescricao; }

  public String getProcedimentoCategoria() { return procedimentoCategoria; }
  public void setProcedimentoCategoria(String procedimentoCategoria) { this.procedimentoCategoria = procedimentoCategoria; }

  public List<Procedimento> getProcedimentos() {
    return procedimentos;
  }

  public void setProcedimentos(List<Procedimento> procedimentos) {
    this.procedimentos = procedimentos;
  }

  public String getIndicacaoClinica() { return indicacaoClinica; }
  public void setIndicacaoClinica(String indicacaoClinica) { this.indicacaoClinica = indicacaoClinica; }

  public String getRelatorioPreOperatorio() { return relatorioPreOperatorio; }
  public void setRelatorioPreOperatorio(String relatorioPreOperatorio) { this.relatorioPreOperatorio = relatorioPreOperatorio; }

  public String getOrientacoes() { return orientacoes; }
  public void setOrientacoes(String orientacoes) { this.orientacoes = orientacoes; }

  public String getConvenioNome() { return convenioNome; }
  public void setConvenioNome(String convenioNome) { this.convenioNome = convenioNome; }

  public String getConvenioNumeroCarteira() { return convenioNumeroCarteira; }
  public void setConvenioNumeroCarteira(String convenioNumeroCarteira) { this.convenioNumeroCarteira = convenioNumeroCarteira; }

  public LocalDate getConvenioValidadeCarteira() { return convenioValidadeCarteira; }
  public void setConvenioValidadeCarteira(LocalDate convenioValidadeCarteira) { this.convenioValidadeCarteira = convenioValidadeCarteira; }

  public String getConvenioTipoPlano() { return convenioTipoPlano; }
  public void setConvenioTipoPlano(String convenioTipoPlano) { this.convenioTipoPlano = convenioTipoPlano; }

  public String getCidCodigo() { return cidCodigo; }
  public void setCidCodigo(String cidCodigo) { this.cidCodigo = cidCodigo; }

  public String getCidDescricao() { return cidDescricao; }
  public void setCidDescricao(String cidDescricao) { this.cidDescricao = cidDescricao; }

  public String getCidCodigo2() { return cidCodigo2; }
  public void setCidCodigo2(String cidCodigo2) { this.cidCodigo2 = cidCodigo2; }

  public String getCidCodigo3() { return cidCodigo3; }
  public void setCidCodigo3(String cidCodigo3) { this.cidCodigo3 = cidCodigo3; }

  public String getCidCodigo4() { return cidCodigo4; }
  public void setCidCodigo4(String cidCodigo4) { this.cidCodigo4 = cidCodigo4; }

  public String getNumeroGuia() { return numeroGuia; }
  public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }

  public String getRegistroAns() { return registroAns; }
  public void setRegistroAns(String registroAns) { this.registroAns = registroAns; }

  public String getNumeroGuiaOperadora() { return numeroGuiaOperadora; }
  public void setNumeroGuiaOperadora(String numeroGuiaOperadora) { this.numeroGuiaOperadora = numeroGuiaOperadora; }

  public String getCodigoOperadora() { return codigoOperadora; }
  public void setCodigoOperadora(String codigoOperadora) { this.codigoOperadora = codigoOperadora; }

  public String getNomeContratado() { return nomeContratado; }
  public void setNomeContratado(String nomeContratado) { this.nomeContratado = nomeContratado; }

  public String getCaraterAtendimento() { return caraterAtendimento; }
  public void setCaraterAtendimento(String caraterAtendimento) { this.caraterAtendimento = caraterAtendimento; }

  public String getTipoInternacao() { return tipoInternacao; }
  public void setTipoInternacao(String tipoInternacao) { this.tipoInternacao = tipoInternacao; }

  public String getRegimeInternacao() { return regimeInternacao; }
  public void setRegimeInternacao(String regimeInternacao) { this.regimeInternacao = regimeInternacao; }

  public String getQtdDiariasSolicitadas() { return qtdDiariasSolicitadas; }
  public void setQtdDiariasSolicitadas(String qtdDiariasSolicitadas) { this.qtdDiariasSolicitadas = qtdDiariasSolicitadas; }

  public String getTelefonePaciente() { return telefonePaciente; }
  public void setTelefonePaciente(String telefonePaciente) { this.telefonePaciente = telefonePaciente; }

  public String getEnderecoPaciente() { return enderecoPaciente; }
  public void setEnderecoPaciente(String enderecoPaciente) { this.enderecoPaciente = enderecoPaciente; }

  public String getCpfPaciente() { return cpfPaciente; }
  public void setCpfPaciente(String cpfPaciente) { this.cpfPaciente = cpfPaciente; }

  public String getEmailPaciente() { return emailPaciente; }
  public void setEmailPaciente(String emailPaciente) { this.emailPaciente = emailPaciente; }

  public String getSexoPaciente() { return sexoPaciente; }
  public void setSexoPaciente(String sexoPaciente) { this.sexoPaciente = sexoPaciente; }

  public LocalDateTime getAgendamentoDataHora() { return agendamentoDataHora; }
  public void setAgendamentoDataHora(LocalDateTime agendamentoDataHora) { this.agendamentoDataHora = agendamentoDataHora; }

  public String getAgendamentoSala() { return agendamentoSala; }
  public void setAgendamentoSala(String agendamentoSala) { this.agendamentoSala = agendamentoSala; }

  public Integer getAgendamentoDuracaoEstimada() { return agendamentoDuracaoEstimada; }
  public void setAgendamentoDuracaoEstimada(Integer agendamentoDuracaoEstimada) { this.agendamentoDuracaoEstimada = agendamentoDuracaoEstimada; }

  public String getAgendamentoObservacoes() { return agendamentoObservacoes; }
  public void setAgendamentoObservacoes(String agendamentoObservacoes) { this.agendamentoObservacoes = agendamentoObservacoes; }

  public StatusPedido.Tipo getStatus() { return status; }
  public void setStatus(StatusPedido.Tipo status) { this.status = status; }

  public Prioridade getPrioridade() { return prioridade; }
  public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

  public Lateralidade getLateralidade() { return lateralidade; }
  public void setLateralidade(Lateralidade lateralidade) { this.lateralidade = lateralidade; }

  public LocalDate getDataPedido() { return dataPedido; }
  public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }

  public List<String> getObservacoes() { return observacoes; }
  public void setObservacoes(List<String> observacoes) { this.observacoes = observacoes; }

  public List<String> getDocumentosAnexados() { return documentosAnexados; }
  public void setDocumentosAnexados(List<String> documentosAnexados) { this.documentosAnexados = documentosAnexados; }

  public CreatePacienteCommand getPaciente() { return paciente; }
  public void setPaciente(CreatePacienteCommand paciente) { this.paciente = paciente; }
}