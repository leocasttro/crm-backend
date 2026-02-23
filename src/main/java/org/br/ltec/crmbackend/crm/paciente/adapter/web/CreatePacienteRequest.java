package org.br.ltec.crmbackend.crm.paciente.adapter.web;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Documento;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Sexo;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Telefone;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePacienteRequest {

  @NotBlank(message = "Primeiro nome é obrigatório")
  @Size(min = 2, max = 50, message = "Primeiro nome deve ter entre 2 e 50 caracteres")
  private String primeiroNome;

  @NotBlank(message = "Sobrenome é obrigatório")
  @Size(min = 2, max = 50, message = "Sobrenome deve ter entre 2 e 50 caracteres")
  private String sobrenome;

  private String documentoNumero;

  private Documento.TipoDocumento documentoTipo;

  @Email(message = "Email deve ser válido")
  private String email;

  @NotNull(message = "Data de nascimento é obrigatória")
  @Past(message = "Data de nascimento deve ser no passado")
  private LocalDate dataNascimento;

  @NotNull(message = "Sexo é obrigatório")
  private Sexo.Tipo sexo;

  // Endereço (opcional)
  private String logradouro;
  private String numero;
  private String complemento;
  private String bairro;
  private String cidade;
  private String estado;

  @Pattern(regexp = "\\d{5}-\\d{3}|\\d{8}", message = "CEP deve ter formato 00000-000 ou 00000000")
  private String cep;

  // Telefones (opcional)
  private List<TelefoneRequest> telefones;

  // Converter para Command
  public CreatePacienteCommand toCommand() {
    List<CreatePacienteCommand.TelefoneRequest> telefoneCommands = null;
    if (telefones != null) {
      telefoneCommands = telefones.stream()
              .map(t -> new CreatePacienteCommand.TelefoneRequest(
                      t.getNumero(),
                      t.getTipo(),
                      t.isWhatsApp()))
              .collect(Collectors.toList());
    }

    return new CreatePacienteCommand(
            primeiroNome,
            sobrenome,
            documentoNumero,
            documentoTipo,
            email,
            dataNascimento,
            sexo,
            logradouro,
            numero,
            complemento,
            bairro,
            cidade,
            estado,
            cep,
            telefoneCommands
    );
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

  // Classe interna para telefones
  public static class TelefoneRequest {
    @NotBlank(message = "Número do telefone é obrigatório")
    @Pattern(regexp = "\\(?\\d{2}\\)?\\s*\\d{4,5}-?\\d{4}",
            message = "Telefone inválido. Use formato (00) 00000-0000 ou (00) 0000-0000")
    private String numero;

    @NotNull(message = "Tipo de telefone é obrigatório")
    private Telefone.TipoTelefone tipo;

    private boolean whatsApp;

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
      return whatsApp;
    }

    public void setWhatsApp(boolean whatsApp) {
      this.whatsApp = whatsApp;
    }
  }
}