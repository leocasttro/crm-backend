package org.br.ltec.crmbackend.crm.pedidos.application.command;

public class StatusPedidoCommand {

  private String pedidoId;
  private String status;
  private String observacao;
  private String usuario;

  public StatusPedidoCommand() {
  }

  public StatusPedidoCommand(String pedidoId, String status, String usuario) {
    this.pedidoId = pedidoId;
    this.status = status;
    this.usuario = usuario;
  }

  // Getters e Setters
  public String getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(String pedidoId) {
    this.pedidoId = pedidoId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
}