package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;

@Value
public class StatusAutorizacao {

  String valor;

  public static final String AUTORIZADO = "AUTORIZADO";
  public static final String AUTORIZADO_PARCIAL = "AUTORIZADO_PARCIAL";
  public static final String PENDENTE = "PENDENTE";
  public static final String NEGADO = "NEGADO";

  public StatusAutorizacao(String valor) {
    if (valor != null && !isValid(valor)) {
      throw new IllegalArgumentException("Status de autorização inválido: " + valor);
    }
    this.valor = valor;
  }

  private boolean isValid(String valor) {
    return valor == null || valor.equals(AUTORIZADO) || valor.equals(AUTORIZADO_PARCIAL) || valor
            .equals(PENDENTE) || valor.equals(NEGADO);
  }

  public boolean isAutorizado() {
    return AUTORIZADO.equals(valor) || AUTORIZADO_PARCIAL.equals(valor);
  }

  public boolean isAutorizadoParcial() { return AUTORIZADO_PARCIAL.equals(valor); }

  public boolean isNegado() {
    return NEGADO.equals(valor);
  }

  public boolean isPendente() { return PENDENTE.equals(valor); }

  public boolean isStatusFinal() {
    return isAutorizado() || isNegado();
  }

  public static StatusAutorizacao autorizado() {
    return new StatusAutorizacao(AUTORIZADO);
  }

  public static StatusAutorizacao parcial() {
    return new StatusAutorizacao(AUTORIZADO_PARCIAL);
  }

  public static StatusAutorizacao pendente() {
    return new StatusAutorizacao(PENDENTE);
  }

  public static StatusAutorizacao negado() {
    return new StatusAutorizacao(NEGADO);
  }

}
