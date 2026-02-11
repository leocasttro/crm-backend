package org.br.ltec.crmbackend.crm.paciente.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeletePacienteUseCase {

  private final PacienteRepository pacienteRepository;

  public DeletePacienteUseCase(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  public void execute(String id) {
    PacienteId pacienteId = PacienteId.fromString(id);

    // Verificar se paciente existe
    boolean existe = pacienteRepository.buscarPorId(pacienteId).isPresent();
    if (!existe) {
      throw new DomainException("Paciente não encontrado com ID: " + id);
    }

    // Excluir paciente
    pacienteRepository.excluir(pacienteId);
  }

  public void executeByDocumento(String documento) {
    // Buscar paciente pelo documento
    var pacienteOpt = pacienteRepository.buscarPorDocumento(documento);
    if (pacienteOpt.isEmpty()) {
      throw new DomainException("Paciente não encontrado com documento: " + documento);
    }

    // Excluir paciente
    pacienteRepository.excluir(pacienteOpt.get().getId());
  }
}