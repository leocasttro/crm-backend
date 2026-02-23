package org.br.ltec.crmbackend.crm.pedidos.domain.port;

import org.br.ltec.crmbackend.crm.pedidos.application.useCase.PedidoExtraido;

public interface PedidoPdfExtractor {
  PedidoExtraido extract(byte[] pdfBytes);
}
