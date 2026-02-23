package org.br.ltec.crmbackend.crm.pedidos.domain.port;

import java.util.Optional;
import java.util.UUID;

public interface PedidoArquivoRepository {

  PedidoArquivoSalvo salvar(UUID pedidoId, ArquivoUpload upload);

  Optional<PedidoArquivoSalvo> buscarPedidoId(UUID pedidoId);

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
          String caminho
  ) {}
}
