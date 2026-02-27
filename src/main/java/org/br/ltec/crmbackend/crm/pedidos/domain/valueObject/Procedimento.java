package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

public class Procedimento {
  private String codigoTUSS;
  private String descricao;
  private String categoria;

  // Construtor padrão OBRIGATÓRIO
  public Procedimento() {
  }

  public Procedimento(String codigoTUSS, String descricao, String categoria) {
    this.codigoTUSS = codigoTUSS;
    this.descricao = descricao;
    this.categoria = categoria;
  }

  // Getters e setters
  public String getCodigoTUSS() { return codigoTUSS; }
  public void setCodigoTUSS(String codigoTUSS) { this.codigoTUSS = codigoTUSS; }

  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }

  public String getCategoria() { return categoria; }
  public void setCategoria(String categoria) { this.categoria = categoria; }
}