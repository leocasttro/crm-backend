package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreatePedidoRequest {

  private String pacienteId;

  @Valid
  private CreatePacienteCommand paciente;

  // Campos do Médico (3 parâmetros)
  @NotBlank(message = "Nome do médico solicitante é obrigatório")
  @Size(min = 3, max = 100, message = "Nome do médico deve ter entre 3 e 100 caracteres")
  private String medicoSolicitanteNome;

  @NotBlank(message = "CRM é obrigatório")
  @Pattern(regexp = "\\d{4,10}/[A-Z]{2}", message = "CRM deve estar no formato 0000/UF")
  private String medicoSolicitanteCrm;

  private String medicoSolicitanteEspecialidade;

  // Campos do Procedimento (3 parâmetros)
  @NotBlank(message = "Nome do procedimento é obrigatório")
  private String procedimentoNome;

  @NotBlank(message = "Código do procedimento é obrigatório")
  @Pattern(regexp = "\\d{6,10}", message = "Código TUSS deve conter apenas números (6-10 dígitos)")
  private String procedimentoCodigo;

  private String procedimentoCategoria;

  // Campos do Convênio (4 parâmetros)
  @NotBlank(message = "Convênio é obrigatório")
  private String convenioNome;

  @NotBlank(message = "Número da carteira é obrigatório")
  private String convenioNumeroCarteira;

  @NotNull(message = "Validade da carteira é obrigatória")
  private LocalDate convenioValidadeCarteira;

  private String convenioTipoPlano;

  // Campos do CID (2 parâmetros)
  private String cidCodigo;

  private String cidDescricao;

  // Outros campos
  private String lateralidade;              // "D", "E", "B", "NA" ou null

  @NotNull(message = "Prioridade é obrigatória")
  private String prioridade;                // "BAIXA", "MEDIA", "ALTA", "URGENTE" ou usar Prioridade.Tipo

  private String prioridadeJustificativa;   // Para a classe Prioridade

  private String observacaoInicial;

  private LocalDate dataPedido;             // opcional, default = hoje

  // Sugestão de data/hora pretendida (opcional)
  private LocalDateTime dataAgendamentoPretendida;
}