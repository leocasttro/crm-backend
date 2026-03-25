package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOpmeItemJpaRepository extends JpaRepository<OpmeItemJpaEntity, UUID> {

  List<OpmeItemJpaEntity> findByPedidoId(UUID pedidoId);

  void deleteByPedidoId(UUID pedidoId);
}