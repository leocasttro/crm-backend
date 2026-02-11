package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public class PedidoId {
  private final UUID value;

  private PedidoId(UUID value) {
    if (value == null) {
      throw new IllegalArgumentException("ID do pedido não pode ser nulo");
    }
    this.value = value;
  }

  public static PedidoId fromString(String id) {
    try {
      return new PedidoId(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("ID do pedido inválido: " + id, e);
    }
  }

  public static PedidoId generate() {
    return new PedidoId(UUID.randomUUID());
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
    PedidoId pedidoId = (PedidoId) o;
    return Objects.equals(value, pedidoId.value);
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