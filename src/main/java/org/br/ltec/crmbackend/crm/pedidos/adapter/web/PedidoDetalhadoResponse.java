package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PedidoDetalhadoResponse {

  private String id;
  private String pacienteId;
  private String pacienteNome;
  private String medicoSolicitante;
  private String medicoSolicitanteCrm;
  private String medicoExecutor;           // pode ser null
  private String procedimento;
  private String procedimentoCodigo;
  private String convenio;
  private String convenioNumeroCarteira;
  private String cid;
  private String lateralidade;
  private String prioridade;
  private String status;
  private String statusMotivo;             // última observação do status
  private LocalDate dataPedido;
  private LocalDateTime criadoEm;
  private String usuarioCriacao;
  private LocalDateTime atualizadoEm;
  private String usuarioAtualizacao;
  private LocalDateTime agendadoPara;
  private List<String> observacoes;
  private List<String> documentosAnexados;
}