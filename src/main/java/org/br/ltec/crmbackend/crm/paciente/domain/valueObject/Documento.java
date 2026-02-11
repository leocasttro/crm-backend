package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;

public abstract class Documento {
  protected final String numero;
  protected final TipoDocumento tipo;

  public enum TipoDocumento {
    CPF
  }

  protected Documento(String numero, TipoDocumento tipo) {
    validar(numero);
    this.numero = limparFormatacao(numero);
    this.tipo = tipo;
  }

  protected abstract void validar(String numero);

  protected String limparFormatacao(String numero) {
    if (numero == null) return null;
    return numero.replaceAll("[^\\d]", "");
  }

  public abstract boolean isValido();

  public String getNumero() {
    return numero;
  }

  public String getNumeroFormatado() {
    return formatar();
  }

  protected abstract String formatar();

  public TipoDocumento getTipo() {
    return tipo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Documento documento = (Documento) o;
    return Objects.equals(numero, documento.numero) && tipo == documento.tipo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero, tipo);
  }

  @Override
  public String toString() {
    return tipo.name() + ": " + getNumeroFormatado();
  }

  // Factory Method
  public static Documento criar(String numero, TipoDocumento tipo) {
    return switch (tipo) {
      case CPF -> new Cpf(numero);
    };
  }
}