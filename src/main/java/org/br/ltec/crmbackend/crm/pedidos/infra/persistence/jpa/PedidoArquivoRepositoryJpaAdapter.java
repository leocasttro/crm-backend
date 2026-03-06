package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.infra.storage.FileSystemPedidoArquivoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Primary
public class PedidoArquivoRepositoryJpaAdapter implements PedidoArquivoRepository {

  private final SpringDataPedidoArquivoJpaRepository jpaRepository;
  private final PedidoArquivoJpaMapper mapper;
  private final FileSystemPedidoArquivoRepository storageRepository;

  public PedidoArquivoRepositoryJpaAdapter(
          SpringDataPedidoArquivoJpaRepository jpaRepository,
          PedidoArquivoJpaMapper mapper,
          FileSystemPedidoArquivoRepository storageRepository) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
    this.storageRepository = storageRepository;
  }

  // ==================== MÉTODO PRINCIPAL CORRIGIDO ====================

  @Override
  @Transactional
  public PedidoArquivoSalvo salvar(UUID pedidoId, ArquivoUpload upload, Long checklistItemId) {
    // 1. Salva no filesystem (agora com checklistItemId)
    PedidoArquivoSalvo arquivoSalvo = storageRepository.salvar(pedidoId, upload, checklistItemId);

    // 2. Converte para entidade JPA
    PedidoArquivoJpaEntity entity = mapper.toEntity(arquivoSalvo);

    // 3. Garante que o checklistItemId está na entidade
    entity.setChecklistItemId(checklistItemId);

    // 4. Salva no banco
    PedidoArquivoJpaEntity saved = jpaRepository.save(entity);

    // 5. Retorna o domínio
    return mapper.toDomain(saved);
  }

  // ==================== MÉTODOS DE CONSULTA ====================

  @Override
  public Optional<PedidoArquivoSalvo> buscarPedidoId(UUID pedidoId) {
    return jpaRepository.findByPedidoId(pedidoId)
            .map(mapper::toDomain);
  }

  @Override
  public boolean existeArquivoParaItem(UUID pedidoId, Long checklistItemId) {
    return jpaRepository.existsByPedidoIdAndChecklistItemId(pedidoId, checklistItemId);
  }

  @Override
  public Optional<PedidoArquivoSalvo> buscarPorPedidoEItem(UUID pedidoId, Long checklistItemId) {
    return jpaRepository.findByPedidoIdAndChecklistItemId(pedidoId, checklistItemId)
            .map(mapper::toDomain);
  }

  @Override
  public List<PedidoArquivoSalvo> listarPorPedido(UUID pedidoId) {
    return jpaRepository.findAllByPedidoId(pedidoId)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public void removerPorPedidoEItem(UUID pedidoId, Long checklistItemId) {
    // 1. Busca o arquivo para obter o caminho
    Optional<PedidoArquivoJpaEntity> entity = jpaRepository
            .findByPedidoIdAndChecklistItemId(pedidoId, checklistItemId);

    // 2. Remove do filesystem se existir
    entity.ifPresent(e -> {
      try {
        // Você precisará implementar um método no storageRepository
        // para remover por ID ou caminho
        storageRepository.removerPorCaminho(e.getCaminho());
      } catch (Exception ex) {
        throw new RuntimeException("Erro ao remover arquivo do filesystem", ex);
      }
    });

    // 3. Remove do banco
    jpaRepository.deleteByPedidoIdAndChecklistItemId(pedidoId, checklistItemId);
  }

  // ==================== MÉTODO ADICIONAL ====================

  @Transactional
  public PedidoArquivoSalvo atualizarChecklistItemId(UUID arquivoId, Long checklistItemId) {
    return jpaRepository.findById(arquivoId)
            .map(entity -> {
              entity.setChecklistItemId(checklistItemId);
              return mapper.toDomain(jpaRepository.save(entity));
            })
            .orElseThrow(() -> new RuntimeException("Arquivo não encontrado: " + arquivoId));
  }
}