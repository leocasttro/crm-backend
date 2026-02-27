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
  @Column(name = "id", nullable = false, updatable = false)
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
  @Column(name = "paciente_id", nullable = false)
  private UUID pacienteId;

  // Médico Solicitante
  @Column(name = "medico_solicitante_nome", nullable = false, length = 100)
  private String medicoSolicitanteNome;

  @Column(name = "medico_solicitante_crm", nullable = false, length = 20)
  private String medicoSolicitanteCrm;

  @Column(name = "medico_solicitante_especialidade", length = 100)
  private String medicoSolicitanteEspecialidade;

  // Médico Executor (opcional)
  @Column(name = "medico_executor_nome", length = 100)
  private String medicoExecutorNome;

  @Column(name = "medico_executor_crm", length = 20)
  private String medicoExecutorCrm;

  @Column(name = "medico_executor_especialidade", length = 100)
  private String medicoExecutorEspecialidade;

  // Procedimento
  @Column(name = "procedimento_codigo_tuss", nullable = false, length = 20)
  private String procedimentoCodigoTuss;

  @Column(name = "procedimento_descricao", nullable = false, length = 500)
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
  @Column(name = "telefone_paciente", length = 20)
  private String telefonePaciente;

  // 🔥 NOVO CAMPO: Endereço do paciente
  @Column(name = "endereco_paciente", length = 255)
  private String enderecoPaciente;

  // Convênio
  @Column(name = "convenio_nome", nullable = false, length = 100)
  private String convenioNome;

  @Column(name = "convenio_numero_carteira", nullable = false, length = 50)
  private String convenioNumeroCarteira;

  @Column(name = "convenio_validade_carteira", nullable = false)
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

  @Column(name = "agendamento_sala", length = 50)
  private String agendamentoSala;

  @Column(name = "agendamento_duracao_estimada")
  private Integer agendamentoDuracaoEstimada;

  @Column(name = "agendamento_observacoes", columnDefinition = "TEXT")
  private String agendamentoObservacoes;

  // Status
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "status_observacao", columnDefinition = "TEXT")
  private String statusObservacao;

  @Column(name = "status_usuario_alteracao", length = 100)
  private String statusUsuarioAlteracao;

  // Prioridade
  @Column(name = "prioridade", nullable = false, length = 20)
  private String prioridade;

  @Column(name = "prioridade_justificativa", columnDefinition = "TEXT")
  private String prioridadeJustificativa;

  // Lateralidade
  @Column(name = "lateralidade", nullable = false, length = 20)
  private String lateralidade;

  // Observações (armazenadas como JSON array)
  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  // Documentos anexados (URLs ou caminhos, armazenados como JSON array)
  @Column(name = "documentos_anexados", columnDefinition = "TEXT")
  private String documentosAnexados;

  // Metadados
  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  @Column(name = "usuario_criacao", nullable = false, length = 100)
  private String usuarioCriacao;

  @Column(name = "usuario_atualizacao", length = 100)
  private String usuarioAtualizacao;

  @Column(name = "data_pedido", nullable = false)
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