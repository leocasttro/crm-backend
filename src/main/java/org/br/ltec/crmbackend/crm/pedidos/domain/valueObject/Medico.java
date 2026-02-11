package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Objects;

public class Medico {
  private final String nome;
  private final String crm;
  private final String especialidade;

  public Medico(String nome, String crm, String especialidade) {
    validar(nome, crm, especialidade);
    this.nome = nome.trim();
    this.crm = crm.trim();
    this.especialidade = especialidade != null ? especialidade.trim() : "";
  }

  private void validar(String nome, String crm, String especialidade) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome do médico é obrigatório");
    }
    if (nome.trim().length() < 3 || nome.trim().length() > 100) {
      throw new IllegalArgumentException("Nome do médico deve ter entre 3 e 100 caracteres");
    }

    if (crm == null || crm.trim().isEmpty()) {
      throw new IllegalArgumentException("CRM é obrigatório");
    }
    if (!crm.matches("\\d{4,10}/[A-Z]{2}")) {
      throw new IllegalArgumentException("CRM deve estar no formato 0000/UF");
    }
  }

  public String getNome() {
    return nome;
  }

  public String getCrm() {
    return crm;
  }

  public String getEspecialidade() {
    return especialidade;
  }

  public String getCrmFormatado() {
    return crm;
  }

  public String getNomeCompleto() {
    return nome + (especialidade.isEmpty() ? "" : " - " + especialidade);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Medico medico = (Medico) o;
    return Objects.equals(crm, medico.crm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crm);
  }

  @Override
  public String toString() {
    return nome + " - CRM: " + crm;
  }
}