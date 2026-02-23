package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

public class ImportPedidoPdfRequest {

  // opcional: se você quiser ajudar a vincular o paciente
  private String cpf;

  // opcional: caso você já tenha pacienteId no frontend
  private String pacienteId;

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getPacienteId() {
    return pacienteId;
  }

  public void setPacienteId(String pacienteId) {
    this.pacienteId = pacienteId;
  }
}
