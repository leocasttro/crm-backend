package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPedidoArquivoJpaRepository extends JpaRepository<PedidoArquivoJpaEntity, UUID> {
  Optional<PedidoArquivoJpaEntity> findByPedidoId(UUID pedidoId);
}
