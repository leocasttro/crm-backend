package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Objects;

public class Procedimento {
  private final String codigoTUSS;
  private final String descricao;
  private final String categoria;

  public Procedimento(String codigoTUSS, String descricao, String categoria) {
    validar(codigoTUSS, descricao);
    this.codigoTUSS = codigoTUSS.trim();
    this.descricao = descricao.trim();
    this.categoria = categoria != null ? categoria.trim() : "";
  }

  private void validar(String codigoTUSS, String descricao) {
    if (codigoTUSS == null || codigoTUSS.trim().isEmpty()) {
      throw new IllegalArgumentException("Código TUSS é obrigatório");
    }
    if (!codigoTUSS.matches("\\d{6,10}")) {
      throw new IllegalArgumentException("Código TUSS deve conter apenas números (6-10 dígitos)");
    }

    if (descricao == null || descricao.trim().isEmpty()) {
      throw new IllegalArgumentException("Descrição do procedimento é obrigatória");
    }
    if (descricao.trim().length() < 5 || descricao.trim().length() > 200) {
      throw new IllegalArgumentException("Descrição deve ter entre 5 e 200 caracteres");
    }
  }

  public String getCodigoTUSS() {
    return codigoTUSS;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getCategoria() {
    return categoria;
  }

  public String getCodigoFormatado() {
    return codigoTUSS;
  }

  public String getDescricaoCompleta() {
    return codigoTUSS + " - " + descricao + (categoria.isEmpty() ? "" : " (" + categoria + ")");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Procedimento that = (Procedimento) o;
    return Objects.equals(codigoTUSS, that.codigoTUSS);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigoTUSS);
  }

  @Override
  public String toString() {
    return getDescricaoCompleta();
  }
}