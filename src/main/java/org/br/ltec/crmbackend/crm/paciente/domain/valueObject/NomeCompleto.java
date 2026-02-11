package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class NomeCompleto {
  private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]{2,100}$");
  private static final int MIN_LENGTH = 2;
  private static final int MAX_LENGTH = 100;

  private final String primeiroNome;
  private final String sobrenome;
  private final String nomeCompleto;

  public NomeCompleto(String primeiroNome, String sobrenome) {
    validar(primeiroNome, "Primeiro nome");
    validar(sobrenome, "Sobrenome");

    this.primeiroNome = primeiroNome.trim();
    this.sobrenome = sobrenome.trim();
    this.nomeCompleto = this.primeiroNome + " " + this.sobrenome;
  }

  private void validar(String nome, String campo) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException(campo + " não pode ser vazio");
    }

    String nomeTrimmed = nome.trim();

    if (nomeTrimmed.length() < MIN_LENGTH || nomeTrimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
              String.format("%s deve ter entre %d e %d caracteres",
                      campo, MIN_LENGTH, MAX_LENGTH)
      );
    }

    if (!VALID_NAME_PATTERN.matcher(nomeTrimmed).matches()) {
      throw new IllegalArgumentException(
              String.format("%s contém caracteres inválidos: %s", campo, nome)
      );
    }
  }

  public String getPrimeiroNome() {
    return primeiroNome;
  }

  public String getSobrenome() {
    return sobrenome;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getIniciais() {
    return (primeiroNome.charAt(0) + "" + sobrenome.charAt(0)).toUpperCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NomeCompleto that = (NomeCompleto) o;
    return Objects.equals(nomeCompleto, that.nomeCompleto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeCompleto);
  }

  @Override
  public String toString() {
    return nomeCompleto;
  }
}