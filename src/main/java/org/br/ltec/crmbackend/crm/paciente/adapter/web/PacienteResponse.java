package org.br.ltec.crmbackend.crm.paciente.adapter.web;

import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteResponse {

  private String id;
  private String nomeCompleto;
  private String primeiroNome;
  private String sobrenome;
  private String documento;
  private String documentoTipo;
  private String email;
  private LocalDate dataNascimento;
  private int idade;
  private String sexo;
  private String sexoCodigo;
  private EnderecoResponse endereco;
  private List<TelefoneResponse> telefones;
  private boolean maiorDeIdade;
  private boolean idoso;
  private String iniciais;
  private String pronomeTratamento;
  private boolean possuiWhatsApp;
  private boolean possuiEndereco;

  // Construtor privado para usar factory method
  private PacienteResponse() {
  }

  public static PacienteResponse fromDomain(Paciente paciente) {
    PacienteResponse response = new PacienteResponse();

    response.id = paciente.getId().asString();
    response.nomeCompleto = paciente.getNome().getNomeCompleto();
    response.primeiroNome = paciente.getNome().getPrimeiroNome();
    response.sobrenome = paciente.getNome().getSobrenome();
    response.documento = paciente.getDocumento().getNumeroFormatado();
    response.documentoTipo = paciente.getDocumento().getTipo().name();
    response.email = paciente.getEmail().getEndereco();
    response.dataNascimento = paciente.getDataNascimento().getValor();
    response.idade = paciente.getDataNascimento().getIdade();
    response.sexo = paciente.getSexo().getDescricao();
    response.sexoCodigo = paciente.getSexo().getCodigo().toString();
    response.maiorDeIdade = paciente.isMaiorDeIdade();
    response.idoso = paciente.isIdoso();
    response.iniciais = paciente.getIniciais();
    response.pronomeTratamento = paciente.getPronomeTratamento();
    response.possuiWhatsApp = paciente.possuiWhatsApp();
    response.possuiEndereco = paciente.possuiEndereco();

    // Endereço (pode ser null)
    if (paciente.getEndereco().isPresent()) {
      response.endereco = EnderecoResponse.fromDomain(paciente.getEndereco().get());
    }

    // Telefones
    response.telefones = paciente.getTelefones().stream()
            .map(TelefoneResponse::fromDomain)
            .collect(Collectors.toList());

    return response;
  }

  // Getters
  public String getId() {
    return id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getPrimeiroNome() {
    return primeiroNome;
  }

  public String getSobrenome() {
    return sobrenome;
  }

  public String getDocumento() {
    return documento;
  }

  public String getDocumentoTipo() {
    return documentoTipo;
  }

  public String getEmail() {
    return email;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public int getIdade() {
    return idade;
  }

  public String getSexo() {
    return sexo;
  }

  public String getSexoCodigo() {
    return sexoCodigo;
  }

  public EnderecoResponse getEndereco() {
    return endereco;
  }

  public List<TelefoneResponse> getTelefones() {
    return telefones;
  }

  public boolean isMaiorDeIdade() {
    return maiorDeIdade;
  }

  public boolean isIdoso() {
    return idoso;
  }

  public String getIniciais() {
    return iniciais;
  }

  public String getPronomeTratamento() {
    return pronomeTratamento;
  }

  public boolean isPossuiWhatsApp() {
    return possuiWhatsApp;
  }

  public boolean isPossuiEndereco() {
    return possuiEndereco;
  }

  // Classes internas para telefone e endereço
  public static class TelefoneResponse {
    private String numero;
    private String numeroFormatado;
    private String tipo;
    private boolean whatsApp;
    private String ddd;

    public static TelefoneResponse fromDomain(Telefone telefone) {
      TelefoneResponse response = new TelefoneResponse();
      response.numero = telefone.getNumero();
      response.numeroFormatado = telefone.getNumeroFormatado();
      response.tipo = telefone.getTipo().name();
      response.whatsApp = telefone.isWhatsApp();
      response.ddd = telefone.getDDD();
      return response;
    }

    // Getters
    public String getNumero() {
      return numero;
    }

    public String getNumeroFormatado() {
      return numeroFormatado;
    }

    public String getTipo() {
      return tipo;
    }

    public boolean isWhatsApp() {
      return whatsApp;
    }

    public String getDdd() {
      return ddd;
    }
  }

  public static class EnderecoResponse {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
    private String enderecoCompleto;
    private String enderecoResumido;

    public static EnderecoResponse fromDomain(Endereco endereco) {
      EnderecoResponse response = new EnderecoResponse();
      response.logradouro = endereco.getLogradouro();
      response.numero = endereco.getNumero();
      response.complemento = endereco.getComplemento();
      response.bairro = endereco.getBairro();
      response.cidade = endereco.getCidade();
      response.estado = endereco.getEstado();
      response.cep = endereco.getCep();
      response.pais = endereco.getPais();
      response.enderecoCompleto = endereco.getEnderecoCompleto();
      response.enderecoResumido = endereco.getEnderecoResumido();
      return response;
    }

    // Getters
    public String getLogradouro() {
      return logradouro;
    }

    public String getNumero() {
      return numero;
    }

    public String getComplemento() {
      return complemento;
    }

    public String getBairro() {
      return bairro;
    }

    public String getCidade() {
      return cidade;
    }

    public String getEstado() {
      return estado;
    }

    public String getCep() {
      return cep;
    }

    public String getPais() {
      return pais;
    }

    public String getEnderecoCompleto() {
      return enderecoCompleto;
    }

    public String getEnderecoResumido() {
      return enderecoResumido;
    }
  }
}