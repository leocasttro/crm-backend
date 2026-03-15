package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Procedimento;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "pedidos_cirurgicos",
        indexes = {
                @Index(name = "idx_pedido_paciente", columnList = "paciente_id"),
                @Index(name = "idx_pedido_status", columnList = "status"),
                @Index(name = "idx_pedido_medico_solicitante", columnList = "medico_solicitante_crm"),
                @Index(name = "idx_pedido_procedimento", columnList = "procedimento_codigo_tuss"),
                @Index(name = "idx_pedido_convenio", columnList = "convenio_nome"),
                @Index(name = "idx_pedido_data_agendamento", columnList = "agendamento_data_hora"),
                @Index(name = "idx_pedido_data_criacao", columnList = "criado_em"),
                @Index(name = "idx_pedido_prioridade", columnList = "prioridade")
        }
)
@Getter
@Setter
public class PedidoJpaEntity implements Persistable<UUID> {

  @Id
  @Column(name = "id", updatable = false)
  private UUID id;

  @Transient
  private boolean isNew = true;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return isNew || id == null;
  }

  @PostLoad
  protected void markNotNew() {
    this.isNew = false;
  }

  @PrePersist
  protected void prePersist() {
    this.isNew = true;
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
    this.criadoEm = LocalDateTime.now();
    this.dataPedido = LocalDate.now();
    calcularCamposDerivados();
  }

  @PreUpdate
  protected void preUpdate() {
    this.isNew = false;
    this.atualizadoEm = LocalDateTime.now();
    calcularCamposDerivados();
  }

  // Referência ao paciente
  @Column(name = "paciente_id")
  private UUID pacienteId;

  // Médico Solicitante
  @Column(name = "medico_solicitante_nome", length = 100)
  private String medicoSolicitanteNome;

  @Column(name = "medico_solicitante_crm", length = 50)
  private String medicoSolicitanteCrm;

  @Column(name = "medico_solicitante_especialidade", length = 100)
  private String medicoSolicitanteEspecialidade;

  // Médico Executor (opcional)
  @Column(name = "medico_executor_nome", length = 100)
  private String medicoExecutorNome;

  @Column(name = "medico_executor_crm", length = 50)
  private String medicoExecutorCrm;

  @Column(name = "medico_executor_especialidade", length = 100)
  private String medicoExecutorEspecialidade;

  // Procedimento
  @Column(name = "procedimento_codigo_tuss", length = 50)
  private String procedimentoCodigoTuss;

  @Column(name = "procedimento_descricao", length = 500)
  private String procedimentoDescricao;

  @Column(name = "procedimento_categoria", length = 100)
  private String procedimentoCategoria;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<Procedimento> procedimentos = new ArrayList<>();

  // 🔥 NOVO CAMPO: Indicação Clínica
  @Column(name = "indicacao_clinica", columnDefinition = "TEXT")
  private String indicacaoClinica;

  // 🔥 NOVO CAMPO: Relatório Pré-Operatório
  @Column(name = "relatorio_pre_operatorio", columnDefinition = "TEXT")
  private String relatorioPreOperatorio;

  // 🔥 NOVO CAMPO: Orientações
  @Column(name = "orientacoes", columnDefinition = "TEXT")
  private String orientacoes;

  // 🔥 NOVO CAMPO: Telefone do paciente
  @Column(name = "telefone_paciente", length = 50)
  private String telefonePaciente;

  // 🔥 NOVO CAMPO: Endereço do paciente
  @Column(name = "endereco_paciente", length = 255)
  private String enderecoPaciente;

  // Convênio
  @Column(name = "convenio_nome", length = 100)
  private String convenioNome;

  @Column(name = "convenio_numero_carteira", length = 50)
  private String convenioNumeroCarteira;

  @Column(name = "convenio_validade_carteira")
  private LocalDate convenioValidadeCarteira;

  @Column(name = "convenio_tipo_plano", length = 50)
  private String convenioTipoPlano;

  // CID (opcional)
  @Column(name = "cid_codigo", length = 10)
  private String cidCodigo;

  @Column(name = "cid_descricao", length = 500)
  private String cidDescricao;

  // CIDs secundários
  @Column(name = "cid_codigo_2", length = 10)
  private String cidCodigo2;

  @Column(name = "cid_codigo_3", length = 10)
  private String cidCodigo3;

  @Column(name = "cid_codigo_4", length = 10)
  private String cidCodigo4;

  // Agendamento (opcional)
  @Column(name = "agendamento_data_hora")
  private LocalDateTime agendamentoDataHora;

  @Column(name = "agendamento_local", length = 50)
  private String agendamentoLocal;

  @Column(name = "agendamento_hospital", length = 50)
  private String hospital;

  @Column(name = "agendamento_fornecedor")
  private String fornecedor;

  @Column(name = "agendamento_risco_cirurgico")
  private String riscoCirurgico;

  @Column(name = "agendamento_duracao_estimada")
  private Integer agendamentoDuracaoEstimada;

  // Status
  @Column(name = "status", length = 50)
  private String status;

  @Column(name = "status_observacao", columnDefinition = "TEXT")
  private String statusObservacao;

  @Column(name = "status_usuario_alteracao", length = 100)
  private String statusUsuarioAlteracao;

  // Prioridade
  @Column(name = "prioridade", length = 50)
  private String prioridade;

  @Column(name = "prioridade_justificativa", columnDefinition = "TEXT")
  private String prioridadeJustificativa;

  // Lateralidade
  @Column(name = "lateralidade", length = 50)
  private String lateralidade;

  // Observações (armazenadas como JSON array)
  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  // Documentos anexados (URLs ou caminhos, armazenados como JSON array)
  @Column(name = "documentos_anexados", columnDefinition = "TEXT")
  private String documentosAnexados;

  // Metadados
  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  @Column(name = "usuario_criacao", length = 100)
  private String usuarioCriacao;

  @Column(name = "usuario_atualizacao", length = 100)
  private String usuarioAtualizacao;

  @Column(name = "data_pedido")
  private LocalDate dataPedido;

  // Dados da guia/internação
  @Column(name = "numero_guia", length = 50)
  private String numeroGuia;

  @Column(name = "registro_ans", length = 50)
  private String registroAns;

  @Column(name = "numero_guia_operadora", length = 50)
  private String numeroGuiaOperadora;

  @Column(name = "codigo_operadora", length = 50)
  private String codigoOperadora;

  @Column(name = "nome_contratado", length = 255)
  private String nomeContratado;

  @Column(name = "carater_atendimento", length = 50)
  private String caraterAtendimento;

  @Column(name = "tipo_internacao", length = 10)
  private String tipoInternacao;

  @Column(name = "regime_internacao", length = 10)
  private String regimeInternacao;

  @Column(name = "qtd_diarias_solicitadas", length = 10)
  private String qtdDiariasSolicitadas;

  // Campos calculados para otimização de consultas
  @Column(name = "tem_agendamento")
  private Boolean temAgendamento;

  @Column(name = "tem_cid")
  private Boolean temCid;

  @Column(name = "tem_documentos")
  private Boolean temDocumentos;

  @Column(name = "ativo")
  private Boolean ativo;

  @Column(name = "finalizado")
  private Boolean finalizado;

  @Column(name = "consulta_pre_data_hora")
  private LocalDateTime consultaPreDataHora;

  @Column(name = "consulta_pre_cuidados", columnDefinition = "TEXT")
  private String consultaPreCuidados;

  @Column(name = "consulta_pre_observacoes_especiais", columnDefinition = "TEXT")
  private String consultaPreObservacoesEspeciais;

  @Column(name = "consulta_pre_local", columnDefinition = "TEXT")
  private String consultaPreLocal;

  @Column(name = "status_autorizacao", columnDefinition = "TEXT")
  private String statusAutorizacao;

  @Column(name = "numero_guia_autorizacao", columnDefinition = "TEXT")
  private String numeroGuiaAutorizacao;

  @Column(name = "senha_autorizacao", columnDefinition = "TEXT")
  private String senhaAutorizacao;

  @Column(name = "validade_autorizacao")
  private LocalDate validadeAutorizacao;

  @Column(name = "tipo_acomodacao", columnDefinition = "TEXT")
  private String tipoAcomodacao;

  private void calcularCamposDerivados() {
    this.temAgendamento = agendamentoDataHora != null;
    this.temCid = cidCodigo != null && !cidCodigo.trim().isEmpty();
    this.temDocumentos = documentosAnexados != null && !documentosAnexados.trim().isEmpty();

    if (status != null) {
      this.ativo = !status.equals("REALIZADO") && !status.equals("CANCELADO") && !status.equals("REJEITADO");
      this.finalizado = status.equals("REALIZADO") || status.equals("CANCELADO") || status.equals("REJEITADO");
    } else {
      this.ativo = true;
      this.finalizado = false;
    }
  }
}