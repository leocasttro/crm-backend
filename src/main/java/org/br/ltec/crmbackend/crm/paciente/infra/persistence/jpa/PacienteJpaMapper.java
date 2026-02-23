package org.br.ltec.crmbackend.crm.paciente.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.paciente.domain.DomainException;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.model.PacienteBuilder;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PacienteJpaMapper {

  public PacienteJpaEntity toEntity(Paciente paciente) {
    if (paciente == null) {
      return null;
    }

    PacienteJpaEntity entity = new PacienteJpaEntity();

    // ID (se vier null, deixa null -> @PrePersist gera UUID)
    if (paciente.getId() != null) {
      entity.setId(paciente.getId().getValue());
    }

    // Nome
    entity.setPrimeiroNome(paciente.getNome().getPrimeiroNome());
    entity.setSobrenome(paciente.getNome().getSobrenome());
    entity.setNomeCompleto(paciente.getNome().getNomeCompleto());

    // Documento
    if (paciente.getDocumento() != null) {
      entity.setDocumentoNumero(paciente.getDocumento().getNumero());
      entity.setDocumentoTipo(paciente.getDocumento().getTipo().name());
    } else {
      entity.setDocumentoNumero(null);
      entity.setDocumentoTipo(null);
    }

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
    if (paciente.getTelefones() != null && !paciente.getTelefones().isEmpty()) {
      StringBuilder telefonesStr = new StringBuilder();

      for (Telefone telefone : paciente.getTelefones()) {
        telefonesStr.append(telefone.getNumero())
                .append(";")
                .append(telefone.getTipo().name())
                .append(";")
                .append(telefone.isWhatsApp())
                .append("|");
      }

      // Remove o último separador "|"
      telefonesStr.deleteCharAt(telefonesStr.length() - 1);
      entity.setTelefones(telefonesStr.toString());
    } else {
      entity.setTelefones(null);
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
      PacienteId id = PacienteId.fromString(entity.getId().toString());

      // ✅ VERIFICAR SE TEM PRIMEIRO NOME ANTES DE CRIAR
      NomeCompleto nome;
      if (entity.getPrimeiroNome() != null && !entity.getPrimeiroNome().isEmpty()) {
        nome = new NomeCompleto(
                entity.getPrimeiroNome(),
                entity.getSobrenome() != null ? entity.getSobrenome() : ""
        );
      } else if (entity.getNomeCompleto() != null && !entity.getNomeCompleto().isEmpty()) {
        // Fallback: usar nome completo se primeiroNome não existir
        String[] partes = entity.getNomeCompleto().split(" ", 2);
        String primeiroNome = partes[0];
        String sobrenome = partes.length > 1 ? partes[1] : "";
        nome = new NomeCompleto(primeiroNome, sobrenome);
      } else {
        throw new DomainException("Nome do paciente é obrigatório");
      }

      // Documento (opcional)
      Documento documento = null;
      if (entity.getDocumentoNumero() != null && entity.getDocumentoTipo() != null) {
        documento = Documento.criar(
                entity.getDocumentoNumero(),
                Documento.TipoDocumento.valueOf(entity.getDocumentoTipo())
        );
      }

      // Email (opcional)
      Email email = null;
      if (entity.getEmail() != null && !entity.getEmail().isEmpty()) {
        email = new Email(entity.getEmail());
      }

      // Data nascimento (obrigatória)
      DataNascimento dataNascimento = null;
      if (entity.getDataNascimento() != null) {
        dataNascimento = new DataNascimento(entity.getDataNascimento());
      }

      // Sexo (opcional)
      Sexo sexo = null;
      if (entity.getSexoCodigo() != null) {
        sexo = new Sexo(entity.getSexoCodigo());
      }

      // Endereço (opcional)
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

      // Telefones (opcional)
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