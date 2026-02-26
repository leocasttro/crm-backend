package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoDocumento"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cpf.class, name = "CPF")
})
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

  // Factory Method para Jackson
  @JsonCreator
  public static Documento criar(
          @JsonProperty("numero") String numero,
          @JsonProperty("tipoDocumento") TipoDocumento tipo) {
    return switch (tipo) {
      case CPF -> new Cpf(numero);
    };
  }
}