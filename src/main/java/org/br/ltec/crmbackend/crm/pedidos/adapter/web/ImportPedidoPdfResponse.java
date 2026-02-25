package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportPedidoPdfResponse {

  private boolean sucesso;
  private String mensagem;
  private String pedidoId;
  private String pacienteId;

  // ==================== DADOS DO PACIENTE ====================
  private String nomePaciente;
  private String dataNascimento;
  private String cpf;
  private String telefone;
  private String email;
  private String sexo;

  // ==================== DADOS DO CONVÊNIO ====================
  private String convenio;
  private String numeroCarteira;
  private String validadeCarteira;

  // ==================== DADOS MÉDICOS ====================
  private String medicoNome;
  private String crmNumero;
  private String crmUf;
  private String crmCompleto;
  private String cbo;
  private String conselhoProfissional;

  // ==================== DADOS DO PEDIDO ====================
  private String cid;
  private String cid2;
  private String cid3;
  private String cid4;
  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;
  private String tipo;
  private String alergias;
  private String lateralidade;
  private String dataPedido;
  private String dataSolicitacao;
  private String prioridade;
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;

  // ==================== DADOS DA GUIA ====================
  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;
  private String enderecoMedico;

  // ==================== PROCEDIMENTOS ====================
  private List<ProcedimentoResponse> procedimentos;

  private ImportPedidoPdfResponse() {}

  // Factory method para sucesso (AGORA COM TODOS OS DADOS)
  public static ImportPedidoPdfResponse sucesso(
          String pedidoId,
          String pacienteId,
          org.br.ltec.crmbackend.crm.pedidos.application.useCase.PedidoExtraido extraido,
          List<ProcedimentoResponse> procedimentos) {

    ImportPedidoPdfResponse response = new ImportPedidoPdfResponse();
    response.sucesso = true;
    response.mensagem = "PDF importado e pedido criado com sucesso";
    response.pedidoId = pedidoId;
    response.pacienteId = pacienteId;

    // 📌 DADOS DO PACIENTE
    response.nomePaciente = extraido.getNomePaciente();
    response.dataNascimento = extraido.getDataNascimento();
    response.telefone = extraido.getTelefone();

    // 📌 DADOS DO CONVÊNIO
    response.convenio = extraido.getConvenio();
    response.numeroCarteira = extraido.getNumeroCarteira();
    response.validadeCarteira = extraido.getValidadeCarteira();

    // 📌 DADOS MÉDICOS
    response.medicoNome = extraido.getMedicoNome();
    response.crmNumero = extraido.getCrmNumero();
    response.crmUf = extraido.getCrmUf();
    if (extraido.getCrmNumero() != null) {
      response.crmCompleto = extraido.getCrmNumero() +
              (extraido.getCrmUf() != null ? "/" + extraido.getCrmUf() : "");
    }
    response.cbo = extraido.getCbo();
    response.conselhoProfissional = extraido.getConselhoProfissional();

    // 📌 DADOS DO PEDIDO
    response.cid = extraido.getCid();
    response.cid2 = extraido.getCid2();
    response.cid3 = extraido.getCid3();
    response.cid4 = extraido.getCid4();
    response.indicacaoClinica = extraido.getIndicacaoClinica();
    response.relatorioPreOperatorio = extraido.getRelatorioPreOperatorio();
    response.orientacoes = extraido.getOrientacoes();
    response.tipo = extraido.getTipo();
    response.alergias = extraido.getAlergias();
    response.lateralidade = extraido.getLateralidade();
    response.dataPedido = extraido.getDataPedido();
    response.dataSolicitacao = extraido.getDataSolicitacao();
    response.caraterAtendimento = extraido.getCaraterAtendimento();
    response.tipoInternacao = extraido.getTipoInternacao();
    response.regimeInternacao = extraido.getRegimeInternacao();
    response.qtdDiariasSolicitadas = extraido.getQtdDiariasSolicitadas();

    // 📌 DADOS DA GUIA
    response.numeroGuia = extraido.getNumeroGuia();
    response.registroAns = extraido.getRegistroAns();
    response.numeroGuiaOperadora = extraido.getNumeroGuiaOperadora();
    response.codigoOperadora = extraido.getCodigoOperadora();
    response.nomeContratado = extraido.getNomeContratado();
    response.enderecoMedico = extraido.getEnderecoMedico();

    // 📌 PROCEDIMENTOS
    response.procedimentos = procedimentos;

    return response;
  }

  // Factory method para erro
  public static ImportPedidoPdfResponse erro(String mensagem) {
    ImportPedidoPdfResponse response = new ImportPedidoPdfResponse();
    response.sucesso = false;
    response.mensagem = mensagem;
    return response;
  }

  // Getters para todos os campos
  public boolean isSucesso() { return sucesso; }
  public String getMensagem() { return mensagem; }
  public String getPedidoId() { return pedidoId; }
  public String getPacienteId() { return pacienteId; }

  // Getters dados paciente
  public String getNomePaciente() { return nomePaciente; }
  public String getDataNascimento() { return dataNascimento; }
  public String getCpf() { return cpf; }
  public String getTelefone() { return telefone; }
  public String getEmail() { return email; }
  public String getSexo() { return sexo; }

  // Getters convênio
  public String getConvenio() { return convenio; }
  public String getNumeroCarteira() { return numeroCarteira; }
  public String getValidadeCarteira() { return validadeCarteira; }

  // Getters médico
  public String getMedicoNome() { return medicoNome; }
  public String getCrmNumero() { return crmNumero; }
  public String getCrmUf() { return crmUf; }
  public String getCrmCompleto() { return crmCompleto; }
  public String getCbo() { return cbo; }
  public String getConselhoProfissional() { return conselhoProfissional; }

  // Getters pedido
  public String getCid() { return cid; }
  public String getCid2() { return cid2; }
  public String getCid3() { return cid3; }
  public String getCid4() { return cid4; }
  public String getIndicacaoClinica() { return indicacaoClinica; }
  public String getRelatorioPreOperatorio() { return relatorioPreOperatorio; }
  public String getOrientacoes() { return orientacoes; }
  public String getTipo() { return tipo; }
  public String getAlergias() { return alergias; }
  public String getLateralidade() { return lateralidade; }
  public String getDataPedido() { return dataPedido; }
  public String getDataSolicitacao() { return dataSolicitacao; }
  public String getPrioridade() { return prioridade; }
  public String getCaraterAtendimento() { return caraterAtendimento; }
  public String getTipoInternacao() { return tipoInternacao; }
  public String getRegimeInternacao() { return regimeInternacao; }
  public String getQtdDiariasSolicitadas() { return qtdDiariasSolicitadas; }

  // Getters guia
  public String getNumeroGuia() { return numeroGuia; }
  public String getRegistroAns() { return registroAns; }
  public String getNumeroGuiaOperadora() { return numeroGuiaOperadora; }
  public String getCodigoOperadora() { return codigoOperadora; }
  public String getNomeContratado() { return nomeContratado; }
  public String getEnderecoMedico() { return enderecoMedico; }

  // Getter procedimentos
  public List<ProcedimentoResponse> getProcedimentos() { return procedimentos; }

  // Classe interna para procedimentos
  public static class ProcedimentoResponse {
    private String codigo;
    private String descricao;
    private String quantidade;

    public ProcedimentoResponse(String codigo, String descricao, String quantidade) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.quantidade = quantidade;
    }

    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public String getQuantidade() { return quantidade; }
  }
}