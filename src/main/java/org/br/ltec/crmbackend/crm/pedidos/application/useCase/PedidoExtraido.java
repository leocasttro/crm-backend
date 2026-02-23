package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import java.util.ArrayList;
import java.util.List;

public class PedidoExtraido {

  // ==================== DADOS BÁSICOS (existentes) ====================
  private String nomePaciente;
  private String dataPedido;      // dd/MM/yyyy
  private String horaPedido;      // HH:mm:ss
  private String convenio;
  private String hospital;
  private String cid;
  private String medicoNome;
  private String crmUf;
  private String crmNumero;
  private List<ProcedimentoExtraido> procedimentos = new ArrayList<>();
  private String textoNormalizado;

  // ==================== NOVOS CAMPOS ====================

  // Data de nascimento
  private String dataNascimento;   // dd/MM/yyyy

  // Tipo (Eletiva/Urgência)
  private String tipo;

  // Alergias
  private String alergias;

  // Lateralidade
  private String lateralidade;

  // CIDs secundários
  private String cid2;
  private String cid3;
  private String cid4;

  // Dados completos do médico (da guia)
  private String conselhoProfissional;
  private String numeroConselho;
  private String ufConselho;
  private String cbo;

  // Dados da guia
  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;

  // Dados do beneficiário
  private String numeroCarteira;
  private String validadeCarteira;
  private String cartaoNacionalSaude;

  // Dados do contratado
  private String codigoOperadora;
  private String nomeContratado;

  // Dados da internação
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;
  private String previsaoUsoOpmepdf;

  // Indicação clínica
  private String indicacaoClinica;

  // Data da solicitação
  private String dataSolicitacao;

  // Contato
  private String telefone;
  private String enderecoMedico;

  // ==================== GETTERS E SETTERS (existentes) ====================

  public String getNomePaciente() {
    return nomePaciente;
  }

  public void setNomePaciente(String nomePaciente) {
    this.nomePaciente = nomePaciente;
  }

  public String getDataPedido() {
    return dataPedido;
  }

  public void setDataPedido(String dataPedido) {
    this.dataPedido = dataPedido;
  }

  public String getHoraPedido() {
    return horaPedido;
  }

