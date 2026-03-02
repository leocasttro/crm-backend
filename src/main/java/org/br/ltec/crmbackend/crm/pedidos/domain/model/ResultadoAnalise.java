package org.br.ltec.crmbackend.crm.pedidos.domain.model;

public class ResultadoAnalise {
  private final boolean sucesso;
  private final PedidoCirurgico pedido;
  private final String mensagem;
  private final boolean aprovado;

  private ResultadoAnalise(boolean sucesso, PedidoCirurgico pedido, String mensagem, boolean aprovado) {
    this.sucesso = sucesso;
    this.pedido = pedido;
    this.mensagem = mensagem;
    this.aprovado = aprovado;
  }

  public static ResultadoAnalise aprovado(PedidoCirurgico pedido, String mensagem) {
    return new ResultadoAnalise(true, pedido, mensagem, true);
  }

  public static ResultadoAnalise reprovado(PedidoCirurgico pedido, String mensagem) {
    return new ResultadoAnalise(true, pedido, mensagem, false);
  }

  public static ResultadoAnalise erro(String mensagem) {
    return new ResultadoAnalise(false, null, mensagem, false);
  }

  public boolean isSucesso() { return sucesso; }
  public PedidoCirurgico getPedido() { return pedido; }
  public String getMensagem() { return mensagem; }
  public boolean isAprovado() { return aprovado; }
}