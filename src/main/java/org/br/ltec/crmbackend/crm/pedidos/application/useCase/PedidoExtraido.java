package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import java.util.ArrayList;
import java.util.List;

public class PedidoExtraido {

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

  // Útil para debug quando regex falhar
  private String textoNormalizado;

  // -------- getters/setters --------

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

  // -------- tipos auxiliares --------

  public static class ProcedimentoExtraido {
    private String codigo;
    private String descricao;

    public ProcedimentoExtraido() {}

    public ProcedimentoExtraido(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
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
  }
}
