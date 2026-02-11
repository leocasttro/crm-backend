package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PedidoRepositoryJpaAdapter implements PedidoRepository {

  private final SpringDataPedidoJpaRepository springDataPedidoJpaRepository;
  private final PedidoJpaMapper pedidoJpaMapper;

  public PedidoRepositoryJpaAdapter(SpringDataPedidoJpaRepository springDataPedidoJpaRepository,
                                    PedidoJpaMapper pedidoJpaMapper) {
    this.springDataPedidoJpaRepository = springDataPedidoJpaRepository;
    this.pedidoJpaMapper = pedidoJpaMapper;
  }

  @Override
  public PedidoCirurgico salvar(PedidoCirurgico pedido) {
    PedidoJpaEntity entity = pedidoJpaMapper.toEntity(pedido);
    PedidoJpaEntity savedEntity = springDataPedidoJpaRepository.save(entity);
    return pedidoJpaMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<PedidoCirurgico> buscarPorId(PedidoId id) {
    return springDataPedidoJpaRepository.findById(id.getValue())
            .map(pedidoJpaMapper::toDomain);
  }

  @Override
  public List<PedidoCirurgico> buscarPorPacienteId(PacienteId pacienteId) {
    return springDataPedidoJpaRepository.findByPacienteId(pacienteId.getValue())
            .stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorStatus(StatusPedido.Tipo status, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByStatus(status.name(), pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorMedicoSolicitante(String crm, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByMedicoSolicitanteCrm(crm, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorMedicoExecutor(String crm, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByMedicoExecutorCrm(crm, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorProcedimento(String codigoTUSS, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByProcedimentoCodigoTuss(codigoTUSS, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorConvenio(String nomeConvenio, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByConvenioNomeContainingIgnoreCase(nomeConvenio, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorCID(String codigoCID, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByCidCodigo(codigoCID, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorDataCriacao(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByCriadoEmBetween(dataInicio, dataFim, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorDataPedido(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByDataPedidoBetween(dataInicio, dataFim, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorDataAgendamento(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByAgendamentoDataHoraBetween(dataInicio, dataFim, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorPrioridade(String prioridade, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByPrioridade(prioridade, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarComAgendamento(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByTemAgendamento(true, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarSemAgendamento(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByTemAgendamento(false, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorLateralidade(String lateralidade, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByLateralidade(lateralidade, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarPorUsuarioCriacao(String usuario, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findByUsuarioCriacao(usuario, pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarParaConfirmacaoJejum(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findParaConfirmacaoJejum(pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarEmAndamento(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findEmAndamento(pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<PedidoCirurgico> buscarTodos(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PedidoJpaEntity> page = springDataPedidoJpaRepository.findAll(pageable);
    return page.getContent().stream()
            .map(pedidoJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public void excluir(PedidoId id) {
    springDataPedidoJpaRepository.deleteById(id.getValue());
  }

  @Override
  public boolean existePorId(PedidoId id) {
    return springDataPedidoJpaRepository.existsById(id.getValue());
  }

  @Override
  public boolean existePedidoAtivoParaPaciente(PacienteId pacienteId) {
    return springDataPedidoJpaRepository.existsByPacienteIdAndAtivoTrue(pacienteId.getValue());
  }

  @Override
  public long contarTotal() {
    return springDataPedidoJpaRepository.count();
  }

  @Override
  public long contarPorStatus(StatusPedido.Tipo status) {
    return springDataPedidoJpaRepository.countByStatus(status.name());
  }

  @Override
  public long contarPorPacienteId(PacienteId pacienteId) {
    return springDataPedidoJpaRepository.countByPacienteId(pacienteId.getValue());
  }

}