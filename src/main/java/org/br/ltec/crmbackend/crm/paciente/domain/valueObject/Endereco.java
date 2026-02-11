package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Endereco {
  private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

  private final String logradouro;
  private final String numero;
  private final String complemento;
  private final String bairro;
  private final String cidade;
  private final String estado;
  private final String cep;
  private final String pais;

  public Endereco(String logradouro, String numero, String complemento,
                  String bairro, String cidade, String estado, String cep) {
    this(logradouro, numero, complemento, bairro, cidade, estado, cep, "Brasil");
  }

  public Endereco(String logradouro, String numero, String complemento,
                  String bairro, String cidade, String estado, String cep, String pais) {
    validar(logradouro, numero, bairro, cidade, estado, cep, pais);

    this.logradouro = logradouro.trim();
    this.numero = numero.trim();
    this.complemento = complemento != null ? complemento.trim() : "";
    this.bairro = bairro.trim();
    this.cidade = cidade.trim();
    this.estado = estado.trim().toUpperCase();
    this.cep = formatarCEP(cep);
    this.pais = pais != null ? pais.trim() : "Brasil";
  }

  private void validar(String logradouro, String numero, String bairro,
                       String cidade, String estado, String cep, String pais) {

    if (logradouro == null || logradouro.trim().isEmpty()) {
      throw new IllegalArgumentException("Logradouro é obrigatório");
    }

    if (numero == null || numero.trim().isEmpty()) {
      throw new IllegalArgumentException("Número é obrigatório");
    }

    if (bairro == null || bairro.trim().isEmpty()) {
      throw new IllegalArgumentException("Bairro é obrigatório");
    }

    if (cidade == null || cidade.trim().isEmpty()) {
      throw new IllegalArgumentException("Cidade é obrigatória");
    }

    if (estado == null || estado.trim().isEmpty()) {
      throw new IllegalArgumentException("Estado é obrigatório");
    }

    if (estado.length() != 2) {
      throw new IllegalArgumentException("Estado deve ser sigla de 2 caracteres");
    }

    if (cep == null || cep.trim().isEmpty()) {
      throw new IllegalArgumentException("CEP é obrigatório");
    }

    if (!CEP_PATTERN.matcher(limparCEP(cep)).matches()) {
      throw new IllegalArgumentException("CEP inválido: " + cep);
    }

    if (pais == null || pais.trim().isEmpty()) {
      throw new IllegalArgumentException("País é obrigatório");
    }
  }

  private String limparCEP(String cep) {
    return cep.replaceAll("[^\\d]", "");
  }

  private String formatarCEP(String cep) {
    String cepLimpo = limparCEP(cep);
    if (cepLimpo.length() == 8) {
      return cepLimpo.substring(0, 5) + "-" + cepLimpo.substring(5);
    }
    return cepLimpo;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public String getEstado() {
    return estado;
  }

  public String getCep() {
    return cep;
  }

  public String getPais() {
    return pais;
  }

  public boolean isBrasil() {
    return "Brasil".equalsIgnoreCase(pais);
  }

  public String getEnderecoCompleto() {
    StringBuilder sb = new StringBuilder();
    sb.append(logradouro).append(", ").append(numero);

    if (!complemento.isEmpty()) {
      sb.append(" - ").append(complemento);
    }

    sb.append(" - ").append(bairro)
            .append("\n").append(cidade).append(" - ").append(estado)
            .append("\nCEP: ").append(cep);

    if (!isBrasil()) {
      sb.append("\n").append(pais);
    }

    return sb.toString();
  }

  public String getEnderecoResumido() {
    return String.format("%s, %s - %s, %s/%s",
            logradouro, numero, bairro, cidade, estado);
  }

  public Endereco comComplemento(String complemento) {
    return new Endereco(logradouro, numero, complemento,
            bairro, cidade, estado, cep, pais);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Endereco endereco = (Endereco) o;
    return Objects.equals(logradouro, endereco.logradouro) &&
            Objects.equals(numero, endereco.numero) &&
            Objects.equals(complemento, endereco.complemento) &&
            Objects.equals(bairro, endereco.bairro) &&
            Objects.equals(cidade, endereco.cidade) &&
            Objects.equals(estado, endereco.estado) &&
            Objects.equals(cep, endereco.cep) &&
            Objects.equals(pais, endereco.pais);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logradouro, numero, complemento, bairro, cidade, estado, cep, pais);
  }

  @Override
  public String toString() {
    return getEnderecoResumido();
  }
}