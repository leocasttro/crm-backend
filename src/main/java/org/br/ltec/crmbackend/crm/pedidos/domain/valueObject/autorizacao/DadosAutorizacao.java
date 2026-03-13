package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;
import java.time.LocalDate;

@Value
public class DadosAutorizacao {
  StatusAutorizacao status;
  NumeroGuia numeroGuia;
  SenhaAutorizacao senha;
  ValidadeAutorizacao validade;
  TipoAcomodacao tipoAcomodacao;

  private DadosAutorizacao(Builder builder) {
    this.status = builder.status;
    this.numeroGuia = builder.numeroGuia;
    this.senha = builder.senha;
    this.validade = builder.validade;
    this.tipoAcomodacao = builder.tipoAcomodacao;
  }

  public boolean isAutorizado() {
    return status != null && status.isAutorizado();
  }

  public boolean isAutorizadoParcial() {
    return status != null && status.isAutorizadoParcial();
  }

  public boolean isPendente() {
    return status != null && status.isPendente();
  }

  public boolean isNegado() {
    return status != null && status.isNegado();
  }

  public boolean temAutorizacaoValida() {
    return isAutorizado() && validade.isValida();
  }

  public boolean dadosCompletosParaAutorizacao() {
    if (!isAutorizado() && !isAutorizadoParcial()) {
      return true;
    }

    return numeroGuia != null &&
            numeroGuia.getValor() != null &&
            !numeroGuia.getValor().trim().isEmpty() &&
            validade != null &&
            validade.getValor() != null;
  }

  public static class Builder {
    private StatusAutorizacao status;
    private NumeroGuia numeroGuia;
    private SenhaAutorizacao senha;
    private ValidadeAutorizacao validade;
    private TipoAcomodacao tipoAcomodacao;

    public Builder status(String status) {
      this.status = status != null ? new StatusAutorizacao(status) : null;
      return this;
    }

    public Builder status(StatusAutorizacao status) {
      this.status = status;
      return this;
    }

    public Builder numeroGuia(String numeroGuia) {
      this.numeroGuia = numeroGuia != null ? new NumeroGuia(numeroGuia) : null;
      return this;
    }

    public Builder numeroGuia(NumeroGuia numeroGuia) {
      this.numeroGuia = numeroGuia;
      return this;
    }

    public Builder senha(String senha) {
      this.senha = senha != null ? new SenhaAutorizacao(senha) : null;
      return this;
    }

    public Builder senha(SenhaAutorizacao senha) {
      this.senha = senha;
      return this;
    }

    public Builder validade(LocalDate validade) {
      this.validade = validade != null ? new ValidadeAutorizacao(validade) : null;
      return this;
    }

    public Builder validade(ValidadeAutorizacao validade) {
      this.validade = validade;
      return this;
    }

    public Builder tipoAcomodacao(String tipoAcomodacao) {
      this.tipoAcomodacao = tipoAcomodacao != null ? new TipoAcomodacao(tipoAcomodacao) : null;
      return this;
    }

    public Builder tipoAcomodacao(TipoAcomodacao tipoAcomodacao) {
      this.tipoAcomodacao = tipoAcomodacao;
      return this;
    }

    public DadosAutorizacao build() {
      return new DadosAutorizacao(this);
    }
  }
}
