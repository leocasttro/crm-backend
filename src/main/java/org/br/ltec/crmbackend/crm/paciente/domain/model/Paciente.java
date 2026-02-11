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
  private final Documento documento;
  private final Email email;
  private final DataNascimento dataNascimento;
  private Sexo sexo;
  private Endereco endereco;
  private final List<Telefone> telefones;

  public Paciente(PacienteId id, NomeCompleto nome, Documento documento,
                  Email email, DataNascimento dataNascimento, Sexo sexo) {
    this(id, nome, documento, email, dataNascimento, sexo, null, new ArrayList<>());
  }

  public Paciente(PacienteId id, NomeCompleto nome, Documento documento,
                  Email email, DataNascimento dataNascimento, Sexo sexo,
                  Endereco endereco, List<Telefone> telefones) {
    validarCriacao(nome, documento, email, dataNascimento, sexo);
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
    if (documento == null) {
      throw new DomainException("Documento é obrigatório");
    }
    if (email == null) {
      throw new DomainException("Email é obrigatório");
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

    long celularesComWhatsApp = telefones.stream()
            .filter(t -> t.isCelular() && t.isWhatsApp())
            .count();

    if (celularesComWhatsApp > 1) {
      throw new DomainException("Apenas um telefone pode ser configurado como WhatsApp");
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

    if (telefone.isWhatsApp()) {
      removerWhatsApp();
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

    removerWhatsApp();

    int index = telefones.indexOf(telefone);
    Telefone telefoneComWhatsApp = telefone.comWhatsApp(true);
    telefones.set(index, telefoneComWhatsApp);
  }

  private void removerWhatsApp() {
    telefones.replaceAll(t ->
            t.isCelular() && t.isWhatsApp() ? t.comWhatsApp(false) : t
    );
  }

  public Optional<Telefone> getTelefoneWhatsApp() {
    return telefones.stream()
            .filter(Telefone::isWhatsApp)
            .findFirst();
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

  public Documento getDocumento() {
    return documento;
  }

  public Email getEmail() {
    return email;
  }

  public DataNascimento getDataNascimento() {
    return dataNascimento;
  }

  public Sexo getSexo() {
    return sexo;
  }

  public Optional<Endereco> getEndereco() {
    return Optional.ofNullable(endereco);
  }

  public List<Telefone> getTelefones() {
    return Collections.unmodifiableList(telefones);
  }

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

  public boolean possuiWhatsApp() {
    return getTelefoneWhatsApp().isPresent();
  }

  public boolean possuiEndereco() {
    return endereco != null;
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