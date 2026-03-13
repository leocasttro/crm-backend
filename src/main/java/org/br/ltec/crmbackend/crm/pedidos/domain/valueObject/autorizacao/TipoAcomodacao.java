package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;

@Value
public class TipoAcomodacao {
  String valor;

  public static final String APARTAMENTO = "APARTAMENTO";
  public static final String QUARTO = "QUARTO";
  public static final String ENFERMARIA = "ENFERMARIA";
  public static final String UTI = "UTI";

  public TipoAcomodacao(String valor) {
    if (valor != null && !isValid(valor)) {
      throw new IllegalArgumentException("Tipo de acomodação inválido: " + valor);
    }
    this.valor = valor;
  }

  private boolean isValid(String valor) {
    return valor == null || valor.equals(APARTAMENTO) || valor.equals(QUARTO) ||
            valor.equals(ENFERMARIA) || valor.equals(UTI);
  }
}
