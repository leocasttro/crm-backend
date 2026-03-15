package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Procedimento;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreatePedidoCommand {
  private CreatePacienteCommand paciente;
  private String pacienteId;
  private String usuarioCriacao;

  private String medicoSolicitanteNome;
  private String medicoSolicitanteCrm;
  private String medicoSolicitanteEspecialidade;

  private String medicoExecutorNome;
  private String medicoExecutorCrm;
  private String medicoExecutorEspecialidade;

  private String procedimentoCodigoTUSS;
  private String procedimentoDescricao;
  private String procedimentoCategoria;
  private List<Procedimento> procedimentos = new ArrayList<>();

  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;

  private String convenioNome;
  private String convenioNumeroCarteira;
  private LocalDate convenioValidadeCarteira;
  private String convenioTipoPlano;

  private String cidCodigo;
  private String cidDescricao;

  private String cidCodigo2;
  private String cidCodigo3;
  private String cidCodigo4;

  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;

  private String telefonePaciente;
  private String enderecoPaciente;
  private String cpfPaciente;
  private String emailPaciente;
  private String sexoPaciente;

  private LocalDateTime agendamentoDataHora;
  private String agendamentoLocal;
  private String hospital;
  private String fornecedor;
  private String riscoCirurgico;
  private Integer agendamentoDuracaoEstimada;
  private String agendamentoObservacoes;

  private StatusPedido.Tipo status;
  private Prioridade prioridade;
  private Lateralidade lateralidade;

  private LocalDate dataPedido;

  private List<String> observacoes;
  private List<String> documentosAnexados;

  public CreatePedidoCommand() {
    this.status = StatusPedido.Tipo.PENDENTE;
    this.dataPedido = LocalDate.now();
  }
}