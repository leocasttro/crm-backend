package org.br.ltec.crmbackend.crm.paciente.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.command.UpdatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UpdatePacienteUseCase {

  private final PacienteRepository pacienteRepository;

  public UpdatePacienteUseCase(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  public Paciente execute(UpdatePacienteCommand command) {
    // Buscar paciente existente
    PacienteId pacienteId = PacienteId.fromString(command.getPacienteId());
    Paciente paciente = pacienteRepository.buscarPorId(pacienteId)
            .orElseThrow(() -> new DomainException("Paciente não encontrado com ID: " + command.getPacienteId()));

    // Validar email se foi alterado
    if (command.getEmail() != null && !command.getEmail().equals(paciente.getEmail().getEndereco())) {
      if (pacienteRepository.existePorEmail(command.getEmail(), pacienteId)) {
        throw new DomainException("Já existe outro paciente cadastrado com este email: " + command.getEmail());
      }
    }

    // Atualizar nome se fornecido
    if (command.getPrimeiroNome() != null && command.getSobrenome() != null) {
      NomeCompleto novoNome = new NomeCompleto(command.getPrimeiroNome(), command.getSobrenome());
      paciente.atualizarNome(novoNome);
    }

    // Atualizar sexo se fornecido
    if (command.getSexo() != null) {
      Sexo novoSexo = new Sexo(command.getSexo());
      paciente.atualizarSexo(novoSexo);
    }

    // Atualizar endereço se fornecido
    if (command.getLogradouro() != null && !command.getLogradouro().trim().isEmpty()) {
      Endereco novoEndereco = new Endereco(
              command.getLogradouro(),
              command.getNumero(),
              command.getComplemento(),
              command.getBairro(),
              command.getCidade(),
              command.getEstado(),
              command.getCep()
      );
      paciente.atualizarEndereco(novoEndereco);
    }

    // Atualizar telefones se fornecidos
    if (command.getTelefones() != null) {
      // Limpar telefones existentes
      List<Telefone> telefonesExistentes = paciente.getTelefones();
      for (Telefone telefone : telefonesExistentes) {
        paciente.removerTelefone(telefone);
      }

      // Adicionar novos telefones
      List<Telefone> novosTelefones = command.getTelefones().stream()
              .map(t -> new Telefone(t.getNumero(), t.getTipo(), t.isWhatsApp()))
              .collect(Collectors.toList());

      for (Telefone telefone : novosTelefones) {
        paciente.adicionarTelefone(telefone);
      }
    }

    // Salvar paciente atualizado
    return pacienteRepository.salvar(paciente);
  }
}