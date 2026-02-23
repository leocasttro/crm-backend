package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import java.util.List;

public class ImportPedidoPdfResponse {

  private String pedidoId;

  // opcional (ajuda debug/UX)
  private String nomePaciente;
  private String convenio;
  private String cid;
  private List<String> procedimentos;

  public String getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(String pedidoId) {
    this.pedidoId = pedidoId;
  }

  public String getNomePaciente() {
    return nomePaciente;
  }

  public void setNomePaciente(String nomePaciente) {
    this.nomePaciente = nomePaciente;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public List<String> getProcedimentos() {
    return procedimentos;
  }

  public void setProcedimentos(List<String> procedimentos) {
    this.procedimentos = procedimentos;
  }
}
