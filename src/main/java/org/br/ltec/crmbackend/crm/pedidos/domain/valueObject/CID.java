package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class CID {
  private static final Pattern CID_PATTERN = Pattern.compile("^[A-Z]\\d{2,3}(\\.\\d)?$");

  private final String codigo;
  private final String descricao;

  public CID(String codigo, String descricao) {
    validar(codigo);
    this.codigo = codigo.trim().toUpperCase();
    this.descricao = descricao != null ? descricao.trim() : "";
  }

  private void validar(String codigo) {
    if (codigo == null || codigo.trim().isEmpty()) {
      throw new IllegalArgumentException("Código CID é obrigatório");
    }

    String codigoLimpo = codigo.trim().toUpperCase();
    if (!CID_PATTERN.matcher(codigoLimpo).matches()) {
      throw new IllegalArgumentException("Código CID inválido. Formato esperado: A00.0");
    }
  }

  public String getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getCodigoFormatado() {
    return codigo;
  }

  public String getDescricaoCompleta() {
    return codigo + (descricao.isEmpty() ? "" : " - " + descricao);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CID cid = (CID) o;
    return Objects.equals(codigo, cid.codigo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigo);
  }

  @Override
  public String toString() {
    return getDescricaoCompleta();
  }
}