package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.pedidos.domain.model.OpmeItem;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.OpmeItemId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpmeItemJpaMapper {

  public OpmeItemJpaEntity toEntity(OpmeItem item) {
    if (item == null) {
      return null;
    }

    OpmeItemJpaEntity entity = new OpmeItemJpaEntity();

    if (item.getId() != null) {
      entity.setId(item.getId().getValue());
    }

    entity.setPedidoId(item.getPedidoId().getValue());
    entity.setDescricao(item.getDescricao());
    entity.setQuantidade(item.getQuantidade() != null ? item.getQuantidade() : 1);
    entity.setMarcasNegadas(item.getMarcasNegadas());
    entity.setObservacao(item.getObservacao());

    // List<String> → "MEDTRONIC,XOMED,RICHARDS"
    if (item.getMarcasAceitas() != null && !item.getMarcasAceitas().isEmpty()) {
      entity.setMarcasAceitas(String.join(",", item.getMarcasAceitas()));
    }

    return entity;
  }

  public OpmeItem toDomain(OpmeItemJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    // "MEDTRONIC,XOMED,RICHARDS" → List<String>
    List<String> marcasAceitas = Collections.emptyList();
    if (entity.getMarcasAceitas() != null && !entity.getMarcasAceitas().isBlank()) {
      marcasAceitas = Arrays.stream(entity.getMarcasAceitas().split(","))
              .map(String::trim)
              .filter(s -> !s.isBlank())
              .collect(Collectors.toList());
    }

    return OpmeItem.builder()
            .id(OpmeItemId.fromString(entity.getId().toString()))
            .pedidoId(PedidoId.fromString(entity.getPedidoId().toString()))
            .descricao(entity.getDescricao())
            .quantidade(entity.getQuantidade())
            .marcasAceitas(marcasAceitas)
            .marcasNegadas(entity.getMarcasNegadas())
            .observacao(entity.getObservacao())
            .build();
  }
}