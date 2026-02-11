package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class DataNascimento {
  private static final int IDADE_MINIMA = 0;
  private static final int IDADE_MAXIMA = 150;

  private final LocalDate valor;

  public DataNascimento(LocalDate data) {
    validar(data);
    this.valor = data;
  }

  private void validar(LocalDate data) {
    if (data == null) {
      throw new IllegalArgumentException("Data de nascimento não pode ser nula");
    }

    if (data.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
    }

    int idade = calcularIdade(data);

    if (idade < IDADE_MINIMA || idade > IDADE_MAXIMA) {
      throw new IllegalArgumentException(
              String.format("Idade deve estar entre %d e %d anos", IDADE_MINIMA, IDADE_MAXIMA)
      );
    }
  }

  public int getIdade() {
    return calcularIdade(valor);
  }

  public int getIdadeEmMeses() {
    return Period.between(valor, LocalDate.now()).getMonths() +
            Period.between(valor, LocalDate.now()).getYears() * 12;
  }

  public boolean isMaiorDeIdade() {
    return getIdade() >= 18;
  }

  public boolean isIdoso() {
    return getIdade() >= 60;
  }

  private int calcularIdade(LocalDate dataNascimento) {
    return Period.between(dataNascimento, LocalDate.now()).getYears();
  }

  public LocalDate getValor() {
    return valor;
  }

  public String formatar() {
    return valor.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataNascimento that = (DataNascimento) o;
    return Objects.equals(valor, that.valor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valor);
  }

  @Override
  public String toString() {
    return valor.toString();
  }
}