package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Documento;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Sexo;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Telefone;
import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Procedimento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PedidoExtraidoMapper {

  private static final DateTimeFormatter PDF_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public CreatePedidoCommand toCreatePedidoCommand(PedidoExtraido extraido) {

    CreatePedidoCommand cmd = new CreatePedidoCommand();

    CreatePacienteCommand pacienteCmd = criarPacienteCommand(extraido);
    cmd.setPaciente(pacienteCmd);

    cmd.setConvenioNome(extraido.getConvenio());
    cmd.setConvenioNumeroCarteira(extraido.getNumeroCarteira() != null ?
            extraido.getNumeroCarteira() : extrairNumeroCarteira(extraido.getTextoNormalizado()));

    if (extraido.getValidadeCarteira() != null && !extraido.getValidadeCarteira().isEmpty()) {
      try {
        cmd.setConvenioValidadeCarteira(LocalDate.parse(extraido.getValidadeCarteira(), PDF_DATE));
      } catch (Exception e) {
        LocalDate validade = extrairValidadeCarteira(extraido.getTextoNormalizado());
        cmd.setConvenioValidadeCarteira(validade);
      }
    } else {
      LocalDate validade = extrairValidadeCarteira(extraido.getTextoNormalizado());
      if (validade != null) {
        cmd.setConvenioValidadeCarteira(validade);
      }
    }

    cmd.setCidCodigo(extraido.getCid());

    cmd.setCidCodigo2(extraido.getCid2());
    cmd.setCidCodigo3(extraido.getCid3());
    cmd.setCidCodigo4(extraido.getCid4());

    cmd.setMedicoSolicitanteNome(extraido.getMedicoNome());
    if (extraido.getCrmNumero() != null) {
      String crm = extraido.getCrmNumero();
      if (extraido.getCrmUf() != null) {
        crm = crm + "/" + extraido.getCrmUf();
      }
      cmd.setMedicoSolicitanteCrm(crm);
    }

    cmd.setMedicoSolicitanteEspecialidade(extraido.getCbo());
    cmd.setMedicoExecutorNome(extraido.getMedicoExecutorNome());
    cmd.setMedicoExecutorCrm(extraido.getMedicoExecutorCrm());
    cmd.setMedicoExecutorEspecialidade(extraido.getMedicoExecutorEspecialidade());

    if (extraido.getProcedimentos() != null && !extraido.getProcedimentos().isEmpty()) {
      List<Procedimento> procedimentos = new ArrayList<>();

      for (var procedimentoExtraido : extraido.getProcedimentos()) {
        Procedimento proc = new Procedimento(
                procedimentoExtraido.getCodigo(),
                procedimentoExtraido.getDescricao(),
                ""  // categoria vazia
        );
        procedimentos.add(proc);
      }

      cmd.setProcedimentos(procedimentos);
    }

    cmd.setIndicacaoClinica(limparIndicacaoClinica(extraido.getIndicacaoClinica()));
    cmd.setRelatorioPreOperatorio(limparIndicacaoClinica(extraido.getRelatorioPreOperatorio()));
    cmd.setOrientacoes(limparTextoOcr(extraido.getOrientacoes()));

    if (extraido.getDataPedido() != null && !extraido.getDataPedido().isBlank()) {
      cmd.setDataPedido(LocalDate.parse(extraido.getDataPedido(), PDF_DATE));
    }

    if (extraido.getCaraterAtendimento() != null) {
      String carater = extraido.getCaraterAtendimento().toUpperCase();
      if (carater.contains("URG")) {
        cmd.setPrioridade(new Prioridade(Prioridade.Tipo.URGENTE));
      } else {
        cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA));
      }
    } else {
      cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA));
    }

    if (extraido.getLateralidade() != null) {
      cmd.setLateralidade(mapearLateralidade(extraido.getLateralidade()));
    } else {
      Lateralidade lateralidade = extrairLateralidade(extraido.getTextoNormalizado());
      cmd.setLateralidade(lateralidade != null ? lateralidade :
              new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL));
    }

    cmd.setNumeroGuia(extraido.getNumeroGuia());
    cmd.setRegistroAns(extraido.getRegistroAns());
    cmd.setNumeroGuiaOperadora(extraido.getNumeroGuiaOperadora());
    cmd.setCodigoOperadora(extraido.getCodigoOperadora());
    cmd.setNomeContratado(extraido.getNomeContratado());

    cmd.setCaraterAtendimento(extraido.getCaraterAtendimento());
    cmd.setTipoInternacao(extraido.getTipoInternacao());
    cmd.setRegimeInternacao(extraido.getRegimeInternacao());
    cmd.setQtdDiariasSolicitadas(extraido.getQtdDiariasSolicitadas());

    cmd.setTelefonePaciente(extraido.getTelefone());
    cmd.setEnderecoPaciente(extraido.getEnderecoMedico());
    cmd.setCpfPaciente(extraido.getCpf());
    cmd.setEmailPaciente(extraido.getEmail());
    cmd.setSexoPaciente(extraido.getSexo());

    List<String> observacoes = new ArrayList<>();

    if (extraido.getTextoNormalizado() != null && !extraido.getTextoNormalizado().isEmpty()) {
      String obs = extraido.getTextoNormalizado();
      if (obs.length() > 500) {
        obs = obs.substring(0, 500) + "...";
      }
      observacoes.add(obs);
    }

    if (extraido.getOrientacoes() != null && !extraido.getOrientacoes().isEmpty()) {
      observacoes.add("ORIENTAÇÕES: " + extraido.getOrientacoes());
    }

    if (!observacoes.isEmpty()) {
      cmd.setObservacoes(observacoes);
    }

    return cmd;
  }

  private CreatePacienteCommand criarPacienteCommand(PedidoExtraido extraido) {
    CreatePacienteCommand pacienteCmd = new CreatePacienteCommand();

    String nomeCompleto = extraido.getNomePaciente();
    if (nomeCompleto != null && !nomeCompleto.isEmpty()) {
      String[] partes = nomeCompleto.split(" ", 2);
      pacienteCmd.setPrimeiroNome(partes[0]);
      pacienteCmd.setSobrenome(partes.length > 1 ? partes[1] : "");
    }

    String dataNascimentoStr = extraido.getDataNascimento();
    if (dataNascimentoStr != null && !dataNascimentoStr.isEmpty()) {
      try {
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, PDF_DATE);
        pacienteCmd.setDataNascimento(dataNascimento);
      } catch (Exception e) {
        System.err.println("Erro ao converter data: " + dataNascimentoStr);
      }
    }

    String cpf = extraido.getCpf() != null ? extraido.getCpf() : extrairCpf(extraido.getTextoNormalizado());
    if (cpf != null) {
      pacienteCmd.setDocumentoNumero(cpf.replaceAll("[^0-9]", ""));
      pacienteCmd.setDocumentoTipo(Documento.TipoDocumento.CPF);
    }

    if (extraido.getEmail() != null) {
      pacienteCmd.setEmail(extraido.getEmail());
    }

    if (extraido.getTelefone() != null) {
      List<CreatePacienteCommand.TelefoneRequest> telefones = new ArrayList<>();
      telefones.add(new CreatePacienteCommand.TelefoneRequest(
              extraido.getTelefone(),
              Telefone.TipoTelefone.CELULAR,
              true
      ));
      pacienteCmd.setTelefones(telefones);
    }

    if (extraido.getSexo() != null) {
      String sexoStr = extraido.getSexo().toUpperCase();
      if (sexoStr.contains("M") || sexoStr.equals("MASCULINO")) {
        pacienteCmd.setSexo(Sexo.Tipo.MASCULINO);
      } else if (sexoStr.contains("F") || sexoStr.equals("FEMININO")) {
        pacienteCmd.setSexo(Sexo.Tipo.FEMININO);
      } else {
        pacienteCmd.setSexo(Sexo.Tipo.NAO_INFORMADO);
      }
    }

    return pacienteCmd;
  }

  private Lateralidade mapearLateralidade(String lateralidade) {
    if (lateralidade == null) return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    String upper = lateralidade.toUpperCase();
    if (upper.contains("DIREITA")) return new Lateralidade(Lateralidade.Tipo.DIREITA);
    if (upper.contains("ESQUERDA")) return new Lateralidade(Lateralidade.Tipo.ESQUERDA);
    if (upper.contains("BILATERAL")) return new Lateralidade(Lateralidade.Tipo.BILATERAL);
    return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
  }

  private String extrairCpf(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("\\b(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})\\b");
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private String extrairNumeroCarteira(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("Número:?\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private LocalDate extrairValidadeCarteira(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("Validade:?\\s*(\\d{2}/\\d{2}/\\d{4})", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(texto);
    if (matcher.find()) {
      try {
        return LocalDate.parse(matcher.group(1), PDF_DATE);
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  private Lateralidade extrairLateralidade(String texto) {
    if (texto == null) return null;
    String textoUpper = texto.toUpperCase();
    if (textoUpper.contains("DIREITA") || textoUpper.contains(" D ")) {
      return new Lateralidade(Lateralidade.Tipo.DIREITA);
    } else if (textoUpper.contains("ESQUERDA") || textoUpper.contains(" E ")) {
      return new Lateralidade(Lateralidade.Tipo.ESQUERDA);
    } else if (textoUpper.contains("BILATERAL") || textoUpper.contains(" B ")) {
      return new Lateralidade(Lateralidade.Tipo.BILATERAL);
    } else if (textoUpper.contains("NAO SE APLICA") || textoUpper.contains("N/A")) {
      return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    }
    return null;
  }

  private String limparTextoOcr(String texto) {
    if (texto == null) return null;

    // Remove marcadores /t
    texto = texto.replaceAll("/t", " ");

    // Remove padrões como "29 - CID 10 Principal", "30 - CID 10 (2)", etc.
    texto = texto.replaceAll("\\d+\\s*-\\s*(?:CID|Tabela|Indicação).*?(?=\\s|$)", "");

    // Remove números de campos (ex: /tN40 2)
    texto = texto.replaceAll("\\s+\\d+\\s+", " ");

    // Remove espaços extras
    texto = texto.replaceAll("\\s+", " ").trim();

    // Se for um texto longo, tentar encontrar o ponto final e cortar
    int pontoFinal = texto.lastIndexOf(".");
    if (pontoFinal > 0 && pontoFinal < texto.length() - 30) {
      texto = texto.substring(0, pontoFinal + 1);
    }

    return texto;
  }

  private String limparIndicacaoClinica(String texto) {
    if (texto == null) return null;

    // Padrão para capturar apenas o texto do relatório (até começar os números de campos)
    Pattern pattern = Pattern.compile(
            "([A-ZÀ-Ú][A-ZÀ-Ú0-9\\s,.;:()\\-]{50,}?)(?=\\s*(?:/t\\d+|\\d+\\s*-\\s*CID|/t\\d+\\s*-|$))",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE
    );

    Matcher matcher = pattern.matcher(texto);
    if (matcher.find()) {
      texto = matcher.group(1);
    }

    // Limpeza adicional
    texto = texto.replaceAll("/t", " ");
    texto = texto.replaceAll("\\s+", " ").trim();

    return texto;
  }
}