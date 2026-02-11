package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataPedidoJpaRepository extends JpaRepository<PedidoJpaEntity, UUID> {

  // Buscas básicas
  Optional<PedidoJpaEntity> findById(UUID id);

  List<PedidoJpaEntity> findByPacienteId(UUID pacienteId);

  // Buscas por status
  List<PedidoJpaEntity> findByStatus(String status);

  Page<PedidoJpaEntity> findByStatus(String status, Pageable pageable);

  // Buscas por médico
  List<PedidoJpaEntity> findByMedicoSolicitanteCrm(String crm);

  Page<PedidoJpaEntity> findByMedicoSolicitanteCrm(String crm, Pageable pageable);

  List<PedidoJpaEntity> findByMedicoExecutorCrm(String crm);

  Page<PedidoJpaEntity> findByMedicoExecutorCrm(String crm, Pageable pageable);

  // Buscas por procedimento
  List<PedidoJpaEntity> findByProcedimentoCodigoTuss(String codigoTuss);

  Page<PedidoJpaEntity> findByProcedimentoCodigoTuss(String codigoTuss, Pageable pageable);

  // Buscas por convênio
  List<PedidoJpaEntity> findByConvenioNomeContainingIgnoreCase(String nomeConvenio);

  Page<PedidoJpaEntity> findByConvenioNomeContainingIgnoreCase(String nomeConvenio, Pageable pageable);

  // Buscas por CID
  List<PedidoJpaEntity> findByCidCodigo(String codigoCID);

  Page<PedidoJpaEntity> findByCidCodigo(String codigoCID, Pageable pageable);

  // Buscas por datas
  List<PedidoJpaEntity> findByCriadoEmBetween(LocalDate inicio, LocalDate fim);

  Page<PedidoJpaEntity> findByCriadoEmBetween(LocalDate inicio, LocalDate fim, Pageable pageable);

  List<PedidoJpaEntity> findByDataPedidoBetween(LocalDate inicio, LocalDate fim);

  Page<PedidoJpaEntity> findByDataPedidoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);

  List<PedidoJpaEntity> findByAgendamentoDataHoraBetween(LocalDate inicio, LocalDate fim);

  Page<PedidoJpaEntity> findByAgendamentoDataHoraBetween(LocalDate inicio, LocalDate fim, Pageable pageable);

  // Buscas por prioridade
  List<PedidoJpaEntity> findByPrioridade(String prioridade);

  Page<PedidoJpaEntity> findByPrioridade(String prioridade, Pageable pageable);

  // Buscas por agendamento
  List<PedidoJpaEntity> findByTemAgendamento(Boolean temAgendamento);

  Page<PedidoJpaEntity> findByTemAgendamento(Boolean temAgendamento, Pageable pageable);

  // Buscas por lateralidade
  List<PedidoJpaEntity> findByLateralidade(String lateralidade);

  Page<PedidoJpaEntity> findByLateralidade(String lateralidade, Pageable pageable);

  // Buscas por usuário
  List<PedidoJpaEntity> findByUsuarioCriacao(String usuario);

  Page<PedidoJpaEntity> findByUsuarioCriacao(String usuario, Pageable pageable);

  // Buscas para confirmação de jejum (agendados ou confirmados)
  @Query("SELECT p FROM PedidoJpaEntity p WHERE p.status IN ('AGENDADO', 'CONFIRMADO')")
  List<PedidoJpaEntity> findParaConfirmacaoJejum();

  @Query("SELECT p FROM PedidoJpaEntity p WHERE p.status IN ('AGENDADO', 'CONFIRMADO')")
  Page<PedidoJpaEntity> findParaConfirmacaoJejum(Pageable pageable);

  // Buscas em andamento (não finalizados)
  @Query("SELECT p FROM PedidoJpaEntity p WHERE p.ativo = true")
  List<PedidoJpaEntity> findEmAndamento();

  @Query("SELECT p FROM PedidoJpaEntity p WHERE p.ativo = true")
  Page<PedidoJpaEntity> findEmAndamento(Pageable pageable);

  // Buscas por descrição do procedimento
  @Query("SELECT p FROM PedidoJpaEntity p WHERE LOWER(p.procedimentoDescricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
  List<PedidoJpaEntity> findByProcedimentoDescricaoContaining(@Param("descricao") String descricao);

  @Query("SELECT p FROM PedidoJpaEntity p WHERE LOWER(p.procedimentoDescricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
  Page<PedidoJpaEntity> findByProcedimentoDescricaoContaining(@Param("descricao") String descricao, Pageable pageable);

  // Buscas por nome do médico solicitante
  @Query("SELECT p FROM PedidoJpaEntity p WHERE LOWER(p.medicoSolicitanteNome) LIKE LOWER(CONCAT('%', :nome, '%'))")
  List<PedidoJpaEntity> findByMedicoSolicitanteNomeContaining(@Param("nome") String nome);

  // Contagens
  long countByStatus(String status);

  long countByPacienteId(UUID pacienteId);

  @Query("SELECT COUNT(p) FROM PedidoJpaEntity p WHERE p.pacienteId = :pacienteId AND p.ativo = true")
  long countPedidosAtivosPorPacienteId(@Param("pacienteId") UUID pacienteId);

  long countByMedicoSolicitanteCrm(String crm);

  long countByProcedimentoCodigoTuss(String codigoTuss);

  // Verificações de existência
  boolean existsByPacienteIdAndAtivoTrue(UUID pacienteId);

  // Buscas complexas com Specification
  @Query("SELECT p FROM PedidoJpaEntity p WHERE " +
          "(:pacienteId IS NULL OR p.pacienteId = :pacienteId) AND " +
          "(:status IS NULL OR p.status = :status) AND " +
          "(:prioridade IS NULL OR p.prioridade = :prioridade) AND " +
          "(:lateralidade IS NULL OR p.lateralidade = :lateralidade) AND " +
          "(:temAgendamento IS NULL OR p.temAgendamento = :temAgendamento) AND " +
          "(:temCid IS NULL OR p.temCid = :temCid) AND " +
          "(:dataPedidoInicio IS NULL OR p.dataPedido >= :dataPedidoInicio) AND " +
          "(:dataPedidoFim IS NULL OR p.dataPedido <= :dataPedidoFim)")
  Page<PedidoJpaEntity> findByCriterios(
          @Param("pacienteId") UUID pacienteId,
          @Param("status") String status,
          @Param("prioridade") String prioridade,
          @Param("lateralidade") String lateralidade,
          @Param("temAgendamento") Boolean temAgendamento,
          @Param("temCid") Boolean temCid,
          @Param("dataPedidoInicio") LocalDate dataPedidoInicio,
          @Param("dataPedidoFim") LocalDate dataPedidoFim,
          Pageable pageable);
}