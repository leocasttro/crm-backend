package org.br.ltec.crmbackend.crm.paciente.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PacienteRepositoryJpaAdapter implements PacienteRepository {

  private final SpringDataPacienteJpaRepository springDataPacienteJpaRepository;
  private final PacienteJpaMapper pacienteJpaMapper;

  public PacienteRepositoryJpaAdapter(
          SpringDataPacienteJpaRepository springDataPacienteJpaRepository,
          PacienteJpaMapper pacienteJpaMapper) {
    this.springDataPacienteJpaRepository = springDataPacienteJpaRepository;
    this.pacienteJpaMapper = pacienteJpaMapper;
  }

  @Override
  public Paciente salvar(Paciente paciente) {
    PacienteJpaEntity entity = pacienteJpaMapper.toEntity(paciente);
    PacienteJpaEntity savedEntity = springDataPacienteJpaRepository.save(entity);
    return pacienteJpaMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Paciente> buscarPorId(PacienteId id) {
    return springDataPacienteJpaRepository.findById(id.getValue())
            .map(pacienteJpaMapper::toDomain);
  }

  @Override
  public Optional<Paciente> buscarPorDocumento(String documento) {
    return springDataPacienteJpaRepository.findByDocumentoNumero(documento)
            .map(pacienteJpaMapper::toDomain);
  }

  @Override
  public Optional<Paciente> buscarPorEmail(String email) {
    return springDataPacienteJpaRepository.findByEmail(email)
            .map(pacienteJpaMapper::toDomain);
  }

  @Override
  public boolean existePorDocumento(String documento, PacienteId excluirId) {
    if (excluirId == null) {
      return springDataPacienteJpaRepository.existsByDocumentoNumero(documento);
    } else {
      return springDataPacienteJpaRepository.existsByDocumentoNumeroAndIdNot(documento, excluirId.getValue());
    }
  }

  @Override
  public boolean existePorEmail(String email, PacienteId excluirId) {
    if (excluirId == null) {
      return springDataPacienteJpaRepository.existsByEmail(email);
    } else {
      return springDataPacienteJpaRepository.existsByEmailAndIdNot(email, excluirId.getValue());
    }
  }

  @Override
  public List<Paciente> listar(int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    Page<PacienteJpaEntity> page = springDataPacienteJpaRepository.findAll(pageable);
    return page.getContent().stream()
            .map(pacienteJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<Paciente> buscarPorNome(String nome, int pagina, int tamanhoPagina) {
    Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
    List<PacienteJpaEntity> entities = springDataPacienteJpaRepository.findByNomeContainingIgnoreCase(nome);

    // Paginação manual para consultas customizadas
    int start = pagina * tamanhoPagina;
    int end = Math.min(start + tamanhoPagina, entities.size());

    if (start > entities.size()) {
      return List.of();
    }

    return entities.subList(start, end).stream()
            .map(pacienteJpaMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public void excluir(PacienteId id) {
    springDataPacienteJpaRepository.deleteById(id.getValue());
  }

  @Override
  public long contar() {
    return springDataPacienteJpaRepository.count();
  }

  @Override
  public long contarPorNome(String nome) {
    return springDataPacienteJpaRepository.countByNomeContainingIgnoreCase(nome);
  }
}