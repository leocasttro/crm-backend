package org.br.ltec.crmbackend.crm.paciente.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.model.PacienteBuilder;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreatePacienteUseCase {

  private final PacienteRepository pacienteRepository;

  public CreatePacienteUseCase(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  public Paciente execute(CreatePacienteCommand command) {
    // Validar se já existe paciente com mesmo documento
    String documentoNumero = command.getDocumentoNumero();
    if (pacienteRepository.existePorDocumento(documentoNumero, null)) {
      throw new DomainException("Já existe um paciente cadastrado com este documento: " + documentoNumero);
    }

    // Validar se já existe paciente com mesmo email
    if (pacienteRepository.existePorEmail(command.getEmail(), null)) {
      throw new DomainException("Já existe um paciente cadastrado com este email: " + command.getEmail());
    }

    try {
      // Criar Value Objects
      PacienteId id = PacienteId.generate();
      NomeCompleto nome = new NomeCompleto(command.getPrimeiroNome(), command.getSobrenome());
      Documento documento = null;
      if (command.getDocumentoNumero() != null && command.getDocumentoTipo() != null) {
        documento = Documento.criar(command.getDocumentoNumero(), command.getDocumentoTipo());
      }
      Email email = new Email(command.getEmail());
      DataNascimento dataNascimento = new DataNascimento(command.getDataNascimento());
      Sexo sexo = new Sexo(command.getSexo());

      // Criar endereço se fornecido
      Endereco endereco = null;
      if (command.getLogradouro() != null && !command.getLogradouro().trim().isEmpty()) {
        endereco = new Endereco(
                command.getLogradouro(),
                command.getNumero(),
                command.getComplemento(),
                command.getBairro(),
                command.getCidade(),
                command.getEstado(),
                command.getCep()
        );
      }

      // Criar lista de telefones
      List<Telefone> telefones = command.getTelefones() != null ?
              command.getTelefones().stream()
                      .map(t -> new Telefone(t.getNumero(), t.getTipo(), t.isWhatsApp()))
                      .collect(Collectors.toList()) :
              List.of();

      // Criar paciente usando o builder
      Paciente paciente = new PacienteBuilder()
              .comId(id)
              .comNome(nome)
              .comDocumento(documento)
              .comEmail(email)
              .comDataNascimento(dataNascimento)
              .comSexo(sexo)
              .comEndereco(endereco)
              .comTelefones(telefones)
              .build();

      // Salvar paciente
      return pacienteRepository.salvar(paciente);

    } catch (IllegalArgumentException e) {
      throw new DomainException("Dados inválidos fornecidos: " + e.getMessage(), e);
    }
  }
}