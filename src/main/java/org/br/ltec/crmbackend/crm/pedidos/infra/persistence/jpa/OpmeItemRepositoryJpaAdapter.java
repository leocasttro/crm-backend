package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.pedidos.domain.model.OpmeItem;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.OpmeItemRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.OpmeItemId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OpmeItemRepositoryJpaAdapter implements OpmeItemRepository {

  private final SpringDataOpmeItemJpaRepository springDataOpmeItemJpaRepository;
  private final OpmeItemJpaMapper opmeItemJpaMapper;

  public OpmeItemRepositoryJpaAdapter(SpringDataOpmeItemJpaRepository springDataOpmeItemJpaRepository,
                                      OpmeItemJpaMapper opmeItemJpaMapper) {
    this.springDataOpmeItemJpaRepository = springDataOpmeItemJpaRepository;
    this.opmeItemJpaMapper = opmeItemJpaMapper;
  }

  @Override
  public OpmeItem save(OpmeItem item) {
    OpmeItemJpaEntity entity = opmeItemJpaMapper.toEntity(item);
    OpmeItemJpaEntity saved = springDataOpmeItemJpaRepository.save(entity);
    return opmeItemJpaMapper.toDomain(saved);
  }

  @Override
  public List<OpmeItem> saveAll(List<OpmeItem> itens) {
    List<OpmeItemJpaEntity> entities = itens.stream()
            .map(opmeItemJpaMapper::toEntity)
            .collect(Collectors.toList());

    return springDataOpmeItemJpaRepository.saveAll(entities)
            .stream()
            .map(opmeItemJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<OpmeItem> findByPedidoId(PedidoId pedidoId) {
    return springDataOpmeItemJpaRepository.findByPedidoId(pedidoId.getValue())
            .stream()
            .map(opmeItemJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public Optional<OpmeItem> findById(OpmeItemId id) {
    return springDataOpmeItemJpaRepository.findById(id.getValue())
            .map(opmeItemJpaMapper::toDomain);
  }

  @Override
  public void deleteByPedidoId(PedidoId pedidoId) {
    springDataOpmeItemJpaRepository.deleteByPedidoId(pedidoId.getValue());
  }
}