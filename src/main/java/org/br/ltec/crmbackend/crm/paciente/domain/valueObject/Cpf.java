package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.regex.Pattern;

public class Cpf extends Documento {
  private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
  private static final int[] PESO_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

  public Cpf(String numero) {
    super(numero, TipoDocumento.CPF);
  }

  @Override
  protected void validar(String numero) {
    String numeroLimpo = limparFormatacao(numero);

    if (numeroLimpo == null || numeroLimpo.length() != 11) {
      throw new IllegalArgumentException("CPF deve ter 11 dígitos");
    }

    if (!CPF_PATTERN.matcher(numeroLimpo).matches()) {
      throw new IllegalArgumentException("CPF inválido");
    }

    if (todosDigitosIguais(numeroLimpo)) {
      throw new IllegalArgumentException("CPF inválido - dígitos iguais");
    }

    if (!validarDigitosVerificadores(numeroLimpo)) {
      throw new IllegalArgumentException("CPF inválido - dígitos verificadores incorretos");
    }
  }

  private boolean todosDigitosIguais(String cpf) {
    char primeiroDigito = cpf.charAt(0);
    return cpf.chars().allMatch(digito -> digito == primeiroDigito);
  }

  private boolean validarDigitosVerificadores(String cpf) {
    String base = cpf.substring(0, 9);
    String digitos = cpf.substring(9);

    int digito1 = calcularDigito(base, PESO_CPF);
    int digito2 = calcularDigito(base + digito1, PESO_CPF);

    return digitos.equals(digito1 + "" + digito2);
  }

  private int calcularDigito(String str, int[] peso) {
    int soma = 0;
    for (int i = str.length() - 1; i >= 0; i--) {
      int digito = Integer.parseInt(str.substring(i, i + 1));
      soma += digito * peso[peso.length - str.length() + i];
    }
    soma = 11 - soma % 11;
    return soma > 9 ? 0 : soma;
  }

  @Override
  public boolean isValido() {
    try {
      validar(this.numero);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  protected String formatar() {
    return numero.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
  }
}