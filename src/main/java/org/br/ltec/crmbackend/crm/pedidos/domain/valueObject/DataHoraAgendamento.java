package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DataHoraAgendamento {
  private final LocalDateTime dataHora;
  private final String local;
  private final String hospital;
  private final String fornecedor;
  private final String riscoCirurgico;
  private final Integer duracaoEstimada;

  private DataHoraAgendamento(LocalDateTime dataHora,
                              String local, String hospital,
                              String fornecedor, String riscoCirurgico,
                              Integer duracaoEstimada) {
    this.dataHora = dataHora;
    this.local = local != null ? local.trim() : "";
    this.hospital = hospital != null ? hospital.trim() : "";
    this.fornecedor = fornecedor != null ? fornecedor.trim() : "";
    this.riscoCirurgico = riscoCirurgico;
    this.duracaoEstimada = duracaoEstimada != null && duracaoEstimada > 0 ? duracaoEstimada : null;
  }

  public static DataHoraAgendamento criar(LocalDateTime dataHora, String local,
                                          String hospital, String fornecedor,
                                          String riscoCirurgico,
                                          Integer duracaoEstimada) {
    if (dataHora != null) {
      validarDataFutura(dataHora);
    }

    return new DataHoraAgendamento(dataHora, local, hospital, fornecedor,
            riscoCirurgico, duracaoEstimada);
  }

  public static DataHoraAgendamento criar(LocalDateTime dataHora) {
    return criar(dataHora, null, null, null, null, null);
  }

  @JsonCreator
  public static DataHoraAgendamento fromDatabase(
          @JsonProperty("dataHora") LocalDateTime dataHora,
          @JsonProperty("local") String local,
          @JsonProperty("hospital") String hospital,
          @JsonProperty("fornecedor") String fornecedor,
          @JsonProperty("riscoCirurgico") String riscoCirurgico,
          @JsonProperty("duracaoEstimada") Integer duracaoEstimada) {

    return new DataHoraAgendamento(dataHora, local, hospital, fornecedor,
            riscoCirurgico, duracaoEstimada);
  }

  private static void validarDataFutura(LocalDateTime dataHora) {
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

  public String getLocal() {
    return local;
  }

  public String getHospital() {
    return hospital;
  }

  public String getFornecedor() {
    return fornecedor;
  }

  public String getRiscoCirurgico() {
    return riscoCirurgico;
  }

  public Integer getDuracaoEstimada() {
    return duracaoEstimada;
  }

  public String getDataFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
  }

  public String getHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
  }

  public String getDataHoraFormatada() {
    return dataHora != null ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Não agendado";
  }

  public boolean hasDataHora() {
    return dataHora != null;
  }

  public boolean hasLocal() {
    return local != null && !local.trim().isEmpty();
  }

  public boolean hasHospital() {
    return hospital != null && !hospital.trim().isEmpty();
  }

  public boolean hasFornecedor() {
    return fornecedor != null && !fornecedor.trim().isEmpty();
  }

  public boolean hasRiscoCirurgico() {
    return riscoCirurgico != null && !riscoCirurgico.trim().isEmpty();
  }

  public boolean hasDuracaoEstimada() {
    return duracaoEstimada != null && duracaoEstimada > 0;
  }

  public boolean isDataFutura() {
    return dataHora != null && dataHora.isAfter(LocalDateTime.now());
  }

  public boolean isAgendamentoCompleto() {
    return hasDataHora() && hasHospital() && hasLocal();
  }

  public boolean temAlgumDado() {
    return hasDataHora() || hasLocal() || hasHospital() || hasFornecedor() ||
            hasRiscoCirurgico() || hasDuracaoEstimada();
  }

  public String getSala() {
    if (!hasLocal()) return "";

    String localLower = local.toLowerCase();

    if (localLower.contains("sala")) {
      int idxSala = localLower.indexOf("sala");
      int idxVirgula = local.indexOf(",", idxSala);
      int idxProxEspaco = local.indexOf(" ", idxSala + 5);

      if (idxVirgula > idxSala) {
        return local.substring(idxSala, idxVirgula).trim();
      } else if (idxProxEspaco > idxSala) {
        return local.substring(idxSala, idxProxEspaco).trim();
      }
    }

    return "";
  }

  public String getAndar() {
    if (!hasLocal()) return "";

    String localLower = local.toLowerCase();

    if (localLower.contains("andar")) {
      int idxAndar = localLower.indexOf("andar");

      for (int i = idxAndar - 5; i < idxAndar; i++) {
        if (i >= 0 && (local.charAt(i) == ',' || local.charAt(i) == ' ')) {
          return local.substring(i + 1, idxAndar + 5).trim();
        }
      }

      return local.substring(Math.max(0, idxAndar - 5), idxAndar + 5).trim();
    }

    return "";
  }

  public String getLocalCompleto() {
    StringBuilder sb = new StringBuilder();

    if (hasHospital()) {
      sb.append(hospital);
    }

    if (hasLocal()) {
      if (sb.length() > 0) sb.append(" - ");
      sb.append(local);
    }

    return sb.length() > 0 ? sb.toString() : "Local não definido";
  }

  public String getDescricaoCompleta() {
    if (!hasDataHora() && !temAlgumDado()) {
      return "Sem agendamento";
    }

    StringBuilder sb = new StringBuilder();

    if (hasDataHora()) {
      sb.append(getDataHoraFormatada());
    } else {
      sb.append("Data a definir");
    }

    String localCompleto = getLocalCompleto();
    if (!localCompleto.equals("Local não definido")) {
      sb.append(" - ").append(localCompleto);
    }

    if (hasFornecedor()) {
      sb.append(" - Fornecedor: ").append(fornecedor);
    }

    if (hasRiscoCirurgico()) {
      sb.append(" - Risco: ").append(riscoCirurgico);
    }

    if (hasDuracaoEstimada()) {
      sb.append(" - ").append(duracaoEstimada).append("min");
    }

    return sb.toString();
  }

  public String getResumo() {
    if (!hasDataHora() && !hasHospital() && !hasLocal()) {
      return "Agendamento incompleto";
    }

    StringBuilder sb = new StringBuilder();

    if (hasDataHora()) {
      sb.append(getDataFormatada()).append(" ").append(getHoraFormatada());
    } else {
      sb.append("Data pendente");
    }

    if (hasHospital()) {
      sb.append(" - ").append(hospital);
    } else if (hasLocal()) {
      sb.append(" - ").append(local);
    }

    return sb.toString();
  }

  public String getStatusAgendamento() {
    if (!hasDataHora() && !temAlgumDado()) {
      return "PENDENTE";
    }

    if (isAgendamentoCompleto()) {
      return "AGENDADO";
    }

    if (hasDataHora() || hasHospital()) {
      return "PARCIAL";
    }

    return "RASCUNHO";
  }

  public boolean isBefore(DataHoraAgendamento outro) {
    if (outro == null || !outro.hasDataHora() || !this.hasDataHora()) return false;
    return this.dataHora.isBefore(outro.getDataHora());
  }

  public boolean isAfter(DataHoraAgendamento outro) {
    if (outro == null || !outro.hasDataHora() || !this.hasDataHora()) return false;
    return this.dataHora.isAfter(outro.getDataHora());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataHoraAgendamento that = (DataHoraAgendamento) o;
    return Objects.equals(dataHora, that.dataHora) &&
            Objects.equals(local, that.local) &&
            Objects.equals(hospital, that.hospital);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataHora, local, hospital);
  }

  @Override
  public String toString() {
    return getDescricaoCompleta();
  }
}