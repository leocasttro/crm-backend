package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class NumeroGuia {
  String valor;

  public NumeroGuia(String valor) {
    log.debug("NumeroGuia constructor recebido: '{}'", valor);

    if (valor != null) {
      String valorTrimmed = valor.trim();
      log.debug("NumeroGuia após trim: '{}'", valorTrimmed);

      if (valorTrimmed.isEmpty()) {
        log.error("Número da guia vazio após trim");
        throw new IllegalArgumentException("Número da guia não pode ser vazio");
      }

      if (valorTrimmed.length() > 50) {
        log.error("Número da guia muito longo: {} caracteres", valorTrimmed.length());
        throw new IllegalArgumentException("Número da guia não pode ser maior que 50 caracteres");
      }

      // Regex mais permissiva - permite letras, números, hífen, barra, ponto, espaço
      if (!valorTrimmed.matches("^[A-Za-z0-9-/.\\s]+$")) {
        log.error("Número da guia contém caracteres inválidos: {}", valorTrimmed);
        throw new IllegalArgumentException("Número da guia contém caracteres inválidos. Use apenas letras, números, hífen, barra, ponto e espaço.");
      }

      this.valor = valorTrimmed;
    } else {
      this.valor = null;
    }

    log.debug("NumeroGuia criado com valor: {}", this.valor);
  }

  public boolean isPresente() {
    return valor != null && !valor.trim().isEmpty();
  }

  @Override
  public String toString() {
    return valor;
  }
}