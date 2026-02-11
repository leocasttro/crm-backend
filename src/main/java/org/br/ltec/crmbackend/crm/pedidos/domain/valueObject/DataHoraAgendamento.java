package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataHoraAgendamento {
  private final LocalDateTime dataHora;
  private final String sala;
  private final Integer duracaoEstimada; // em minutos
  private final String observacoes;

  public DataHoraAgendamento(LocalDateTime dataHora) {
    this(dataHora, null, null, null);
  }

  public DataHoraAgendamento(LocalDateTime dataHora, String sala, Integer duracaoEstimada, String observacoes) {
    validar(dataHora);
    this.dataHora = dataHora;
    this.sala = sala != null ? sala.trim() : "";
    this.duracaoEstimada = duracaoEstimada != null && duracaoEstimada > 0 ? duracaoEstimada : null;
    this.observacoes = observacoes != null ? observacoes.trim() : "";
  }

  private void validar(LocalDateTime dataHora) {
    if (dataHora == null) {
      throw new IllegalArgumentException("Data e hora são obrigatórias");
    }

    LocalDateTime agora = LocalDateTime.now();
    if (dataHora.isBefore(agora)) {
      throw new IllegalArgumentException("Data e hora não podem ser no passado");
    }

    // Não permitir agendamentos muito distantes (2 anos)
    LocalDateTime limite = agora.plusYears(2);
    if (dataHora.isAfter(limite)) {
      throw new IllegalArgumentException("Data e hora não podem ser mais de 2 anos no futuro");
    }
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public String getSala() {
    return sala;
  }

  public Integer getDuracaoEstimada() {
    return duracaoEstimada;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public String getDataFormatada() {
    return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public String getHoraFormatada() {
    return dataHora.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  public String getDataHoraFormatada() {
    return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  }

  public boolean hasSala() {
    return sala != null && !sala.trim().isEmpty();
  }

  public boolean hasDuracaoEstimada() {
    return duracaoEstimada != null && duracaoEstimada > 0;
  }

  public boolean hasObservacoes() {
    return observacoes != null && !observacoes.trim().isEmpty();
  }

  public String getDescricaoCompleta() {
    StringBuilder sb = new StringBuilder();
    sb.append(getDataHoraFormatada());

    if (hasSala()) {
      sb.append(" - Sala: ").append(sala);
    }

    if (hasDuracaoEstimada()) {
      sb.append(" - Duração: ").append(duracaoEstimada).append("min");
    }

    if (hasObservacoes()) {
      sb.append(" - ").append(observacoes);
    }

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataHoraAgendamento that = (DataHoraAgendamento) o;
    return Objects.equals(dataHora, that.dataHora) &&
            Objects.equals(sala, that.sala);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataHora, sala);
  }

  @Override
  public String toString() {
    return getDataHoraFormatada();
  }
}