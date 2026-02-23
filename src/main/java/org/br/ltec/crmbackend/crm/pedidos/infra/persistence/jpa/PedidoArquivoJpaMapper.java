package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import java.util.UUID;

import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.springframework.stereotype.Component;

@Component
public class PedidoArquivoJpaMapper {

  public PedidoArquivoJpaEntity toEntity(PedidoArquivoRepository.PedidoArquivoSalvo salvo) {
    if (salvo == null) return null;

    PedidoArquivoJpaEntity e = new PedidoArquivoJpaEntity();
    e.setId(salvo.id());
    e.setPedidoId(salvo.pedidoId());
    e.setNomeOriginal(salvo.nomeOriginal());
    e.setContentType(salvo.contentType());
    e.setTamanhoBytes(salvo.tamanhoBytes());
    e.setSha256(salvo.sha256());
    e.setCaminho(salvo.caminho());
    return e;
  }

  public PedidoArquivoRepository.PedidoArquivoSalvo toDomain(PedidoArquivoJpaEntity e) {
    if (e == null) return null;

    UUID id = e.getId();
    UUID pedidoId = e.getPedidoId();

    return new PedidoArquivoRepository.PedidoArquivoSalvo(
            id,
            pedidoId,
            e.getNomeOriginal(),
            e.getContentType(),
            e.getTamanhoBytes(),
            e.getSha256(),
            e.getCaminho()
    );
  }
}
