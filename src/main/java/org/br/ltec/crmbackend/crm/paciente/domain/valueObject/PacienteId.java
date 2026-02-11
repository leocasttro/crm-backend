package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public class PacienteId {
  private final UUID value;

  private PacienteId(UUID value) {
    if (value == null) {
      throw new IllegalArgumentException("ID não pode ser nulo");
    }
    this.value = value;
  }

  public static PacienteId fromString(String id) {
    try {
      return new PacienteId(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("ID inválido: " + id, e);
    }
  }

  public static PacienteId generate() {
    return new PacienteId(UUID.randomUUID());
  }

  public UUID getValue() {
    return value;
  }

  public String asString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PacienteId that = (PacienteId) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}