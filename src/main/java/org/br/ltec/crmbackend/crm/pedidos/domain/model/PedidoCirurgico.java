package org.br.ltec.crmbackend.crm.pedidos.domain.model;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PedidoCirurgico {

  private final PedidoId id;
  private PacienteId pacienteId;
  private Medico medicoSolicitante;
  private Medico medicoExecutor;
  private Procedimento procedimento;
  private Convenio convenio;
  private CID cid;
  private DataHoraAgendamento agendamento;
  private StatusPedido status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;
  private final List<String> observacoes;
  private final List<String> documentosAnexados;
  private final LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private String usuarioCriacao;
  private String usuarioAtualizacao;
  private LocalDate dataPedido;

  // Construtor simplificado
  public PedidoCirurgico(PedidoId id, PacienteId pacienteId, Medico medicoSolicitante,
                         Procedimento procedimento, Convenio convenio,
                         Prioridade prioridade, String usuarioCriacao) {
    this(id, pacienteId, medicoSolicitante, null, procedimento, convenio, null,
            null, new StatusPedido(StatusPedido.Tipo.RASCUNHO), prioridade,
            new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL), new ArrayList<>(),
            new ArrayList<>(), LocalDateTime.now(), null, usuarioCriacao, null, LocalDate.now());
  }

  // Construtor completo
  public PedidoCirurgico(PedidoId id, PacienteId pacienteId, Medico medicoSolicitante,
                         Medico medicoExecutor, Procedimento procedimento,
                         Convenio convenio, CID cid, DataHoraAgendamento agendamento,
                         StatusPedido status, Prioridade prioridade, Lateralidade lateralidade,
                         List<String> observacoes, List<String> documentosAnexados,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm, String usuarioCriacao,
                         String usuarioAtualizacao, LocalDate dataPedido) {

    validarCriacao(id, pacienteId, medicoSolicitante, procedimento, convenio, status);

    this.id = id;
    this.pacienteId = pacienteId;
    this.medicoSolicitante = medicoSolicitante;
    this.medicoExecutor = medicoExecutor;
    this.procedimento = procedimento;
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
  }

  private void validarCriacao(PedidoId id, PacienteId pacienteId, Medico medicoSolicitante,
                              Procedimento procedimento, Convenio convenio,
                              StatusPedido status) {
    if (id == null) {
      throw new IllegalArgumentException("ID do pedido é obrigatório");
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

  // Métodos de negócio (usando getTipo())
  public void enviarParaAnalise(String usuario) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.PENDENTE)) {
      throw new IllegalStateException("Não é possível enviar para análise este pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.PENDENTE, "Enviado para análise", usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void iniciarAnalise(String usuario) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.EM_ANALISE)) {
      throw new IllegalStateException("Não é possível iniciar análise deste pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.EM_ANALISE, "Análise iniciada", usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void agendar(DataHoraAgendamento agendamento, String usuario) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.AGENDADO)) {
      throw new IllegalStateException("Não é possível agendar este pedido no status atual: " + status.getTipo());
    }
    this.agendamento = agendamento;
    this.status = new StatusPedido(StatusPedido.Tipo.AGENDADO, "Pedido agendado", usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void confirmar(String usuario, String observacoesConfirmacao) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.CONFIRMADO)) {
      throw new IllegalStateException("Não é possível confirmar este pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.CONFIRMADO, observacoesConfirmacao, usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void iniciarProcedimento(String usuario) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.EM_PROGRESSO)) {
      throw new IllegalStateException("Não é possível iniciar procedimento deste pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.EM_PROGRESSO, "Procedimento iniciado", usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void finalizar(String usuario, String observacoesFinalizacao) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.REALIZADO)) {
      throw new IllegalStateException("Não é possível finalizar este pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.REALIZADO, observacoesFinalizacao, usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void cancelar(String usuario, String motivo) {
    if (status.getTipo().isFinal()) {
      throw new IllegalStateException("Não é possível cancelar um pedido com status final: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.CANCELADO, motivo, usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  public void rejeitar(String usuario, String motivo) {
    if (!status.getTipo().podeSerAtualizadoPara(StatusPedido.Tipo.REJEITADO)) {
      throw new IllegalStateException("Não é possível rejeitar este pedido no status atual: " + status.getTipo());
    }
    this.status = new StatusPedido(StatusPedido.Tipo.REJEITADO, motivo, usuario);
    this.atualizadoEm = LocalDateTime.now();
    this.usuarioAtualizacao = usuario;
  }

  // Métodos de atualização
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

  // Métodos de consulta
  public boolean podeSerEditado() {
    return status.getTipo() == StatusPedido.Tipo.RASCUNHO ||
            status.getTipo() == StatusPedido.Tipo.PENDENTE;
  }

  public boolean podeSerCancelado() {
    return !status.getTipo().isFinal() &&
            status.getTipo() != StatusPedido.Tipo.REALIZADO;
  }

  public boolean precisaConfirmarJejum() {
    return status.getTipo() == StatusPedido.Tipo.AGENDADO ||
            status.getTipo() == StatusPedido.Tipo.CONFIRMADO;
  }

  // Getters
  public PedidoId getId() {
    return id;
  }

  public PacienteId getPacienteId() {
    return pacienteId;
  }

  public Medico getMedicoSolicitante() {
    return medicoSolicitante;
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

  public boolean temDocumentos() {
    return !documentosAnexados.isEmpty();
  }

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
            ", procedimento=" + procedimento +
            ", status=" + status.getTipo() +
            ", dataPedido=" + dataPedido +
            '}';
  }
}