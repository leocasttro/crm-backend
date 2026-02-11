package org.br.ltec.crmbackend.crm.pedidos.application.command;

import java.time.LocalDateTime;

public class AgendamentoPedidoCommand {

  private String pedidoId;
  private LocalDateTime dataHora;
  private String sala;
  private Integer duracaoEstimada;
  private String observacoes;
  private String usuario;

  public AgendamentoPedidoCommand() {
  }

  public AgendamentoPedidoCommand(String pedidoId, LocalDateTime dataHora, String usuario) {
    this.pedidoId = pedidoId;
    this.dataHora = dataHora;
    this.usuario = usuario;
  }

  // Getters e Setters
  public String getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(String pedidoId) {
    this.pedidoId = pedidoId;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public String getSala() {
    return sala;
  }

  public void setSala(String sala) {
    this.sala = sala;
  }

  public Integer getDuracaoEstimada() {
    return duracaoEstimada;
  }

  public void setDuracaoEstimada(Integer duracaoEstimada) {
    this.duracaoEstimada = duracaoEstimada;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
}