package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPedidoArquivoJpaRepository extends JpaRepository<PedidoArquivoJpaEntity, UUID> {

  Optional<PedidoArquivoJpaEntity> findByPedidoId(UUID pedidoId);
  List<PedidoArquivoJpaEntity> findAllByPedidoId(UUID pedidoId);
  Optional<PedidoArquivoJpaEntity> findByPedidoIdAndChecklistItemId(UUID pedidoId, Long checklistItemId);
  boolean existsByPedidoIdAndChecklistItemId(UUID pedidoId, Long checklistItemId);
  void deleteByPedidoIdAndChecklistItemId(UUID pedidoId, Long checklistItemId);
}