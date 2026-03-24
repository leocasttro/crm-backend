package org.br.ltec.crmbackend.crm.pedidos.domain.port;

import org.br.ltec.crmbackend.crm.pedidos.domain.model.OpmeItem;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.OpmeItemId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;

import java.util.List;
import java.util.Optional;

public interface OpmeItemRepository {

  OpmeItem save(OpmeItem item);

  List<OpmeItem> saveAll(List<OpmeItem> itens);

  List<OpmeItem> findByPedidoId(PedidoId pedidoId);

  Optional<OpmeItem> findById(OpmeItemId id);

  void deleteByPedidoId(PedidoId pedidoId);
}