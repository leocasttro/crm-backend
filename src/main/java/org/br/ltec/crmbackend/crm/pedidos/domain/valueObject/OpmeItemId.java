package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public class OpmeItemId {
  private final UUID value;

  private OpmeItemId(UUID value) {
    if (value == null) {
      throw new IllegalArgumentException("ID do OPME não pode ser nulo");
    }
    this.value = value;
  }

  public static OpmeItemId fromString(String id) {
    try {
      return new OpmeItemId(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("ID do OPME inválido: " + id, e);
    }
  }

  public static OpmeItemId generate() { return new OpmeItemId(UUID.randomUUID()); }

  public UUID getValue() { return value; }

  public String asString() { return value.toString(); }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    OpmeItemId opmeItemId = (OpmeItemId) object;
    return Objects.equals(value, opmeItemId.value);
  }

  @Override
  public int hashCode() { return Objects.hash(value); }

  @Override
  public String toString() { return value.toString(); }
}
