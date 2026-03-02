package org.br.ltec.crmbackend.crm.pedidos.domain.model;

public class ResultadoValidacao {
  private final boolean valido;
  private final String mensagem;

  private ResultadoValidacao(boolean valido, String mensagem) {
    this.valido = valido;
    this.mensagem = mensagem;
  }

  public static ResultadoValidacao ok() {
    return new ResultadoValidacao(true, null);
  }

  public static ResultadoValidacao erro(String mensagem) {
    return new ResultadoValidacao(false, mensagem);
  }

  public boolean isValido() { return valido; }
  public String getMensagem() { return mensagem; }
}