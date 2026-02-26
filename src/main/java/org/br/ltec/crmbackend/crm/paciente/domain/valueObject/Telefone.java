package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Telefone {
  private static final Pattern TELEFONE_PATTERN = Pattern.compile(
          "^(\\+55)?\\s*(\\(?\\d{2}\\)?)\\s*(\\d{4,5}-?\\d{4})$"
  );

  private final String numero;
  private final TipoTelefone tipo;
  private final String ddd;

  public enum TipoTelefone {
    CELULAR("Celular"),
    RESIDENCIAL("Residencial"),
    COMERCIAL("Comercial");

    private final String descricao;

    TipoTelefone(String descricao) {
      this.descricao = descricao;
    }

    public String getDescricao() {
      return descricao;
    }
  }

  public Telefone(String numero, TipoTelefone tipo) {
    validar(numero, tipo);
    this.numero = limparFormatacao(numero);
    this.tipo = tipo;
    this.ddd = extrairDDD(this.numero);
  }

  private void validar(String numero, TipoTelefone tipo) {
    if (numero == null || numero.trim().isEmpty()) {
      throw new IllegalArgumentException("Número de telefone não pode ser vazio");
    }

    String numeroLimpo = limparFormatacao(numero);

    if (tipo == TipoTelefone.CELULAR) {
      if (numeroLimpo.length() < 10 || numeroLimpo.length() > 11) {
        throw new IllegalArgumentException("Número de celular deve ter 10 ou 11 dígitos");
      }
    } else {
      if (numeroLimpo.length() != 10) {
        throw new IllegalArgumentException("Telefone fixo deve ter 10 dígitos");
      }
    }

    if (!TELEFONE_PATTERN.matcher(formatarDDD(numeroLimpo)).matches()) {
      throw new IllegalArgumentException("Número de telefone inválido: " + numero);
    }
  }

  private String limparFormatacao(String numero) {
    return numero.replaceAll("[^\\d]", "");
  }

  private String extrairDDD(String numero) {
    if (numero.length() >= 10) {
      return numero.substring(0, 2);
    }
    return "";
  }

  private String formatarDDD(String numero) {
    if (numero.length() >= 10) {
      return "(" + numero.substring(0, 2) + ")" + numero.substring(2);
    }
    return numero;
  }

  public String getNumero() {
    return numero;
  }

  public String getNumeroFormatado() {
    if (numero.length() == 10) {
      return String.format("(%s) %s-%s",
              numero.substring(0, 2),
              numero.substring(2, 6),
              numero.substring(6));
    } else if (numero.length() == 11) {
      return String.format("(%s) %s-%s",
              numero.substring(0, 2),
              numero.substring(2, 7),
              numero.substring(7));
    }
    return numero;
  }

  public TipoTelefone getTipo() {
    return tipo;
  }

  public String getDDD() {
    return ddd;
  }

  public boolean isCelular() {
    return tipo == TipoTelefone.CELULAR;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Telefone telefone = (Telefone) o;
    return Objects.equals(numero, telefone.numero) &&
            tipo == telefone.tipo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero, tipo);
  }

  @Override
  public String toString() {
    return getNumeroFormatado();
  }
}