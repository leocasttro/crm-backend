package org.br.ltec.crmbackend.crm.pedidos.domain.model;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
// import org.springframework.stereotype.Component; // REMOVIDO - não é mais Component

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// @Component REMOVIDO - será instanciado manualmente
public class PedidoBuilder {

  private PedidoId id;
  private PacienteId pacienteId;
  private Medico medicoSolicitante;
  private Medico medicoExecutor;
  private Procedimento procedimento;
  private List<Procedimento> todosProcedimentos = new ArrayList<>();
  private Convenio convenio;
  private CID cid;
  private DataHoraAgendamento agendamento;
  private StatusPedido status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;
  private List<String> observacoes;
  private List<String> documentosAnexados;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private String usuarioCriacao;
  private String usuarioAtualizacao;
  private LocalDate dataPedido;

  // 🔥 NOVOS CAMPOS
  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;
  private String telefonePaciente;
  private String enderecoPaciente;
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
  private String cpfPaciente;
  private String emailPaciente;
  private String sexoPaciente;

  public PedidoBuilder() {
    reset(); // Chama reset no construtor para inicializar tudo
  }

  // ==================== MÉTODOS DO BUILDER ====================

  public PedidoBuilder comId(PedidoId id) {
    this.id = id;
    return this;
  }

  public PedidoBuilder comPacienteId(PacienteId pacienteId) {
    this.pacienteId = pacienteId;
    return this;
  }

  public PedidoBuilder comMedicoSolicitante(Medico medicoSolicitante) {
    this.medicoSolicitante = medicoSolicitante;
    return this;
  }

  public PedidoBuilder comMedicoExecutor(Medico medicoExecutor) {
    this.medicoExecutor = medicoExecutor;
    return this;
  }

  public PedidoBuilder comProcedimento(Procedimento procedimento) {
    this.procedimento = procedimento;
    return this;
  }

  public PedidoBuilder comTodosProcedimentos(List<Procedimento> procedimentos) {
    this.todosProcedimentos = procedimentos != null ? new ArrayList<>(procedimentos) : new ArrayList<>();
    return this;
  }

  public PedidoBuilder comConvenio(Convenio convenio) {
    this.convenio = convenio;
    return this;
  }

  public PedidoBuilder comCid(CID cid) {
    this.cid = cid;
    return this;
  }

  public PedidoBuilder comCidsSecundarios(String cid2, String cid3, String cid4) {
    this.cidCodigo2 = cid2;
    this.cidCodigo3 = cid3;
    this.cidCodigo4 = cid4;
    return this;
  }

  public PedidoBuilder comAgendamento(DataHoraAgendamento agendamento) {
    this.agendamento = agendamento;
    return this;
  }

  public PedidoBuilder comStatus(StatusPedido status) {
    this.status = status;
    return this;
  }

  public PedidoBuilder comPrioridade(Prioridade prioridade) {
    this.prioridade = prioridade;
    return this;
  }

  public PedidoBuilder comLateralidade(Lateralidade lateralidade) {
    this.lateralidade = lateralidade;
    return this;
  }

  public PedidoBuilder adicionarObservacao(String observacao) {
    if (observacao != null && !observacao.trim().isEmpty()) {
      this.observacoes.add(observacao.trim());
    }
    return this;
  }

  public PedidoBuilder comObservacoes(List<String> observacoes) {
    if (observacoes != null) {
      this.observacoes = new ArrayList<>(observacoes);
    }
    return this;
  }

  public PedidoBuilder adicionarDocumento(String documento) {
    if (documento != null && !documento.trim().isEmpty()) {
      this.documentosAnexados.add(documento.trim());
    }
    return this;
  }

  public PedidoBuilder comDocumentosAnexados(List<String> documentosAnexados) {
    if (documentosAnexados != null) {
      this.documentosAnexados = new ArrayList<>(documentosAnexados);
    }
    return this;
  }

  public PedidoBuilder comIndicacaoClinica(String indicacaoClinica) {
    this.indicacaoClinica = indicacaoClinica;
    return this;
  }

  public PedidoBuilder comRelatorioPreOperatorio(String relatorioPreOperatorio) {
    this.relatorioPreOperatorio = relatorioPreOperatorio;
    return this;
  }

  public PedidoBuilder comOrientacoes(String orientacoes) {
    this.orientacoes = orientacoes;
    return this;
  }

  public PedidoBuilder comTelefonePaciente(String telefonePaciente) {
    this.telefonePaciente = telefonePaciente;
    return this;
  }

  public PedidoBuilder comEnderecoPaciente(String enderecoPaciente) {
    this.enderecoPaciente = enderecoPaciente;
    return this;
  }

  public PedidoBuilder comCpfPaciente(String cpfPaciente) {
    this.cpfPaciente = cpfPaciente;
    return this;
  }

  public PedidoBuilder comEmailPaciente(String emailPaciente) {
    this.emailPaciente = emailPaciente;
    return this;
  }

  public PedidoBuilder comSexoPaciente(String sexoPaciente) {
    this.sexoPaciente = sexoPaciente;
    return this;
  }

