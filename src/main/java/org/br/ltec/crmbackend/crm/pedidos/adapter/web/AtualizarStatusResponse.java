package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AtualizarStatusResponse {
  private boolean sucesso;
  private String mensagem;
  private List<String> erros;

  // Dados do status
  private String pedidoId;
  private String statusAnterior;
  private String statusAtual;
  private String dataAtualizacao;
  private String usuario;
  private String observacao;

  // Dados resumidos do pedido (opcional)
  private String pacienteNome;
  private String procedimento;

  // Métodos factory
  public static AtualizarStatusResponse sucesso(
          String mensagem,
          String pedidoId,
          String statusAnterior,
          String statusAtual,
          String pacienteNome,
          String procedimento,
          String usuario,
          String observacao) {

    return AtualizarStatusResponse.builder()
            .sucesso(true)
            .mensagem(mensagem)
            .pedidoId(pedidoId)
            .statusAnterior(statusAnterior)
            .statusAtual(statusAtual)
            .dataAtualizacao(java.time.LocalDateTime.now().toString())
            .usuario(usuario)
            .observacao(observacao)
            .pacienteNome(pacienteNome)
            .procedimento(procedimento)
            .build();
  }

  public static AtualizarStatusResponse erro(String mensagem, List<String> erros) {
    return AtualizarStatusResponse.builder()
            .sucesso(false)
            .mensagem(mensagem)
            .erros(erros)
            .build();
  }
}
