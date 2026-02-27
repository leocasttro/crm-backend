package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.application.useCase.CreatePacienteUseCase;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.pedidos.application.command.*;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.*;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

  private final CreatePedidoUseCase createUseCase;
  private final FindPedidoUseCase findUseCase;
  private final AgendarPedidoUseCase agendarUseCase;
  private final StatusPedidoUseCase statusUseCase;
  private final UpdatePedidoUseCase updatePedidoUseCase;
  private final CreatePedidoFromPdfUseCase createFromPdfUseCase;

  // ✅ NOVO: para criar paciente dentro do POST /api/pedidos quando pacienteId não vier
  private final CreatePacienteUseCase createPacienteUseCase;

  @PostMapping
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CreatePedidoRequest req) {
    String pacienteId = req.getPacienteId();

    if (pacienteId == null || pacienteId.isBlank()) {
      CreatePacienteCommand pacienteCmd = req.getPaciente();

      if (pacienteCmd == null) {
        throw new IllegalArgumentException("Informe pacienteId ou os dados do paciente (campo 'paciente').");
      }

      Paciente pacienteCriado = createPacienteUseCase.execute(pacienteCmd);
      pacienteId = pacienteCriado.getId().getValue().toString();
    }

    Prioridade.Tipo tipoPrioridade = Prioridade.Tipo.fromString(req.getPrioridade());

    CreatePedidoCommand command = new CreatePedidoCommand();

    command.setPacienteId(pacienteId);
    command.setUsuarioCriacao("usuario_atual");
    command.setDataPedido(req.getDataPedido() != null ? req.getDataPedido() : LocalDate.now());

    command.setMedicoSolicitanteNome(req.getMedicoSolicitanteNome());
    command.setMedicoSolicitanteCrm(req.getMedicoSolicitanteCrm());
    command.setMedicoSolicitanteEspecialidade(req.getMedicoSolicitanteEspecialidade());

    command.setProcedimentoCodigoTUSS(req.getProcedimentoCodigo());
    command.setProcedimentoDescricao(req.getProcedimentoNome());
    command.setProcedimentoCategoria(req.getProcedimentoCategoria());

    command.setConvenioNome(req.getConvenioNome());
    command.setConvenioNumeroCarteira(req.getConvenioNumeroCarteira());
    command.setConvenioValidadeCarteira(req.getConvenioValidadeCarteira());
    command.setConvenioTipoPlano(req.getConvenioTipoPlano());

    command.setCidCodigo(req.getCidCodigo());
    command.setCidDescricao(req.getCidDescricao());

    command.setPrioridade(new Prioridade(tipoPrioridade, req.getPrioridadeJustificativa()));

    if (req.getLateralidade() != null) {
      command.setLateralidade(new Lateralidade(req.getLateralidade()));
    }

    command.setStatus(StatusPedido.Tipo.RASCUNHO);

    List<String> observacoes = new ArrayList<>();
    if (req.getObservacaoInicial() != null && !req.getObservacaoInicial().trim().isEmpty()) {
      observacoes.add(req.getObservacaoInicial());
    }
    command.setObservacoes(observacoes);

    if (req.getDataAgendamentoPretendida() != null) {
      command.setAgendamentoDataHora(req.getDataAgendamentoPretendida());
      command.setAgendamentoObservacoes("Agendamento pretendido informado na criação");
    }

    command.setDocumentosAnexados(new ArrayList<>());

    PedidoCirurgico pedido = createUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(pedido));
  }

  @GetMapping
  public ResponseEntity<List<PedidoResponse>> listar(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size) {

    List<PedidoCirurgico> pedidos = findUseCase.findAll(page, size);

    List<PedidoResponse> dtos = pedidos.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PedidoDetalhadoResponse> buscarPorId(@PathVariable String id) {
    PedidoCirurgico pedido = findUseCase.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

    return ResponseEntity.ok(toDetalhadoResponse(pedido));
  }

  @PatchMapping("/{id}/agendar")
  public ResponseEntity<PedidoResponse> agendar(
          @PathVariable String id,
          @RequestBody AgendamentoPedidoCommand command) {

    command.setPedidoId(id);
    PedidoCirurgico atualizado = agendarUseCase.execute(command);

    return ResponseEntity.ok(toResponse(atualizado));
  }

  @PostMapping(value = "/importar-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImportPedidoPdfResponse> importarPdf(@RequestPart("pdf") MultipartFile pdf) throws IOException {

    ImportPedidoPdfResponse response = createFromPdfUseCase.execute(pdf);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  private PedidoResponse toResponse(PedidoCirurgico p) {

    List<PedidoResponse.ProcedimentoResponse> procedimentosResponse = null;
    if (p.getTodosProcedimentos() != null && !p.getTodosProcedimentos().isEmpty()) {
      procedimentosResponse = p.getTodosProcedimentos().stream()
              .map(this::mapProcedimento)
              .collect(Collectors.toList());
    }

    return PedidoResponse.builder()
            .id(p.getId().getValue().toString())
            .pacienteId(p.getPacienteId().getValue().toString())

            // Status e prioridade
            .status(p.getStatus().getTipo().name())
            .prioridade(p.getPrioridade().getTipo().name())
            .prioridadeJustificativa(p.getPrioridade().getJustificativa())

            // Datas
            .criadoEm(p.getCriadoEm())
            .atualizadoEm(p.getAtualizadoEm())
            .dataPedido(p.getDataPedido())
            .agendadoPara(p.temAgendamento() ? p.getAgendamento().getDataHora() : null)

            // Procedimento
            .procedimento(p.getProcedimento().getDescricao())
            .procedimentoDescricao(p.getProcedimento().getDescricao())
            .procedimentoCodigo(p.getProcedimento().getCodigoTUSS())
            .procedimentoCategoria(p.getProcedimento().getCategoria())
            .procedimentos(procedimentosResponse)
            // 🔥 DADOS CLÍNICOS
            .indicacaoClinica(p.getIndicacaoClinica())
            .relatorioPreOperatorio(p.getRelatorioPreOperatorio())
            .orientacoes(p.getOrientacoes())

            // Lateralidade
            .lateralidade(p.getLateralidade().getTipo().name())

            // Convênio
            .convenio(p.getConvenio().getNome())
            .convenioNome(p.getConvenio().getNome())
            .convenioNumeroCarteira(p.getConvenio().getNumeroCarteira())
            .convenioValidadeCarteira(p.getConvenio().getValidade())
            .convenioTipoPlano(p.getConvenio().getTipoPlano())

            // Número carteira (alias)
            .numeroCarteira(p.getConvenio().getNumeroCarteira())
            .validadeCarteira(p.getConvenio().getValidade())

            // CID
            .cid(p.getCid() != null ? p.getCid().getCodigo() : null)
            .cidCodigo(p.getCid() != null ? p.getCid().getCodigo() : null)
            .cidDescricao(p.getCid() != null ? p.getCid().getDescricao() : null)

            .cidCodigo2(p.getCidCodigo2())
            .cidCodigo3(p.getCidCodigo3())
            .cidCodigo4(p.getCidCodigo4())

            .medicoSolicitante(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteNome(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteCrm(p.getMedicoSolicitante().getCrm())
            .medicoSolicitanteEspecialidade(p.getMedicoSolicitante().getEspecialidade())

            // 🔥 Médico executor
            .medicoExecutorNome(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getNome() : null)
            .medicoExecutorCrm(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getCrm() : null)
            .medicoExecutorEspecialidade(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getEspecialidade() : null)

            // 🔥 Dados da guia
            .numeroGuia(p.getNumeroGuia())
            .registroAns(p.getRegistroAns())
            .numeroGuiaOperadora(p.getNumeroGuiaOperadora())
            .codigoOperadora(p.getCodigoOperadora())
            .nomeContratado(p.getNomeContratado())

            // 🔥 Dados da internação
            .caraterAtendimento(p.getCaraterAtendimento())
            .tipoInternacao(p.getTipoInternacao())
            .regimeInternacao(p.getRegimeInternacao())
            .qtdDiariasSolicitadas(p.getQtdDiariasSolicitadas())

            // Documentos e observações
            .temDocumentos(!p.getDocumentosAnexados().isEmpty())
            .documentosAnexados(p.getDocumentosAnexados())
            .observacoes(p.getObservacoes())
            .quantidadeObservacoes(p.getObservacoes().size())

            .build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<PedidoResponse> atualizarPedido(
          @PathVariable String id,
          @Valid @RequestBody UpdatePedidoRequest request) {

    UpdatePedidoCommand command = new UpdatePedidoCommand();
    command.setPedidoId(id);
    command.setUsuarioAtualizacao("usuario_atual"); // Pegar do usuário logado (via SecurityContext)

    // ==================== DADOS DO PACIENTE ====================
    // Nome Completo
    if (request.getNomePaciente() != null) {
      command.setNomePaciente(request.getNomePaciente());
    }

    // Data de Nascimento
    if (request.getDataNascimento() != null) {
      command.setDataNascimento(request.getDataNascimento());
    }

    // CPF
    if (request.getCpfPaciente() != null) {
      command.setCpfPaciente(request.getCpfPaciente());
    }

    // Email
    if (request.getEmailPaciente() != null) {
      command.setEmailPaciente(request.getEmailPaciente());
    }

    // Telefones - ✅ Agora recebe List<Telefone> diretamente
    if (request.getTelefonesPaciente() != null && !request.getTelefonesPaciente().isEmpty()) {
      command.setTelefonePaciente(request.getTelefonesPaciente());
    }

    // Sexo
    if (request.getSexoPaciente() != null) {
      command.setSexoPaciente(request.getSexoPaciente());
    }

    // Endereço (String)
    if (request.getEnderecoPaciente() != null) {
      command.setEnderecoPaciente(request.getEnderecoPaciente());
    }

    // ==================== MÉDICO ====================
    command.setMedicoSolicitanteNome(request.getMedicoSolicitanteNome());
    command.setMedicoSolicitanteCrm(request.getMedicoSolicitanteCrm());
    command.setMedicoSolicitanteEspecialidade(request.getMedicoSolicitanteEspecialidade());

    // ==================== PROCEDIMENTO ====================
    command.setProcedimentoCodigoTUSS(request.getProcedimentoCodigoTUSS());
    command.setProcedimentoDescricao(request.getProcedimentoDescricao());
    command.setProcedimentoCategoria(request.getProcedimentoCategoria());

    // ==================== CONVÊNIO ====================
    command.setConvenioNome(request.getConvenioNome());
    command.setConvenioNumeroCarteira(request.getConvenioNumeroCarteira());
    command.setConvenioValidadeCarteira(request.getConvenioValidadeCarteira());
    command.setConvenioTipoPlano(request.getConvenioTipoPlano());

    // ==================== CID ====================
    command.setCidCodigo(request.getCidCodigo());
    command.setCidDescricao(request.getCidDescricao());

    // ==================== PRIORIDADE ====================
    if (request.getPrioridade() != null) {
      command.setPrioridade(new Prioridade(
              Prioridade.Tipo.fromString(request.getPrioridade()),
              null // justificativa se houver
      ));
    }

    // ==================== DATA DO PEDIDO ====================
    command.setDataPedido(request.getDataPedido());

    // ==================== OBSERVAÇÕES ====================
    command.setObservacoes(request.getObservacoes());

    // ==================== EXECUTAR USE CASE ====================
    PedidoCirurgico pedidoAtualizado = updatePedidoUseCase.execute(command);

    // ==================== RETORNAR RESPOSTA ====================
    return ResponseEntity.ok(toResponse(pedidoAtualizado));
  }

  private PedidoDetalhadoResponse toDetalhadoResponse(PedidoCirurgico p) {
    return PedidoDetalhadoResponse.builder()
            .id(p.getId().getValue().toString())
            .pacienteId(p.getPacienteId().getValue().toString())
            .medicoSolicitante(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteCrm(p.getMedicoSolicitante().getCrm())
            .medicoExecutor(p.temMedicoExecutor() ? p.getMedicoExecutor().getNome() : null)
            .procedimento(p.getProcedimento().getDescricao())
            .procedimentoCodigo(p.getProcedimento().getCodigoTUSS())
            .convenio(p.getConvenio().getNome())
            .convenioNumeroCarteira(p.getConvenio().getNumeroCarteira())
            .cid(p.getCid() != null ? p.getCid().getCodigo() : null)
            .lateralidade(p.getLateralidade().getTipo().name())
            .prioridade(p.getPrioridade().getTipo().name())
            .status(p.getStatus().getTipo().name())
            .dataPedido(p.getDataPedido())
            .criadoEm(p.getCriadoEm())
            .usuarioCriacao(p.getUsuarioCriacao())
            .atualizadoEm(p.getAtualizadoEm())
            .usuarioAtualizacao(p.getUsuarioAtualizacao())
            .agendadoPara(p.temAgendamento() ? p.getAgendamento().getDataHora() : null)
            .observacoes(p.getObservacoes())
            .documentosAnexados(p.getDocumentosAnexados())
            .build();
  }

  private PedidoResponse.ProcedimentoResponse mapProcedimento(Procedimento procedimento) {
    if (procedimento == null) return null;

    return PedidoResponse.ProcedimentoResponse.builder()
            .codigoTUSS(procedimento.getCodigoTUSS())
            .descricao(procedimento.getDescricao())
            .categoria(procedimento.getCategoria())
            .build();
  }
}