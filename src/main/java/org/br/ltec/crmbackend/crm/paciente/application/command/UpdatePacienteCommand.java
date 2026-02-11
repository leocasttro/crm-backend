package org.br.ltec.crmbackend.crm.paciente.application.command;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Sexo;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Telefone;

import java.time.LocalDate;
import java.util.List;

public class UpdatePacienteCommand {

  private String pacienteId;
  private String primeiroNome;
  private String sobrenome;
  private String email;
  private LocalDate dataNascimento;
  private Sexo.Tipo sexo;
  private String logradouro;
  private String numero;
  private String complemento;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;
  private List<TelefoneRequest> telefones;

  public UpdatePacienteCommand() {
  }

  // Construtor com campos obrigatórios
  public UpdatePacienteCommand(String pacienteId) {
    this.pacienteId = pacienteId;
  }

  // Getters e Setters
  public String getPacienteId() {
    return pacienteId;
  }

  public void setPacienteId(String pacienteId) {
    this.pacienteId = pacienteId;
  }

  public String getPrimeiroNome() {
    return primeiroNome;
  }

  public void setPrimeiroNome(String primeiroNome) {
    this.primeiroNome = primeiroNome;
  }

  public String getSobrenome() {
    return sobrenome;
  }

  public void setSobrenome(String sobrenome) {
    this.sobrenome = sobrenome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public Sexo.Tipo getSexo() {
    return sexo;
  }

  public void setSexo(Sexo.Tipo sexo) {
    this.sexo = sexo;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public List<TelefoneRequest> getTelefones() {
    return telefones;
  }

  public void setTelefones(List<TelefoneRequest> telefones) {
    this.telefones = telefones;
  }

  // Inner class para telefones (reutilizando do Create)
  public static class TelefoneRequest extends CreatePacienteCommand.TelefoneRequest {
    private String id;

    public TelefoneRequest() {
    }

    public TelefoneRequest(String id, String numero, Telefone.TipoTelefone tipo, boolean isWhatsApp) {
      super(numero, tipo, isWhatsApp);
      this.id = id;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }
}