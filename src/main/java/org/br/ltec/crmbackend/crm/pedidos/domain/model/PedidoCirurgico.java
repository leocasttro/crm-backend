package org.br.ltec.crmbackend.crm.pedidos.domain.model;

import lombok.extern.slf4j.Slf4j;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao.DadosAutorizacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PedidoCirurgico {

  private final PedidoId id;
  private PacienteId pacienteId;
  private Medico medicoSolicitante;
  private Medico medicoExecutor;
  private Procedimento procedimento;
  private List<Procedimento> todosProcedimentos;
  private Convenio convenio;
  private CID cid;
  private DataHoraAgendamento agendamento;
  private StatusPedido status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;
  private List<String> observacoes;
  private final List<String> documentosAnexados;
  private final LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private String usuarioCriacao;
  private String usuarioAtualizacao;
  private LocalDate dataPedido;

  // CAMPOS EXTRAÍDOS DO PDF
  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;
  private String telefonePaciente;
  private String enderecoPaciente;

  // CIDs secundários
  private String cidCodigo2;
  private String cidCodigo3;
  private String cidCodigo4;

  // Dados da guia/internação
  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;

  // Dados adicionais do paciente
  private String cpfPaciente;
  private String emailPaciente;
  private String sexoPaciente;

  // 🔥 NOVO CAMPO - Consulta Pré-Operatória (versão simplificada)
  private ConsultaPreOperatoria consultaPreOperatoria;
  private DadosAutorizacao dadosAutorizacao;

  // ==================== CONSTRUTOR SIMPLIFICADO ====================

  public PedidoCirurgico(PedidoId id, PacienteId pacienteId, Medico medicoSolicitante,
                         Procedimento procedimento, List<Procedimento> todosProcedimentos, Convenio convenio,
                         Prioridade prioridade, String usuarioCriacao) {

    // Inicializa campos obrigatórios
    this.id = id;
    this.pacienteId = pacienteId;
    this.medicoSolicitante = medicoSolicitante;
    this.procedimento = procedimento;
    this.todosProcedimentos = todosProcedimentos != null ? todosProcedimentos : new ArrayList<>();
    this.convenio = convenio;
    this.prioridade = prioridade;
    this.usuarioCriacao = usuarioCriacao;

    // Inicializa campos com valores padrão
    this.medicoExecutor = null;
    this.cid = null;
    this.agendamento = null;
    this.status = new StatusPedido(StatusPedido.Tipo.RASCUNHO);
    this.lateralidade = new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    this.observacoes = new ArrayList<>();
    this.documentosAnexados = new ArrayList<>();
    this.criadoEm = LocalDateTime.now();
    this.atualizadoEm = null;
    this.usuarioAtualizacao = null;
    this.dataPedido = LocalDate.now();

    // Inicializa campos extras com null
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
    this.consultaPreOperatoria = null;
  }

  // ==================== CONSTRUTOR COMPLETO ====================

  public PedidoCirurgico(
          PedidoId id,
          PacienteId pacienteId,
          Medico medicoSolicitante,
          Medico medicoExecutor,
          Procedimento procedimento,
          List<Procedimento> todosProcedimentos,
          Convenio convenio,
          CID cid,
          DataHoraAgendamento agendamento,
          StatusPedido status,
          Prioridade prioridade,
          Lateralidade lateralidade,
          List<String> observacoes,
          List<String> documentosAnexados,
          LocalDateTime criadoEm,
          LocalDateTime atualizadoEm,
          String usuarioCriacao,
          String usuarioAtualizacao,
          LocalDate dataPedido,

          // Campos existentes
          String indicacaoClinica,
          String relatorioPreOperatorio,
          String orientacoes,
          String telefonePaciente,
          String enderecoPaciente,
          String cidCodigo2,
          String cidCodigo3,
          String cidCodigo4,
          String numeroGuia,
          String registroAns,
          String numeroGuiaOperadora,
          String codigoOperadora,
          String nomeContratado,
          String caraterAtendimento,
          String tipoInternacao,
          String regimeInternacao,
          String qtdDiariasSolicitadas,
          String cpfPaciente,
          String emailPaciente,
          String sexoPaciente,

          // 🔥 NOVO CAMPO - Consulta Pré-Operatória
          ConsultaPreOperatoria consultaPreOperatoria,
          DadosAutorizacao dadosAutorizacao) {

    validarCriacao(id, pacienteId, medicoSolicitante, procedimento, convenio, status);

    this.id = id;
    this.pacienteId = pacienteId;
    this.medicoSolicitante = medicoSolicitante;
    this.medicoExecutor = medicoExecutor;
    this.procedimento = procedimento;
    this.todosProcedimentos = todosProcedimentos != null ? todosProcedimentos : new ArrayList<>();
    this.convenio = convenio;
    this.cid = cid;
    this.agendamento = agendamento;
    this.status = status;
    this.prioridade = prioridade;
    this.lateralidade = lateralidade;
    this.observacoes = observacoes != null ? new ArrayList<>(observacoes) : new ArrayList<>();
    this.documentosAnexados = documentosAnexados != null ? new ArrayList<>(documentosAnexados) : new ArrayList<>();
    this.criadoEm = criadoEm != null ? criadoEm : LocalDateTime.now();
    this.atualizadoEm = atualizadoEm;
    this.usuarioCriacao = usuarioCriacao;
    this.usuarioAtualizacao = usuarioAtualizacao;
    this.dataPedido = dataPedido != null ? dataPedido : LocalDate.now();

    // Campos existentes
    this.indicacaoClinica = indicacaoClinica;
    this.relatorioPreOperatorio = relatorioPreOperatorio;
    this.orientacoes = orientacoes;
    this.telefonePaciente = telefonePaciente;
    this.enderecoPaciente = enderecoPaciente;
    this.cidCodigo2 = cidCodigo2;
    this.cidCodigo3 = cidCodigo3;
    this.cidCodigo4 = cidCodigo4;
    this.numeroGuia = numeroGuia;
    this.registroAns = registroAns;
    this.numeroGuiaOperadora = numeroGuiaOperadora;
    this.codigoOperadora = codigoOperadora;
    this.nomeContratado = nomeContratado;
    this.caraterAtendimento = caraterAtendimento;
    this.tipoInternacao = tipoInternacao;
    this.regimeInternacao = regimeInternacao;
    this.qtdDiariasSolicitadas = qtdDiariasSolicitadas;
    this.cpfPaciente = cpfPaciente;
    this.emailPaciente = emailPaciente;
    this.sexoPaciente = sexoPaciente;

    // 🔥 NOVO CAMPO
    this.consultaPreOperatoria = consultaPreOperatoria;
    this.dadosAutorizacao = dadosAutorizacao;
  }

  private void validarCriacao(PedidoId id, PacienteId pacienteId, Medico medicoSolicitante,
                              Procedimento procedimento, Convenio convenio,
                              StatusPedido status) {
    if (id == null) {
      throw new IllegalArgumentException("ID do pedido é obrigatório");
    }
    if (pacienteId == null) {
      throw new IllegalArgumentException("ID do paciente é obrigatório");
    }
    if (medicoSolicitante == null) {
      throw new IllegalArgumentException("Médico solicitante é obrigatório");
    }
    if (procedimento == null) {
      throw new IllegalArgumentException("Procedimento é obrigatório");
    }
    if (convenio == null) {
      throw new IllegalArgumentException("Convênio é obrigatório");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status é obrigatório");
    }
  }

  // ==================== MÉTODOS DE NEGÓCIO - CONSULTA PRÉ (CORRIGIDOS) ====================

  public ConsultaPreOperatoria getConsultaPreOperatoria() {
    return consultaPreOperatoria;
  }

  public void setConsultaPreOperatoria(ConsultaPreOperatoria consultaPreOperatoria) {
    this.consultaPreOperatoria = consultaPreOperatoria;
  }

  /**
   * Agendar consulta pré-operatória
   */
  public void agendarConsultaPre(LocalDateTime dataHora, String cuidados, String observacoesEspeciais) {

    // Validar se pode agendar consulta pré
    if (this.status.getTipo() != StatusPedido.Tipo.AGENDADO) {
      throw new IllegalStateException(
              "Só é possível agendar consulta pré após o agendamento da cirurgia. " +
                      "Status atual: " + this.status.getTipo()
      );
    }

    this.consultaPreOperatoria = ConsultaPreOperatoria.criar(
            dataHora, cuidados, observacoesEspeciais
    );

    this.atualizadoEm = LocalDateTime.now();
  }

  /**
   * Verifica se tem consulta pré agendada
   */
  public boolean temConsultaPreAgendada() {
    return consultaPreOperatoria != null && consultaPreOperatoria.existe();
  }

  // ==================== MÉTODOS DE NEGÓCIO EXISTENTES ====================

  public void atualizarStatus(StatusPedido.Tipo novoStatus, String usuario, String observacao) {

    if (this.status.getTipo() == novoStatus) {
      throw new IllegalStateException("Pedido já está com status: " + novoStatus);
    }

    if (!this.status.getTipo().podeSerAtualizadoPara(novoStatus)) {
      throw new IllegalStateException(
              String.format("Não é possível atualizar de %s para %s",
                      this.status.getTipo(), novoStatus)
      );
    }

    this.status = new StatusPedido(novoStatus, observacao, usuario);

    if (observacao != null && !observacao.isEmpty()) {
      adicionarObservacao(observacao, usuario);
    }

    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void enviarParaAnalise(String usuario) {
    atualizarStatus(StatusPedido.Tipo.PENDENTE, usuario, "Enviado para análise");
  }

  public void iniciarAnalise(String usuario) {
    atualizarStatus(StatusPedido.Tipo.EM_ANALISE, usuario, "Análise iniciada");
  }

  public void agendar(DataHoraAgendamento agendamento, String usuario) {
    this.agendamento = agendamento;
    atualizarStatus(StatusPedido.Tipo.AGENDAR, usuario, "Pedido agendado");
  }

  public void confirmar(String usuario, String observacoesConfirmacao) {
    atualizarStatus(StatusPedido.Tipo.CONFIRMADO, usuario, observacoesConfirmacao);
  }

  public void iniciarProcedimento(String usuario) {
    atualizarStatus(StatusPedido.Tipo.EM_PROGRESSO, usuario, "Procedimento iniciado");
  }

  public void finalizar(String usuario, String observacoesFinalizacao) {
    atualizarStatus(StatusPedido.Tipo.REALIZADO, usuario, observacoesFinalizacao);
  }

  public void cancelar(String usuario, String motivo) {
    if (status.getTipo().isFinal()) {
      throw new IllegalStateException("Não é possível cancelar um pedido com status final: " + status.getTipo());
    }
    atualizarStatus(StatusPedido.Tipo.CANCELADO, usuario, motivo);
  }

  public void rejeitar(String usuario, String motivo) {
    atualizarStatus(StatusPedido.Tipo.REJEITADO, usuario, motivo);
  }

  public void aprovar(String usuario, String observacao) {
    atualizarStatus(StatusPedido.Tipo.APROVADO, usuario, observacao);
  }

  // ==================== MÉTODOS DE ATUALIZAÇÃO ====================

  public void atualizarMedicoExecutor(Medico medicoExecutor, String usuario) {
    this.medicoExecutor = medicoExecutor;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarProcedimento(Procedimento procedimento, String usuario) {
    if (status.getTipo().isFinal()) {
      throw new IllegalStateException("Não é possível alterar procedimento de um pedido com status final");
    }
    this.procedimento = procedimento;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarConvenio(Convenio convenio, String usuario) {
    if (status.getTipo().isFinal()) {
      throw new IllegalStateException("Não é possível alterar convênio de um pedido com status final");
    }
    this.convenio = convenio;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarCID(CID cid, String usuario) {
    this.cid = cid;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarDadosAutorizacao(DadosAutorizacao novosDados) {
    log.debug("Iniciando atualização de dados de autorização");
    log.debug("novosDados é null? {}", novosDados == null);

    if (novosDados == null) {
      throw new IllegalArgumentException("Dados de autorização não podem ser nulos");
    }

    log.debug("Status do novosDados: {}", novosDados.getStatus());
    log.debug("Status do dadosAutorizacao atual: {}", this.dadosAutorizacao != null ? this.dadosAutorizacao.getStatus() : "null");

    // Verificar se houve mudança no status
    if (this.dadosAutorizacao != null &&
            this.dadosAutorizacao.getStatus() != null &&
            novosDados.getStatus() != null &&
            !this.dadosAutorizacao.getStatus().equals(novosDados.getStatus())) {

      log.debug("Status mudou, adicionando observação");
      String observacao = String.format("Status de autorização alterado de %s para %s",
              this.dadosAutorizacao.getStatus().getValor(),
              novosDados.getStatus().getValor());
      adicionarObservacao(observacao, "sistema");
    }

    log.debug("Atualizando dadosAutorizacao");
    this.dadosAutorizacao = novosDados;

    log.debug("Atualizando atualizadoEm");
    this.atualizadoEm = LocalDateTime.now();

    log.debug("Atualização concluída com sucesso");
  }

  public void atualizarCIDsSecundarios(String cid2, String cid3, String cid4, String usuario) {
    this.cidCodigo2 = cid2;
    this.cidCodigo3 = cid3;
    this.cidCodigo4 = cid4;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarAgendamento(DataHoraAgendamento agendamento, String usuario) {
    if (status.getTipo().isFinal()) {
      throw new IllegalStateException("Não é possível alterar agendamento de um pedido com status final");
    }
    this.agendamento = agendamento;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarPrioridade(Prioridade prioridade, String usuario) {
    this.prioridade = prioridade;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarLateralidade(Lateralidade lateralidade, String usuario) {
    this.lateralidade = lateralidade;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarDadosClinicos(String indicacaoClinica, String relatorioPreOperatorio,
                                     String orientacoes, String usuario) {
    this.indicacaoClinica = indicacaoClinica;
    this.relatorioPreOperatorio = relatorioPreOperatorio;
    this.orientacoes = orientacoes;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarDadosGuia(String numeroGuia, String registroAns, String numeroGuiaOperadora,
                                 String codigoOperadora, String nomeContratado, String usuario) {
    this.numeroGuia = numeroGuia;
    this.registroAns = registroAns;
    this.numeroGuiaOperadora = numeroGuiaOperadora;
    this.codigoOperadora = codigoOperadora;
    this.nomeContratado = nomeContratado;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarDadosInternacao(String caraterAtendimento, String tipoInternacao,
                                       String regimeInternacao, String qtdDiariasSolicitadas, String usuario) {
    this.caraterAtendimento = caraterAtendimento;
    this.tipoInternacao = tipoInternacao;
    this.regimeInternacao = regimeInternacao;
    this.qtdDiariasSolicitadas = qtdDiariasSolicitadas;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void atualizarDadosContatoPaciente(String telefone, String endereco, String cpf,
                                            String email, String sexo, String usuario) {
    this.telefonePaciente = telefone;
    this.enderecoPaciente = endereco;
    this.cpfPaciente = cpf;
    this.emailPaciente = email;
    this.sexoPaciente = sexo;
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void adicionarObservacao(String observacao, String usuario) {
    if (observacao != null && !observacao.trim().isEmpty()) {
      this.observacoes.add(LocalDateTime.now() + " - " + usuario + ": " + observacao.trim());
      this.atualizadoEm = LocalDateTime.now();
      this.usuarioAtualizacao = usuario;
    }
  }

  public void adicionarDocumento(String documentoUrl, String usuario) {
    if (documentoUrl != null && !documentoUrl.trim().isEmpty()) {
      this.documentosAnexados.add(documentoUrl.trim());
      this.atualizadoEm = LocalDateTime.now();
      this.usuarioAtualizacao = usuario;
    }
  }

  // ==================== MÉTODOS DE CONSULTA ====================

  public boolean podeSerCancelado() {
    return !status.getTipo().isFinal() &&
            status.getTipo() != StatusPedido.Tipo.REALIZADO;
  }

  public boolean precisaConfirmarJejum() {
    return status.getTipo() == StatusPedido.Tipo.AGENDAR ||
            status.getTipo() == StatusPedido.Tipo.CONFIRMADO;
  }

  public boolean podeSerEditado() {
    return status.getTipo() == StatusPedido.Tipo.EM_ANALISE ||
            status.getTipo() == StatusPedido.Tipo.REJEITADO ||
            status.getTipo() == StatusPedido.Tipo.RASCUNHO ||
            status.getTipo() == StatusPedido.Tipo.PENDENTE;
  }

  // ==================== GETTERS EXISTENTES ====================

  public PedidoId getId() {
    return id;
  }

  public PacienteId getPacienteId() {
    return pacienteId;
  }

  public Medico getMedicoSolicitante() {
    return medicoSolicitante;
  }

  public void setMedicoSolicitante(Medico medicoSolicitante) {
    this.medicoSolicitante = medicoSolicitante;
  }

  public Medico getMedicoExecutor() {
    return medicoExecutor;
  }

  public Procedimento getProcedimento() {
    return procedimento;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public CID getCid() {
    return cid;
  }

  public DataHoraAgendamento getAgendamento() {
    return agendamento;
  }

  public StatusPedido getStatus() {
    return status;
  }

  public Prioridade getPrioridade() {
    return prioridade;
  }

  public Lateralidade getLateralidade() {
    return lateralidade;
  }

  public List<String> getObservacoes() {
    return Collections.unmodifiableList(observacoes);
  }

  public List<String> getDocumentosAnexados() {
    return Collections.unmodifiableList(documentosAnexados);
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public String getUsuarioCriacao() {
    return usuarioCriacao;
  }

  public String getUsuarioAtualizacao() {
    return usuarioAtualizacao;
  }

  public LocalDate getDataPedido() {
    return dataPedido;
  }

  public void setDataPedido(LocalDate dataPedido) {
    this.dataPedido = dataPedido;
  }

  // GETTERS PARA CAMPOS EXISTENTES
  public String getIndicacaoClinica() {
    return indicacaoClinica;
  }

  public String getRelatorioPreOperatorio() {
    return relatorioPreOperatorio;
  }

  public String getOrientacoes() {
    return orientacoes;
  }

  public String getTelefonePaciente() {
    return telefonePaciente;
  }

  public String getEnderecoPaciente() {
    return enderecoPaciente;
  }

  public String getCidCodigo2() {
    return cidCodigo2;
  }

  public String getCidCodigo3() {
    return cidCodigo3;
  }

  public String getCidCodigo4() {
    return cidCodigo4;
  }

  public String getNumeroGuia() {
    return numeroGuia;
  }

  public String getRegistroAns() {
    return registroAns;
  }

  public String getNumeroGuiaOperadora() {
    return numeroGuiaOperadora;
  }

  public String getCodigoOperadora() {
    return codigoOperadora;
  }

  public String getNomeContratado() {
    return nomeContratado;
  }

  public String getCaraterAtendimento() {
    return caraterAtendimento;
  }

  public String getTipoInternacao() {
    return tipoInternacao;
  }

  public String getRegimeInternacao() {
    return regimeInternacao;
  }

  public String getQtdDiariasSolicitadas() {
    return qtdDiariasSolicitadas;
  }

  public String getCpfPaciente() {
    return cpfPaciente;
  }

  public String getEmailPaciente() {
    return emailPaciente;
  }

  public String getSexoPaciente() {
    return sexoPaciente;
  }

  // GETTER PARA TODOS PROCEDIMENTOS
  public List<Procedimento> getTodosProcedimentos() {
    return todosProcedimentos != null ? Collections.unmodifiableList(todosProcedimentos) : Collections.emptyList();
  }

  // SETTER PARA TODOS PROCEDIMENTOS
  public void setTodosProcedimentos(List<Procedimento> todosProcedimentos) {
    this.todosProcedimentos = todosProcedimentos != null ? new ArrayList<>(todosProcedimentos) : new ArrayList<>();
  }

  public DadosAutorizacao getDadosAutorizacao() {
    return dadosAutorizacao;
  }
  // ==================== MÉTODOS DE CONSULTA BOOLEANOS ====================

  public boolean isRascunho() {
    return status.getTipo() == StatusPedido.Tipo.RASCUNHO;
  }

  public boolean isAtivo() {
    return status.getTipo().isAtivo();
  }

  public boolean isFinalizado() {
    return status.getTipo().isFinal();
  }

  public boolean temAgendamento() {
    return agendamento != null;
  }

  public boolean temMedicoExecutor() {
    return medicoExecutor != null;
  }

  public boolean temCID() {
    return cid != null;
  }

  public boolean temCIDsSecundarios() {
    return cidCodigo2 != null || cidCodigo3 != null || cidCodigo4 != null;
  }

  public boolean temDocumentos() {
    return !documentosAnexados.isEmpty();
  }

  public boolean temIndicacaoClinica() {
    return indicacaoClinica != null && !indicacaoClinica.trim().isEmpty();
  }

  public boolean temRelatorioPreOperatorio() {
    return relatorioPreOperatorio != null && !relatorioPreOperatorio.trim().isEmpty();
  }

  public boolean temDadosGuia() {
    return numeroGuia != null || registroAns != null || numeroGuiaOperadora != null;
  }

  // ==================== EQUALS, HASHCODE, TOSTRING ====================

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PedidoCirurgico that = (PedidoCirurgico) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "PedidoCirurgico{" +
            "id=" + id +
            ", procedimento=" + (procedimento != null ? procedimento.getDescricao() : "null") +
            ", status=" + (status != null ? status.getTipo() : "null") +
            ", dataPedido=" + dataPedido +
            ", temConsultaPre=" + (consultaPreOperatoria != null) +
            '}';
  }
}