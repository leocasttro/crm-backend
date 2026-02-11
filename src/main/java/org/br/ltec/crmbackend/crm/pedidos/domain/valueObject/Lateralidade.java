package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Arrays;

public class Lateralidade {
  public enum Tipo {
    DIREITA("Direita", "Right"),
    ESQUERDA("Esquerda", "Left"),
    BILATERAL("Bilateral", "Bilateral"),
    NAO_APLICAVEL("Não Aplicável", "Not Applicable");

    private final String descricao;
    private final String descricaoIngles;

    Tipo(String descricao, String descricaoIngles) {
      this.descricao = descricao;
      this.descricaoIngles = descricaoIngles;
    }

    public String getDescricao() {
      return descricao;
    }

    public String getDescricaoIngles() {
      return descricaoIngles;
    }

    public static Tipo fromString(String lateralidade) {
      if (lateralidade == null) return NAO_APLICAVEL;

      return Arrays.stream(values())
              .filter(t -> t.name().equalsIgnoreCase(lateralidade) ||
                      t.descricao.equalsIgnoreCase(lateralidade) ||
                      t.descricaoIngles.equalsIgnoreCase(lateralidade))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Lateralidade inválida: " + lateralidade));
    }
  }

  private final Tipo tipo;

  public Lateralidade(Tipo tipo) {
    if (tipo == null) {
      throw new IllegalArgumentException("Tipo de lateralidade é obrigatório");
    }
    this.tipo = tipo;
  }

  public Lateralidade(String lateralidade) {
    this(Tipo.fromString(lateralidade));
  }

  public Tipo getTipo() {
    return tipo;
  }

  public String getDescricao() {
    return tipo.getDescricao();
  }

  public boolean isDireita() {
    return tipo == Tipo.DIREITA;
  }

  public boolean isEsquerda() {
    return tipo == Tipo.ESQUERDA;
  }

  public boolean isBilateral() {
    return tipo == Tipo.BILATERAL;
  }

  public boolean isNaoAplicavel() {
    return tipo == Tipo.NAO_APLICAVEL;
  }

  @Override
  public String toString() {
    return tipo.getDescricao();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lateralidade that = (Lateralidade) o;
    return tipo == that.tipo;
  }

  @Override
  public int hashCode() {
    return tipo.hashCode();
  }
}