  public PedidoBuilder comNumeroGuia(String numeroGuia) {
    this.numeroGuia = numeroGuia;
    return this;
  }

  public PedidoBuilder comRegistroAns(String registroAns) {
    this.registroAns = registroAns;
    return this;
  }

  public PedidoBuilder comNumeroGuiaOperadora(String numeroGuiaOperadora) {
    this.numeroGuiaOperadora = numeroGuiaOperadora;
    return this;
  }

  public PedidoBuilder comCodigoOperadora(String codigoOperadora) {
    this.codigoOperadora = codigoOperadora;
    return this;
  }

  public PedidoBuilder comNomeContratado(String nomeContratado) {
    this.nomeContratado = nomeContratado;
    return this;
  }

  public PedidoBuilder comCaraterAtendimento(String caraterAtendimento) {
    this.caraterAtendimento = caraterAtendimento;
    return this;
  }

  public PedidoBuilder comTipoInternacao(String tipoInternacao) {
    this.tipoInternacao = tipoInternacao;
    return this;
  }

  public PedidoBuilder comRegimeInternacao(String regimeInternacao) {
    this.regimeInternacao = regimeInternacao;
    return this;
  }

  public PedidoBuilder comQtdDiariasSolicitadas(String qtdDiariasSolicitadas) {
    this.qtdDiariasSolicitadas = qtdDiariasSolicitadas;
    return this;
  }

  public PedidoBuilder comCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
    return this;
  }

  public PedidoBuilder comAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
    return this;
  }

  public PedidoBuilder comUsuarioCriacao(String usuarioCriacao) {
    this.usuarioCriacao = usuarioCriacao;
    return this;
  }

  public PedidoBuilder comUsuarioAtualizacao(String usuarioAtualizacao) {
    this.usuarioAtualizacao = usuarioAtualizacao;
    return this;
  }

  public PedidoBuilder comDataPedido(LocalDate dataPedido) {
    this.dataPedido = dataPedido;
    return this;
  }

  // ==================== MÉTODO RESET ====================

  public void reset() {
    this.id = null;
    this.pacienteId = null;
    this.medicoSolicitante = null;
    this.medicoExecutor = null;
    this.procedimento = null;
    this.todosProcedimentos = new ArrayList<>();
    this.convenio = null;
    this.cid = null;
    this.agendamento = null;
    this.status = new StatusPedido(StatusPedido.Tipo.RASCUNHO);
    this.prioridade = null;
    this.lateralidade = new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    this.observacoes = new ArrayList<>();
    this.documentosAnexados = new ArrayList<>();
    this.criadoEm = null;
    this.atualizadoEm = null;
    this.usuarioCriacao = null;
    this.usuarioAtualizacao = null;
    this.dataPedido = LocalDate.now();

    // Reset dos novos campos
    this.indicacaoClinica = null;
    this.relatorioPreOperatorio = null;
    this.orientacoes = null;
    this.telefonePaciente = null;
    this.enderecoPaciente = null;
    this.cidCodigo2 = null;
    this.cidCodigo3 = null;
    this.cidCodigo4 = null;
    this.numeroGuia = null;
    this.registroAns = null;
    this.numeroGuiaOperadora = null;
    this.codigoOperadora = null;
    this.nomeContratado = null;
    this.caraterAtendimento = null;
    this.tipoInternacao = null;
    this.regimeInternacao = null;
    this.qtdDiariasSolicitadas = null;
    this.cpfPaciente = null;
    this.emailPaciente = null;
    this.sexoPaciente = null;
  }

  // ==================== MÉTODO BUILD ====================

  public PedidoCirurgico build() {
    if (this.id == null) {
      this.id = PedidoId.generate();
    }
    if (this.criadoEm == null) {
      this.criadoEm = LocalDateTime.now();
    }


    PedidoCirurgico pedido = new PedidoCirurgico(
            id,
            pacienteId,
            medicoSolicitante,
            medicoExecutor,
            procedimento,
            todosProcedimentos,  // ← AGORA O CONSTRUTOR TEM QUE TER ESTE PARÂMETRO!
            convenio,
            cid,
            agendamento,
            status,
            prioridade,
            lateralidade,
            observacoes,
            documentosAnexados,
            criadoEm,
            atualizadoEm,
            usuarioCriacao,
            usuarioAtualizacao,
            dataPedido,
            indicacaoClinica,
            relatorioPreOperatorio,
            orientacoes,
            telefonePaciente,
            enderecoPaciente,
            cidCodigo2,
            cidCodigo3,
            cidCodigo4,
            numeroGuia,
            registroAns,
            numeroGuiaOperadora,
            codigoOperadora,
            nomeContratado,
            caraterAtendimento,
            tipoInternacao,
            regimeInternacao,
            qtdDiariasSolicitadas,
            cpfPaciente,
            emailPaciente,
            sexoPaciente
    );

    // Auto-reset após build (opcional, mas recomendado)
    // this.reset();

    return pedido;
  }
}