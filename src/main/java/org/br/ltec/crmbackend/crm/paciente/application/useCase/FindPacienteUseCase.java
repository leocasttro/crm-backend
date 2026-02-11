package org.br.ltec.crmbackend.crm.paciente.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindPacienteUseCase {

  private final PacienteRepository pacienteRepository;

  public FindPacienteUseCase(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  public Optional<Paciente> findById(String id) {
    PacienteId pacienteId = PacienteId.fromString(id);
    return pacienteRepository.buscarPorId(pacienteId);
  }

  public Optional<Paciente> findByDocumento(String documento) {
    return pacienteRepository.buscarPorDocumento(documento);
  }

  public Optional<Paciente> findByEmail(String email) {
    return pacienteRepository.buscarPorEmail(email);
  }

  public List<Paciente> findAll(int pagina, int tamanhoPagina) {
    if (pagina < 0) {
      throw new IllegalArgumentException("Página não pode ser negativa");
    }
    if (tamanhoPagina <= 0 || tamanhoPagina > 100) {
      throw new IllegalArgumentException("Tamanho da página deve estar entre 1 e 100");
    }

    return pacienteRepository.listar(pagina, tamanhoPagina);
  }

  public List<Paciente> findByNome(String nome, int pagina, int tamanhoPagina) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome não pode ser vazio");
    }

    return pacienteRepository.buscarPorNome(nome, pagina, tamanhoPagina);
  }

  public long countAll() {
    return pacienteRepository.contar();
  }

  public long countByNome(String nome) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("Nome não pode ser vazio");
    }

    return pacienteRepository.contarPorNome(nome);
  }
}