package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Arrays;
import java.util.Objects;

public class StatusPedido {
  public enum Tipo {
    RASCUNHO("Rascunho", "Draft"),
    PENDENTE("Pendente", "Pending"),
    EM_ANALISE("Em Análise", "In Analysis"),
    AGENDADO("Agendado", "Scheduled"),
    CONFIRMADO("Confirmado", "Confirmed"),
    EM_PROGRESSO("Em Progresso", "In Progress"),
    REALIZADO("Realizado", "Completed"),
    CANCELADO("Cancelado", "Cancelled"),
    REJEITADO("Rejeitado", "Rejected");

    private final String descricao;
    private final String descricaoIngles;

    Tipo(String descricao, String descricaoIngles) {
      this.descricao = descricao;
      this.descricaoIngles = descricaoIngles;
    }

    public String getDescricao() {
      return descricao;
    }

    public String getDescricaoIngles() {
      return descricaoIngles;
    }

    public boolean podeSerAtualizadoPara(Tipo novoStatus) {
      return switch (this) {
        case RASCUNHO -> novoStatus == PENDENTE || novoStatus == CANCELADO;
        case PENDENTE -> novoStatus == EM_ANALISE || novoStatus == CANCELADO;
        case EM_ANALISE -> novoStatus == AGENDADO || novoStatus == REJEITADO || novoStatus == CANCELADO;
        case AGENDADO -> novoStatus == CONFIRMADO || novoStatus == CANCELADO;
        case CONFIRMADO -> novoStatus == EM_PROGRESSO || novoStatus == CANCELADO;
        case EM_PROGRESSO -> novoStatus == REALIZADO || novoStatus == CANCELADO;
        case REALIZADO -> false; // Não pode mudar após realizado
        case CANCELADO -> false; // Não pode mudar após cancelado
        case REJEITADO -> false; // Não pode mudar após rejeitado
      };
    }

    public boolean isFinal() {
      return this == REALIZADO || this == CANCELADO || this == REJEITADO;
    }

    public boolean isAtivo() {
      return !isFinal();
    }

    public static Tipo fromString(String status) {
      if (status == null) return RASCUNHO;

      return Arrays.stream(values())
              .filter(t -> t.name().equalsIgnoreCase(status) || t.descricao.equalsIgnoreCase(status))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + status));
    }
  }

  private final Tipo tipo;
  private final String observacao;
  private final String usuarioAlteracao;

  public StatusPedido(Tipo tipo) {
    this(tipo, null, null);
  }

  public StatusPedido(Tipo tipo, String observacao, String usuarioAlteracao) {
    if (tipo == null) {
      throw new IllegalArgumentException("Tipo de status é obrigatório");
    }
    this.tipo = tipo;
    this.observacao = observacao != null ? observacao.trim() : "";
    this.usuarioAlteracao = usuarioAlteracao != null ? usuarioAlteracao.trim() : "";
  }

  // Getters
  public Tipo getTipo() {
    return tipo;
  }

  public String getObservacao() {
    return observacao;
  }

  public String getUsuarioAlteracao() {
    return usuarioAlteracao;
  }

  public String getDescricao() {
    return tipo.getDescricao();
  }

  public boolean isFinal() {
    return tipo.isFinal();
  }

  public boolean isAtivo() {
    return tipo.isAtivo();
  }

  public boolean podeSerAtualizadoPara(StatusPedido novoStatus) {
    return this.tipo.podeSerAtualizadoPara(novoStatus.tipo);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatusPedido that = (StatusPedido) o;
    return tipo == that.tipo &&
            Objects.equals(observacao, that.observacao) &&
            Objects.equals(usuarioAlteracao, that.usuarioAlteracao);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, observacao, usuarioAlteracao);
  }

  @Override
  public String toString() {
    return tipo.getDescricao() +
            (observacao.isEmpty() ? "" : " - " + observacao) +
            (usuarioAlteracao.isEmpty() ? "" : " (por " + usuarioAlteracao + ")");
  }
}