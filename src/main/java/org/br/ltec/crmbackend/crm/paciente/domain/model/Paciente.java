package org.br.ltec.crmbackend.crm.paciente.domain.model;

import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Paciente {
  private final PacienteId id;
  private NomeCompleto nome;
  private Documento documento;
  private Email email;
  private DataNascimento dataNascimento;
  private Sexo sexo;
  private Endereco endereco;
  private List<Telefone> telefones;

  public Paciente(PacienteId id, NomeCompleto nome, Documento documento,
                  Email email, DataNascimento dataNascimento, Sexo sexo) {
    this(id, nome, documento, email, dataNascimento, sexo, null, new ArrayList<>());
  }

  public Paciente(PacienteId id, NomeCompleto nome, Documento documento,
                  Email email, DataNascimento dataNascimento, Sexo sexo,
                  Endereco endereco, List<Telefone> telefones) {
    this.id = id;
    this.nome = nome;
    this.documento = documento;
    this.email = email;
    this.dataNascimento = dataNascimento;
    this.sexo = sexo;
    this.endereco = endereco;
    this.telefones = telefones != null ? new ArrayList<>(telefones) : new ArrayList<>();
    validarTelefones();
  }

  private void validarCriacao(NomeCompleto nome, Documento documento,
                              Email email, DataNascimento dataNascimento, Sexo sexo) {
    if (nome == null) {
      throw new DomainException("Nome é obrigatório");
    }
    if (dataNascimento == null) {
      throw new DomainException("Data de nascimento é obrigatória");
    }
    if (sexo == null) {
      throw new DomainException("Sexo é obrigatório");
    }
  }

  private void validarTelefones() {
    if (telefones.size() > 5) {
      throw new DomainException("Máximo de 5 telefones por paciente");
    }
  }

  // Métodos de atualização
  public void atualizarNome(NomeCompleto novoNome) {
    if (novoNome == null) {
      throw new DomainException("Nome não pode ser nulo");
    }
    this.nome = novoNome;
  }

  public void atualizarSexo(Sexo novoSexo) {
    if (novoSexo == null) {
      throw new DomainException("Sexo não pode ser nulo");
    }
    this.sexo = novoSexo;
  }

  public void atualizarEndereco(Endereco novoEndereco) {
    this.endereco = novoEndereco;
  }

  // Métodos para telefones
  public void adicionarTelefone(Telefone telefone) {
    if (telefone == null) {
      throw new DomainException("Telefone não pode ser nulo");
    }

    if (telefones.size() >= 5) {
      throw new DomainException("Máximo de 5 telefones atingido");
    }

    telefones.add(telefone);
  }

  public void removerTelefone(Telefone telefone) {
    if (telefone != null) {
      telefones.remove(telefone);
    }
  }

  public void definirComoWhatsApp(Telefone telefone) {
    if (telefone == null || !telefones.contains(telefone)) {
      throw new DomainException("Telefone não encontrado na lista");
    }

    if (!telefone.isCelular()) {
      throw new DomainException("Apenas celulares podem ser WhatsApp");
    }

  }


  public List<Telefone> getTelefonesCelular() {
    return telefones.stream()
            .filter(Telefone::isCelular)
            .toList();
  }

  public List<Telefone> getTelefonesFixo() {
    return telefones.stream()
            .filter(t -> !t.isCelular())
            .toList();
  }

  // Getters
  public PacienteId getId() {
    return id;
  }

  public NomeCompleto getNome() {
    return nome;
  }

  public void setNome(NomeCompleto nomeCompleto) { this.nome = nomeCompleto; }

  public Documento getDocumento() {
    return documento;
  }

  public void setDocumento(Documento cpf) { this.documento = cpf; }

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email email) { this.email = email; }

  public DataNascimento getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(DataNascimento dataNascimento) { this.dataNascimento = dataNascimento; }

  public Sexo getSexo() {
    return sexo;
  }

  public void setSexo(Sexo sexo) { this.sexo = sexo; }

  public Optional<Endereco> getEndereco() {
    return Optional.ofNullable(endereco);
  }

  public void setEndereco(Endereco endereco) { this.endereco = endereco; }

  public List<Telefone> getTelefones() {
    return Collections.unmodifiableList(telefones);
  }

  public void setTelefones(List<Telefone> telefones) { this.telefones = new ArrayList<>(telefones); }

  public boolean isMaiorDeIdade() {
    return dataNascimento.isMaiorDeIdade();
  }

  public boolean isIdoso() {
    return dataNascimento.isIdoso();
  }

  public String getIniciais() {
    return nome.getIniciais();
  }

  public String getPronomeTratamento() {
    return sexo.getPronomeTratamento();
  }

  public boolean possuiEndereco() {
    return endereco != null;
  }

  public void atualizarEndereco(String logradouro, String numero, String complemento,
                                String bairro, String cidade, String estado, String cep) {
    this.endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, estado, cep);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Paciente paciente = (Paciente) o;
    return id.equals(paciente.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}