package org.br.ltec.crmbackend.crm.paciente.application.command;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Documento;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Sexo;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Telefone;

import java.time.LocalDate;
import java.util.List;

public class CreatePacienteCommand {

  private String primeiroNome;
  private String sobrenome;
  private String documentoNumero;
  private Documento.TipoDocumento documentoTipo;
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

  public CreatePacienteCommand() {
  }

  public CreatePacienteCommand(
          String primeiroNome,
          String sobrenome,
          String documentoNumero,
          Documento.TipoDocumento documentoTipo,
          String email,
          LocalDate dataNascimento,
          Sexo.Tipo sexo,
          String logradouro,
          String numero,
          String complemento,
          String bairro,
          String cidade,
          String estado,
          String cep,
          List<TelefoneRequest> telefones) {
    this.primeiroNome = primeiroNome;
    this.sobrenome = sobrenome;
    this.documentoNumero = documentoNumero;
    this.documentoTipo = documentoTipo;
    this.email = email;
    this.dataNascimento = dataNascimento;
    this.sexo = sexo;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
    this.telefones = telefones;
  }

  // Getters e Setters
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

  public String getDocumentoNumero() {
    return documentoNumero;
  }

  public void setDocumentoNumero(String documentoNumero) {
    this.documentoNumero = documentoNumero;
  }

  public Documento.TipoDocumento getDocumentoTipo() {
    return documentoTipo;
  }

  public void setDocumentoTipo(Documento.TipoDocumento documentoTipo) {
    this.documentoTipo = documentoTipo;
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

  // Inner class para telefones
  public static class TelefoneRequest {
    private String numero;
    private Telefone.TipoTelefone tipo;
    private boolean isWhatsApp;

    public TelefoneRequest() {
    }

    public TelefoneRequest(String numero, Telefone.TipoTelefone tipo, boolean isWhatsApp) {
      this.numero = numero;
      this.tipo = tipo;
      this.isWhatsApp = isWhatsApp;
    }

    // Getters e Setters
    public String getNumero() {
      return numero;
    }

    public void setNumero(String numero) {
      this.numero = numero;
    }

    public Telefone.TipoTelefone getTipo() {
      return tipo;
    }

    public void setTipo(Telefone.TipoTelefone tipo) {
      this.tipo = tipo;
    }

    public boolean isWhatsApp() {
      return isWhatsApp;
    }

    public void setWhatsApp(boolean whatsApp) {
      isWhatsApp = whatsApp;
    }
  }
}