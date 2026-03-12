package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConsultaPreOperatoria {

  private final LocalDateTime dataHora;
  private final String cuidados;
  private final String observacoesEspeciais;

  // Construtor privado
  private ConsultaPreOperatoria(LocalDateTime dataHora, String cuidados,
                                String observacoesEspeciais) {
    this.dataHora = dataHora;
    this.cuidados = cuidados != null ? cuidados.trim() : "";
    this.observacoesEspeciais = observacoesEspeciais != null ? observacoesEspeciais.trim() : "";
  }

  // Factory method para criar nova consulta (COM validação)
  public static ConsultaPreOperatoria criar(LocalDateTime dataHora, String cuidados,
                                            String observacoesEspeciais) {
    validarDataHora(dataHora);
    return new ConsultaPreOperatoria(dataHora, cuidados, observacoesEspeciais);
  }

  // Factory method para leitura do banco (SEM validação)
  @JsonCreator
  public static ConsultaPreOperatoria fromDatabase(
          @JsonProperty("dataHora") LocalDateTime dataHora,
          @JsonProperty("cuidados") String cuidados,
          @JsonProperty("observacoesEspeciais") String observacoesEspeciais) {

    return new ConsultaPreOperatoria(dataHora, cuidados, observacoesEspeciais);
  }

  private static void validarDataHora(LocalDateTime dataHora) {
    if (dataHora == null) {
      throw new IllegalArgumentException("Data e hora da consulta são obrigatórias");
    }
    if (dataHora.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Data da consulta não pode ser no passado");
    }
  }

  // Getters
  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public String getCuidados() {
    return cuidados;
  }

  public String getObservacoesEspeciais() {
    return observacoesEspeciais;
  }

  // Métodos de conveniência
  public String getDataFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
  }

  public String getHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
  }

  public String getDataHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
  }

  // Método simples para verificar se existe
  public boolean existe() {
    return dataHora != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConsultaPreOperatoria that = (ConsultaPreOperatoria) o;
    return Objects.equals(dataHora, that.dataHora);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataHora);
  }

  @Override
  public String toString() {
    if (dataHora == null) return "Sem consulta agendada";
    return getDataHoraFormatada();
  }
}