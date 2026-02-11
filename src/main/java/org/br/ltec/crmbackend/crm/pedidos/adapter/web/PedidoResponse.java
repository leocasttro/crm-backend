package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class PedidoResponse {

  private String id;
  private String pacienteId;
  private String medicoSolicitante;
  private String medicoSolicitanteEspecialidade; // ← ADICIONE ESTE CAMPO
  private String procedimento;
  private String procedimentoCodigo;
  private String convenio;
  private LocalDate convenioValidadeCarteira;
  private String cid;
  private String cidDescricao;
  private String prioridade;
  private String prioridadeJustificativa;
  private String status;
  private String lateralidade;
  private LocalDate dataPedido;
  private LocalDateTime criadoEm;
  private LocalDateTime agendadoPara;
  private Integer quantidadeObservacoes;
  private Boolean temDocumentos;
}