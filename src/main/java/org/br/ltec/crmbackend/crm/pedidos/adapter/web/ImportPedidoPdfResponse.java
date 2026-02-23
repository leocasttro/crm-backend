package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportPedidoPdfResponse {

  private boolean sucesso;
  private String mensagem;
  private String pedidoId;
  private String pacienteId;
  private String nomePaciente;
  private String convenio;
  private String cid;
  private List<ProcedimentoResponse> procedimentos;

  private ImportPedidoPdfResponse() {}

  // Factory method para sucesso (ATUALIZADO)
  public static ImportPedidoPdfResponse sucesso(
          String pedidoId,
          String pacienteId,
          String nomePaciente,
          String convenio,
          String cid,
          List<ProcedimentoResponse> procedimentos) {  // ✅ NOVO PARÂMETRO

    ImportPedidoPdfResponse response = new ImportPedidoPdfResponse();
    response.sucesso = true;
    response.mensagem = "PDF importado e pedido criado com sucesso";
    response.pedidoId = pedidoId;
    response.pacienteId = pacienteId;
    response.nomePaciente = nomePaciente;
    response.convenio = convenio;
    response.cid = cid;
    response.procedimentos = procedimentos;  // ✅ ATRIBUIR
    return response;
  }

  // Factory method para erro
  public static ImportPedidoPdfResponse erro(String mensagem) {
    ImportPedidoPdfResponse response = new ImportPedidoPdfResponse();
    response.sucesso = false;
    response.mensagem = mensagem;
    return response;
  }

  // Getters
  public boolean isSucesso() { return sucesso; }
  public String getMensagem() { return mensagem; }
  public String getPedidoId() { return pedidoId; }
  public String getPacienteId() { return pacienteId; }
  public String getNomePaciente() { return nomePaciente; }
  public String getConvenio() { return convenio; }
  public String getCid() { return cid; }
  public List<ProcedimentoResponse> getProcedimentos() { return procedimentos; }  // ✅ GETTER

  // Classe interna para procedimentos
  public static class ProcedimentoResponse {
    private String codigo;
    private String descricao;
    private String quantidade;

    public ProcedimentoResponse(String codigo, String descricao, String quantidade) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.quantidade = quantidade;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public String getQuantidade() { return quantidade; }
  }
}