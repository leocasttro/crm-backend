package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PedidoArquivoRepositoryJpaAdapter {

  private final SpringDataPedidoArquivoJpaRepository repo;
  private final PedidoArquivoJpaMapper mapper;

  public PedidoArquivoRepositoryJpaAdapter(
          SpringDataPedidoArquivoJpaRepository repo,
          PedidoArquivoJpaMapper mapper
  ) {
    this.repo = repo;
    this.mapper = mapper;
  }

  /**
   * Salva/atualiza os metadados do PDF do pedido.
   */
  @Transactional
  public void salvarMetadados(PedidoArquivoRepository.PedidoArquivoSalvo salvo) {
    PedidoArquivoJpaEntity entity = mapper.toEntity(salvo);

    // Se você colocou unique em pedido_id, é comum querer "upsert"
    Optional<PedidoArquivoJpaEntity> existente = repo.findByPedidoId(salvo.pedidoId());
    existente.ifPresent(e -> entity.setId(e.getId())); // reaproveita o mesmo ID

    repo.save(entity);
  }

  @Transactional(readOnly = true)
  public Optional<PedidoArquivoRepository.PedidoArquivoSalvo> buscarPorPedidoId(UUID pedidoId) {
    return repo.findByPedidoId(pedidoId).map(mapper::toDomain);
  }
}
