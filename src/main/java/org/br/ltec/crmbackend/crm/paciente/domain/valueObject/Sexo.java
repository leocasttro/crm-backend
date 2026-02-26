package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class Sexo {
  public enum Tipo {
    MASCULINO("Masculino", 'M'),
    FEMININO("Feminino", 'F'),
    OUTRO("Outro", 'O'),
    NAO_INFORMADO("Não informado", 'N');

    private final String descricao;
    private final Character codigo;

    Tipo(String descricao, Character codigo) {
      this.descricao = descricao;
      this.codigo = codigo;
    }

    public String getDescricao() {
      return descricao;
    }

    public Character getCodigo() {
      return codigo;
    }

    public static Tipo fromCodigo(Character codigo) {
      if (codigo == null) return NAO_INFORMADO;

      return Arrays.stream(values())
              .filter(t -> t.codigo.equals(Character.toUpperCase(codigo)))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Código de sexo inválido: " + codigo));
    }

    public static Tipo fromDescricao(String descricao) {
      if (descricao == null || descricao.trim().isEmpty()) return NAO_INFORMADO;

      return Arrays.stream(values())
              .filter(t -> t.descricao.equalsIgnoreCase(descricao.trim()))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Descrição de sexo inválida: " + descricao));
    }
  }

  private final Tipo tipo;

  @JsonCreator
  public Sexo(@JsonProperty("tipo") Tipo tipo) {
    this.tipo = tipo != null ? tipo : Tipo.NAO_INFORMADO;
  }

  public Sexo(Character codigo) {
    this.tipo = Tipo.fromCodigo(codigo);
  }

  public Sexo(String descricao) {
    this.tipo = Tipo.fromDescricao(descricao);
  }

  public Tipo getTipo() {
    return tipo;
  }

  public Character getCodigo() {
    return tipo.getCodigo();
  }

  public String getDescricao() {
    return tipo.getDescricao();
  }

  public boolean isMasculino() {
    return tipo == Tipo.MASCULINO;
  }

  public boolean isFeminino() {
    return tipo == Tipo.FEMININO;
  }

  public boolean isInformado() {
    return tipo != Tipo.NAO_INFORMADO;
  }

  public String getPronomeTratamento() {
    return switch (tipo) {
      case MASCULINO -> "Sr.";
      case FEMININO -> "Sra.";
      default -> "";
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sexo sexo = (Sexo) o;
    return tipo == sexo.tipo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo);
  }

  @Override
  public String toString() {
    return tipo.getDescricao();
  }
}