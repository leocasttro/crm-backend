package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AnaliseResponse {
  private final boolean sucesso;
  private final boolean aprovado;
  private final String mensagem;
  private final PedidoResponse pedido;
  private final List<String> erros;

  public static AnaliseResponse sucesso(boolean aprovado, String mensagem, PedidoResponse pedido) {
    return AnaliseResponse.builder()
            .sucesso(true)
            .aprovado(aprovado)
            .mensagem(mensagem)
            .pedido(pedido)
            .build();
  }

  public static AnaliseResponse erro(String mensagem, List<String> erros) {
    return AnaliseResponse.builder()
            .sucesso(false)
            .aprovado(false)
            .mensagem(mensagem)
            .erros(erros)
            .build();
  }

  public static AnaliseResponse erro(String mensagem, String erro) {
    return AnaliseResponse.builder()
            .sucesso(false)
            .aprovado(false)
            .mensagem(mensagem)
            .erros(List.of(erro))
            .build();
  }
}