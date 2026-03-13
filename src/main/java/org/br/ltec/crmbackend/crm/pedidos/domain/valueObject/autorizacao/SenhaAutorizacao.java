package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;

@Value
public class SenhaAutorizacao {
  String valor;

  public SenhaAutorizacao(String valor) {
    if (valor != null) {
      valor = valor.trim();

      if (valor.length() > 50) {
        throw new IllegalArgumentException("Senha muito longa (máx. 50 caracteres)");
      }
    }
    this.valor = valor;
  }
}
