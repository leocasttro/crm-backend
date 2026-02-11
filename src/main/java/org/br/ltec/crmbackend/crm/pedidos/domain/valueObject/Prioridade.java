package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Arrays;

public class Prioridade {
  public enum Tipo {
    ELETIVA("Eletiva", "Elective"),
    URGENTE("Urgente", "Urgent"),
    EMERGENCIA("Emergência", "Emergency");

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

    public static Tipo fromString(String prioridade) {
      if (prioridade == null) return ELETIVA;

      return Arrays.stream(values())
              .filter(t -> t.name().equalsIgnoreCase(prioridade) ||
                      t.descricao.equalsIgnoreCase(prioridade) ||
                      t.descricaoIngles.equalsIgnoreCase(prioridade))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Prioridade inválida: " + prioridade));
    }
  }

  private final Tipo tipo;
  private final String justificativa;

  public Prioridade(Tipo tipo) {
    this(tipo, null);
  }

  public Prioridade(Tipo tipo, String justificativa) {
    if (tipo == null) {
      throw new IllegalArgumentException("Tipo de prioridade é obrigatório");
    }
    this.tipo = tipo;
    this.justificativa = justificativa != null ? justificativa.trim() : "";
  }

  public Tipo getTipo() {
    return tipo;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public String getDescricao() {
    return tipo.getDescricao();
  }

  public boolean isEmergencia() {
    return tipo == Tipo.EMERGENCIA;
  }

  public boolean isUrgente() {
    return tipo == Tipo.URGENTE;
  }

  public boolean isEletiva() {
    return tipo == Tipo.ELETIVA;
  }

  public int getNivelPrioridade() {
    return switch (tipo) {
      case EMERGENCIA -> 1;
      case URGENTE -> 2;
      case ELETIVA -> 3;
    };
  }

  @Override
  public String toString() {
    return tipo.getDescricao() + (justificativa.isEmpty() ? "" : " - " + justificativa);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Prioridade that = (Prioridade) o;
    return tipo == that.tipo;
  }

  @Override
  public int hashCode() {
    return tipo.hashCode();
  }
}