package org.br.ltec.crmbackend.crm.paciente.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.model.PacienteBuilder;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PacienteJpaMapper {

  public PacienteJpaEntity toEntity(Paciente paciente) {
    if (paciente == null) {
      return null;
    }

    PacienteJpaEntity entity = new PacienteJpaEntity();

    // ID
    if (paciente.getId() != null) {
      entity.setId(paciente.getId().getValue());
    }

    // Nome
    entity.setPrimeiroNome(paciente.getNome().getPrimeiroNome());
    entity.setSobrenome(paciente.getNome().getSobrenome());
    entity.setNomeCompleto(paciente.getNome().getNomeCompleto());

    // Documento
    entity.setDocumentoNumero(paciente.getDocumento().getNumero());
    entity.setDocumentoTipo(paciente.getDocumento().getTipo().name());

    // Email
    entity.setEmail(paciente.getEmail().getEndereco());

    // Data Nascimento
    entity.setDataNascimento(paciente.getDataNascimento().getValor());
    entity.setIdade(paciente.getDataNascimento().getIdade());

    // Sexo
    entity.setSexoCodigo(paciente.getSexo().getCodigo());
    entity.setSexoDescricao(paciente.getSexo().getDescricao());

    // Endereço
    paciente.getEndereco().ifPresent(endereco -> {
      entity.setLogradouro(endereco.getLogradouro());
      entity.setNumero(endereco.getNumero());
      entity.setComplemento(endereco.getComplemento());
      entity.setBairro(endereco.getBairro());
      entity.setCidade(endereco.getCidade());
      entity.setEstado(endereco.getEstado());
      entity.setCep(endereco.getCep());
      entity.setPais(endereco.getPais());
    });

    // Telefones (serializar como string formatada)
    if (!paciente.getTelefones().isEmpty()) {
      StringBuilder telefonesStr = new StringBuilder();
      for (Telefone telefone : paciente.getTelefones()) {
        telefonesStr.append(telefone.getNumero())
                .append(";")
                .append(telefone.getTipo().name())
                .append(";")
                .append(telefone.isWhatsApp())
                .append("|");
      }
      // Remove o último separador
      if (telefonesStr.length() > 0) {
        telefonesStr.deleteCharAt(telefonesStr.length() - 1);
      }
      entity.setTelefones(telefonesStr.toString());
    }

    // Campos calculados
    entity.setMaiorDeIdade(paciente.isMaiorDeIdade());
    entity.setIdoso(paciente.isIdoso());
    entity.setPossuiWhatsApp(paciente.possuiWhatsApp());

    return entity;
  }

  public Paciente toDomain(PacienteJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    try {
      // Criar Value Objects
      PacienteId id = PacienteId.fromString(entity.getId().toString());
      NomeCompleto nome = new NomeCompleto(entity.getPrimeiroNome(), entity.getSobrenome());

      Documento documento = Documento.criar(
              entity.getDocumentoNumero(),
              Documento.TipoDocumento.valueOf(entity.getDocumentoTipo())
      );

      Email email = new Email(entity.getEmail());
      DataNascimento dataNascimento = new DataNascimento(entity.getDataNascimento());
      Sexo sexo = new Sexo(entity.getSexoCodigo());

      // Criar endereço
      Endereco endereco = null;
      if (entity.getLogradouro() != null && !entity.getLogradouro().trim().isEmpty()) {
        endereco = new Endereco(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep(),
                entity.getPais()
        );
      }

      // Criar lista de telefones
      List<Telefone> telefones = new ArrayList<>();
      if (entity.getTelefones() != null && !entity.getTelefones().trim().isEmpty()) {
        String[] telefoneEntries = entity.getTelefones().split("\\|");
        for (String entry : telefoneEntries) {
          String[] parts = entry.split(";");
          if (parts.length == 3) {
            String numero = parts[0];
            Telefone.TipoTelefone tipo = Telefone.TipoTelefone.valueOf(parts[1]);
            boolean isWhatsApp = Boolean.parseBoolean(parts[2]);
            telefones.add(new Telefone(numero, tipo, isWhatsApp));
          }
        }
      }

      // Criar paciente
      return new PacienteBuilder()
              .comId(id)
              .comNome(nome)
              .comDocumento(documento)
              .comEmail(email)
              .comDataNascimento(dataNascimento)
              .comSexo(sexo)
              .comEndereco(endereco)
              .comTelefones(telefones)
              .build();

    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter entidade para domínio: " + e.getMessage(), e);
    }
  }
}