package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.time.LocalDate;
import java.util.Objects;

public class Convenio {
  private final String nome;
  private final String numeroCarteira;
  private final LocalDate validadeCarteira;
  private final String tipoPlano;

  public Convenio(String nome, String numeroCarteira, LocalDate validadeCarteira, String tipoPlano) {
    validar(nome, numeroCarteira, validadeCarteira);
    this.nome = nome.trim();
    this.numeroCarteira = numeroCarteira.trim();
    this.validadeCarteira = validadeCarteira;
    this.tipoPlano = tipoPlano != null ? tipoPlano.trim() : "";
  }

  private void validar(String nome, String numeroCarteira, LocalDate validadeCarteira) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome do convênio é obrigatório");
    }

    if (numeroCarteira == null || numeroCarteira.trim().isEmpty()) {
      throw new IllegalArgumentException("Número da carteira é obrigatório");
    }

    if (validadeCarteira == null) {
      throw new IllegalArgumentException("Validade da carteira é obrigatória");
    }

    if (validadeCarteira.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Carteira do convênio está vencida");
    }
  }

  public String getNome() {
    return nome;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public LocalDate getValidadeCarteira() {
    return validadeCarteira;
  }

  public String getTipoPlano() {
    return tipoPlano;
  }

  public boolean isValido() {
    return validadeCarteira.isAfter(LocalDate.now()) || validadeCarteira.isEqual(LocalDate.now());
  }

  public String getNumeroFormatado() {
    return numeroCarteira;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Convenio convenio = (Convenio) o;
    return Objects.equals(numeroCarteira, convenio.numeroCarteira) &&
            Objects.equals(nome, convenio.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, numeroCarteira);
  }

  @Override
  public String toString() {
    return nome + " - Carteira: " + numeroCarteira + " (Válida até: " + validadeCarteira + ")";
  }
}