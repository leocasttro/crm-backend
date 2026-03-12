package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataHoraAgendamento {
  private final LocalDateTime dataHora;
  private final String sala;
  private final Integer duracaoEstimada;
  private final String observacoes;

  // 🔥 Construtor privado sem validação (usado apenas pelo JPA/Jackson)
  private DataHoraAgendamento(LocalDateTime dataHora, String sala, Integer duracaoEstimada, String observacoes) {
    this.dataHora = dataHora;
    this.sala = sala != null ? sala.trim() : "";
    this.duracaoEstimada = duracaoEstimada != null && duracaoEstimada > 0 ? duracaoEstimada : null;
    this.observacoes = observacoes != null ? observacoes.trim() : "";
  }

  // 🔥 Construtor público com validação (para novos agendamentos)
  public static DataHoraAgendamento criar(LocalDateTime dataHora, String sala, Integer duracaoEstimada, String observacoes) {
    validar(dataHora);
    return new DataHoraAgendamento(dataHora, sala, duracaoEstimada, observacoes);
  }

  public static DataHoraAgendamento criar(LocalDateTime dataHora) {
    return criar(dataHora, null, null, null);
  }

  // 🔥 Factory method para o JPA/Jackson (SEM VALIDAÇÃO)
  @JsonCreator
  public static DataHoraAgendamento fromDatabase(
          @JsonProperty("dataHora") LocalDateTime dataHora,
          @JsonProperty("sala") String sala,
          @JsonProperty("duracaoEstimada") Integer duracaoEstimada,
          @JsonProperty("observacoes") String observacoes) {

    // Retorna instância sem validar
    return new DataHoraAgendamento(dataHora, sala, duracaoEstimada, observacoes);
  }

  private static void validar(LocalDateTime dataHora) {
    if (dataHora == null) {
      throw new IllegalArgumentException("Data e hora são obrigatórias");
    }

    LocalDateTime agora = LocalDateTime.now();
    if (dataHora.isBefore(agora)) {
      throw new IllegalArgumentException("Data e hora não podem ser no passado");
    }

    LocalDateTime limite = agora.plusYears(2);
    if (dataHora.isAfter(limite)) {
      throw new IllegalArgumentException("Data e hora não podem ser mais de 2 anos no futuro");
    }
  }

  @JsonValue
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
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
  }

  public String getHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
  }

  public String getDataHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
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

  public boolean isDataFutura() {
    return dataHora != null && dataHora.isAfter(LocalDateTime.now());
  }

  public String getDescricaoCompleta() {
    if (dataHora == null) return "Sem agendamento";

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
    return dataHora != null ? getDataHoraFormatada() : "null";
  }
}