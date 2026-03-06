package org.br.ltec.crmbackend.crm.pedidos.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoArquivoRepository {


  PedidoArquivoSalvo salvar(UUID pedidoId, ArquivoUpload upload, Long checklistItemId);

  Optional<PedidoArquivoSalvo> buscarPedidoId(UUID pedidoId);

  boolean existeArquivoParaItem(UUID pedidoId, Long checklistItemId);

  Optional<PedidoArquivoSalvo> buscarPorPedidoEItem(UUID pedidoId, Long checklistItemId);

  List<PedidoArquivoSalvo> listarPorPedido(UUID pedidoId);

  void removerPorPedidoEItem(UUID pedidoId, Long checklistItemId);

  record ArquivoUpload(
          String nomeOriginal,
          String contentType,
          byte[] bytes
  ) {}

  record PedidoArquivoSalvo(
          UUID id,
          UUID pedidoId,
          String nomeOriginal,
          String contentType,
          long tamanhoBytes,
          String sha256,
          String caminho,
          Long checklistItemId
  ) {}
}
