package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.time.LocalDate;
import java.util.Objects;

public class Convenio {

  private String nome;
  private String numeroCarteira;
  private LocalDate validade;
  private String tipoPlano;

  public Convenio(String nome, String numeroCarteira, LocalDate validade, String tipoPlano) {
    // ✅ Tratar valores null
    this.nome = nome != null ? nome.trim() : null;
    this.numeroCarteira = numeroCarteira != null ? numeroCarteira.trim() : null;
    this.validade = validade;
    this.tipoPlano = tipoPlano;
  }

  // Getters
  public String getNome() {
    return nome;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public LocalDate getValidade() {
    return validade;
  }

  public String getTipoPlano() {
    return tipoPlano;
  }
}