package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PedidoResponse {
  private String id;
  private String pacienteId;

  // Status e prioridade
  private String status;
  private String prioridade;
  private String prioridadeJustificativa;

  // Datas
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private LocalDate dataPedido;
  private LocalDate dataSolicitacao;
  private LocalDateTime agendadoPara;

  // Procedimento
  private String procedimento;
  private String procedimentoDescricao;
  private String procedimentoCodigo;
  private String procedimentoCategoria;

  // Dados clínicos
  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;

  // Lateralidade
  private String lateralidade;

  // Convênio
  private String convenio;
  private String convenioNome;
  private String convenioNumeroCarteira;
  private LocalDate convenioValidadeCarteira;
  private String convenioTipoPlano;

  // Número carteira (alias)
  private String numeroCarteira;
  private LocalDate validadeCarteira;

  // CID
  private String cid;
  private String cidCodigo;
  private String cidDescricao;

  // CIDs secundários
  private String cidCodigo2;
  private String cidCodigo3;
  private String cidCodigo4;

  // Médico solicitante
  private String medicoSolicitante;
  private String medicoSolicitanteNome;
  private String medicoSolicitanteCrm;
  private String medicoSolicitanteEspecialidade;
  private String cbo;

  // Médico executor
  private String medicoExecutorNome;
  private String medicoExecutorCrm;
  private String medicoExecutorEspecialidade;

  // Dados do paciente (campos diretos)
  private String nomePaciente;
  private LocalDate dataNascimento;
  private String cpfPaciente;
  private String emailPaciente;
  private String telefonePaciente;
  private String sexoPaciente;

  // Dados da guia
  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;

  // Dados da internação
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;

  // Documentos e observações
  private boolean temDocumentos;
  private List<String> documentosAnexados;
  private List<String> observacoes;
  private int quantidadeObservacoes;
}