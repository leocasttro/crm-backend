package org.br.ltec.crmbackend.crm.paciente.domain.model;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;

import java.util.ArrayList;
import java.util.List;

public class PacienteBuilder {
  private PacienteId id;
  private NomeCompleto nome;
  private Documento documento;
  private Email email;
  private DataNascimento dataNascimento;
  private Sexo sexo;
  private Endereco endereco;
  private List<Telefone> telefones;

  public PacienteBuilder() {
    this.telefones = new ArrayList<>();
  }

  public PacienteBuilder comId(PacienteId id) {
    this.id = id;
    return this;
  }

  public PacienteBuilder comNome(NomeCompleto nome) {
    this.nome = nome;
    return this;
  }

  public PacienteBuilder comDocumento(Documento documento) {
    this.documento = documento;
    return this;
  }

  public PacienteBuilder comEmail(Email email) {
    this.email = email;
    return this;
  }

  public PacienteBuilder comDataNascimento(DataNascimento dataNascimento) {
    this.dataNascimento = dataNascimento;
    return this;
  }

  public PacienteBuilder comSexo(Sexo sexo) {
    this.sexo = sexo;
    return this;
  }

  public PacienteBuilder comEndereco(Endereco endereco) {
    this.endereco = endereco;
    return this;
  }

  public PacienteBuilder adicionarTelefone(Telefone telefone) {
    this.telefones.add(telefone);
    return this;
  }

  public PacienteBuilder comTelefones(List<Telefone> telefones) {
    this.telefones = new ArrayList<>(telefones);
    return this;
  }

  public Paciente build() {
    return new Paciente(id, nome, documento, email,
            dataNascimento, sexo, endereco, telefones);
  }
}