  public void setHoraPedido(String horaPedido) {
    this.horaPedido = horaPedido;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public String getHospital() {
    return hospital;
  }

  public void setHospital(String hospital) {
    this.hospital = hospital;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getMedicoNome() {
    return medicoNome;
  }

  public void setMedicoNome(String medicoNome) {
    this.medicoNome = medicoNome;
  }

  public String getCrmUf() {
    return crmUf;
  }

  public void setCrmUf(String crmUf) {
    this.crmUf = crmUf;
  }

  public String getCrmNumero() {
    return crmNumero;
  }

  public void setCrmNumero(String crmNumero) {
    this.crmNumero = crmNumero;
  }

  public List<ProcedimentoExtraido> getProcedimentos() {
    return procedimentos;
  }

  public void setProcedimentos(List<ProcedimentoExtraido> procedimentos) {
    this.procedimentos = (procedimentos == null) ? new ArrayList<>() : procedimentos;
  }

  public String getTextoNormalizado() {
    return textoNormalizado;
  }

  public void setTextoNormalizado(String textoNormalizado) {
    this.textoNormalizado = textoNormalizado;
  }

  // ==================== GETTERS E SETTERS (NOVOS CAMPOS) ====================

  public String getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getAlergias() {
    return alergias;
  }

  public void setAlergias(String alergias) {
    this.alergias = alergias;
  }

  public String getLateralidade() {
    return lateralidade;
  }

  public void setLateralidade(String lateralidade) {
    this.lateralidade = lateralidade;
  }

  public String getCid2() {
    return cid2;
  }

  public void setCid2(String cid2) {
    this.cid2 = cid2;
  }

  public String getCid3() {
    return cid3;
  }

  public void setCid3(String cid3) {
    this.cid3 = cid3;
  }

  public String getCid4() {
    return cid4;
  }

  public void setCid4(String cid4) {
    this.cid4 = cid4;
  }

  public String getConselhoProfissional() {
    return conselhoProfissional;
  }

  public void setConselhoProfissional(String conselhoProfissional) {
    this.conselhoProfissional = conselhoProfissional;
  }

  public String getNumeroConselho() {
    return numeroConselho;
  }

  public void setNumeroConselho(String numeroConselho) {
    this.numeroConselho = numeroConselho;
  }

  public String getUfConselho() {
    return ufConselho;
  }

  public void setUfConselho(String ufConselho) {
    this.ufConselho = ufConselho;
  }

  public String getCbo() {
    return cbo;
  }

  public void setCbo(String cbo) {
    this.cbo = cbo;
  }

  public String getNumeroGuia() {
    return numeroGuia;
  }

  public void setNumeroGuia(String numeroGuia) {
    this.numeroGuia = numeroGuia;
  }

  public String getRegistroAns() {
    return registroAns;
  }

  public void setRegistroAns(String registroAns) {
    this.registroAns = registroAns;
  }

  public String getNumeroGuiaOperadora() {
    return numeroGuiaOperadora;
  }

  public void setNumeroGuiaOperadora(String numeroGuiaOperadora) {
    this.numeroGuiaOperadora = numeroGuiaOperadora;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public void setNumeroCarteira(String numeroCarteira) {
    this.numeroCarteira = numeroCarteira;
  }

  public String getValidadeCarteira() {
    return validadeCarteira;
  }

  public void setValidadeCarteira(String validadeCarteira) {
    this.validadeCarteira = validadeCarteira;
  }

  public String getCartaoNacionalSaude() {
    return cartaoNacionalSaude;
  }

  public void setCartaoNacionalSaude(String cartaoNacionalSaude) {
    this.cartaoNacionalSaude = cartaoNacionalSaude;
  }

  public String getCodigoOperadora() {
    return codigoOperadora;
  }

  public void setCodigoOperadora(String codigoOperadora) {
    this.codigoOperadora = codigoOperadora;
  }

  public String getNomeContratado() {
    return nomeContratado;
  }

  public void setNomeContratado(String nomeContratado) {
    this.nomeContratado = nomeContratado;
  }

  public String getCaraterAtendimento() {
    return caraterAtendimento;
  }

  public void setCaraterAtendimento(String caraterAtendimento) {
    this.caraterAtendimento = caraterAtendimento;
  }

  public String getTipoInternacao() {
    return tipoInternacao;
  }

  public void setTipoInternacao(String tipoInternacao) {
    this.tipoInternacao = tipoInternacao;
  }

  public String getRegimeInternacao() {
    return regimeInternacao;
  }

  public void setRegimeInternacao(String regimeInternacao) {
    this.regimeInternacao = regimeInternacao;
  }

  public String getQtdDiariasSolicitadas() {
    return qtdDiariasSolicitadas;
  }

  public void setQtdDiariasSolicitadas(String qtdDiariasSolicitadas) {
    this.qtdDiariasSolicitadas = qtdDiariasSolicitadas;
  }

  public String getPrevisaoUsoOpmepdf() {
    return previsaoUsoOpmepdf;
  }

  public void setPrevisaoUsoOpmepdf(String previsaoUsoOpmepdf) {
    this.previsaoUsoOpmepdf = previsaoUsoOpmepdf;
  }

  public String getIndicacaoClinica() {
    return indicacaoClinica;
  }

  public void setIndicacaoClinica(String indicacaoClinica) {
    this.indicacaoClinica = indicacaoClinica;
  }

  public String getDataSolicitacao() {
    return dataSolicitacao;
  }

  public void setDataSolicitacao(String dataSolicitacao) {
    this.dataSolicitacao = dataSolicitacao;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEnderecoMedico() {
    return enderecoMedico;
  }

  public void setEnderecoMedico(String enderecoMedico) {
    this.enderecoMedico = enderecoMedico;
  }

  // ==================== TIPOS AUXILIARES ATUALIZADOS ====================

  public static class ProcedimentoExtraido {
    private String codigo;
    private String descricao;
    private String quantidade;  // NOVO: quantidade do procedimento

    public ProcedimentoExtraido() {}

    public ProcedimentoExtraido(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.quantidade = "1"; // default
    }

    public ProcedimentoExtraido(String codigo, String descricao, String quantidade) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.quantidade = quantidade;
    }

    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    public String getDescricao() {
      return descricao;
    }

    public void setDescricao(String descricao) {
      this.descricao = descricao;
    }

    public String getQuantidade() {
      return quantidade;
    }

    public void setQuantidade(String quantidade) {
      this.quantidade = quantidade;
    }
  }
}