package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UpdateResponse {
  private final boolean sucesso;
  private final String mensagem;
  private final List<String> alteracoes;
  private final PedidoResponse pedido;
  private final List<String> erros;

  public static UpdateResponse sucesso(String mensagem, List<String> alteracoes, PedidoResponse pedido) {
    return UpdateResponse.builder()
            .sucesso(true)
            .mensagem(mensagem)
            .alteracoes(alteracoes)
            .pedido(pedido)
            .build();
  }

  public static UpdateResponse erro(String mensagem, List<String> erros) {
    return UpdateResponse.builder()
            .sucesso(false)
            .mensagem(mensagem)
            .erros(erros)
            .build();
  }

  public static UpdateResponse erro(String mensagem, String erro) {
    return UpdateResponse.builder()
            .sucesso(false)
            .mensagem(mensagem)
            .erros(List.of(erro))
            .build();
  }
}