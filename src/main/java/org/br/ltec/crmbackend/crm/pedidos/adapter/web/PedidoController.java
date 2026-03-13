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
import java.util.UUID;
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
  private final AtualizarStatusPedidoUseCase analisarPedidoUseCase;
  private final CreatePacienteUseCase createPacienteUseCase;
  private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
  private final AgendarPedidoUseCase agendarPedidoUseCase;
  private final SalvarDadosAutorizacaoUseCase salvarDadosAutorizacaoUseCase;

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

  @PostMapping(value = "/importar-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImportPedidoPdfResponse> importarPdf(@RequestPart("pdf") MultipartFile pdf) throws IOException {

    ImportPedidoPdfResponse response = createFromPdfUseCase.execute(pdf);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{pedidoId}/agendar")
  public ResponseEntity<PedidoResponse> agendarPedido(
          @PathVariable String pedidoId,
          @RequestBody AgendamentoRequest request) {

    var command = AgendamentoPedidoCommand.builder()
            .pedidoId(pedidoId)
            .dataAgendamento(request.getDataAgendamento())
            .observacao(request.getObservacao())
            .build();

    PedidoCirurgico pedido = agendarPedidoUseCase.execute(command);

    return ResponseEntity.ok(toResponse(pedido));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UpdateResponse> atualizarPedido(
          @PathVariable String id,
          @Valid @RequestBody UpdatePedidoRequest request) {

    UpdatePedidoCommand command = new UpdatePedidoCommand();
    command.setPedidoId(id);
    command.setUsuarioAtualizacao(getUsuarioLogado());

    // ==================== DADOS DO PACIENTE ====================
    if (request.getNomePaciente() != null) {
      command.setNomePaciente(request.getNomePaciente());
    }

    if (request.getDataNascimento() != null) {
      command.setDataNascimento(request.getDataNascimento());
    }

    if (request.getCpfPaciente() != null) {
      command.setCpfPaciente(request.getCpfPaciente());
    }

    if (request.getEmailPaciente() != null) {
      command.setEmailPaciente(request.getEmailPaciente());
    }

    if (request.getTelefonesPaciente() != null && !request.getTelefonesPaciente().isEmpty()) {
      command.setTelefonePaciente(request.getTelefonesPaciente());
    }

    if (request.getSexoPaciente() != null) {
      command.setSexoPaciente(request.getSexoPaciente());
    }

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
              Prioridade.Tipo.fromString(request.getPrioridade())
      ));
    }

    // ==================== DATA DO PEDIDO ====================
    command.setDataPedido(request.getDataPedido());

    // ==================== OBSERVAÇÕES ====================
    command.setObservacoes(request.getObservacoes());

    // ==================== EXECUTAR USE CASE ====================
    ResultadoOperacao<PedidoCirurgico> resultado = updatePedidoUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(UpdateResponse.erro(
                      resultado.getMensagem(),
                      resultado.getErros()
              ));
    }

    // ==================== RETORNAR RESPOSTA ====================
    return ResponseEntity.ok(UpdateResponse.sucesso(
            resultado.getMensagem(),
            resultado.getErros(),
            toResponse(resultado.getDados())
    ));
  }

  @PostMapping("/{id}/analise")
  public ResponseEntity<AnaliseResponse> analisar(
          @PathVariable String id,
          @Valid @RequestBody AnaliseRequest request) {

    AtualizarStatusCommand command = new AtualizarStatusCommand();
    command.setPedidoId(id);
    command.setUsuario(getUsuarioLogado());
    command.setObservacao(request.getObservacao());

    ResultadoOperacao<PedidoCirurgico> resultado = analisarPedidoUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(AnaliseResponse.erro(resultado.getMensagem(), resultado.getErros()));
    }

    return ResponseEntity.ok(AnaliseResponse.sucesso(
            request.isAprovado(),
            resultado.getMensagem(),
            toResponse(resultado.getDados())
    ));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<AtualizarStatusResponse> atualizarStatus(
          @PathVariable String id,
          @Valid @RequestBody AtualizarStatusRequest request) {

    AtualizarStatusCommand command = new AtualizarStatusCommand();
    command.setPedidoId(id);
    command.setNovoStatus(request.getStatus());
    command.setObservacao(request.getObservacao());
    command.setUsuario(getUsuarioLogado());

    PedidoCirurgico pedidoAntes = findUseCase.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

    String statusAnterior = pedidoAntes.getStatus().getTipo().name();

    ResultadoOperacao<PedidoCirurgico> resultado = atualizarStatusPedidoUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(AtualizarStatusResponse.erro(
                      resultado.getMensagem(),
                      resultado.getErros()
              ));
    }

    PedidoCirurgico pedido = resultado.getDados();

    return ResponseEntity.ok(AtualizarStatusResponse.sucesso(
            resultado.getMensagem(),
            pedido.getId().getValue().toString(),
            statusAnterior,
            pedido.getStatus().getTipo().name(),
            pedido.getPacienteId() != null ? pedido.getPacienteId().getValue().toString() : null,
            pedido.getProcedimento() != null ? pedido.getProcedimento().getDescricao() : null,
            getUsuarioLogado(),
            request.getObservacao()
    ));
  }

  @GetMapping("/{id}/pode-editar")
  public ResponseEntity<PermissaoEdicaoResponse> podeEditar(@PathVariable String id) {
    PedidoCirurgico pedido = findUseCase.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

    return ResponseEntity.ok(new PermissaoEdicaoResponse(
            pedido.podeSerEditado(),
            pedido.getStatus().getTipo().name()
    ));
  }

  private final IniciarAnaliseUseCase iniciarAnaliseUseCase;

  @PostMapping("/{id}/analise/iniciar")
  public ResponseEntity<ResultadoOperacao<PedidoResponse>> iniciarAnalise(@PathVariable String id) {
    ResultadoOperacao<PedidoCirurgico> resultado = iniciarAnaliseUseCase.execute(id, getUsuarioLogado());

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(ResultadoOperacao.erro(resultado.getMensagem()));
    }

    return ResponseEntity.ok(ResultadoOperacao.sucesso(
            toResponse(resultado.getDados()),
            resultado.getMensagem()
    ));
  }

  @PutMapping("/{id}/autorizacao")
  public ResponseEntity<?> salvarDadosAutorizacao(
          @PathVariable UUID id,
          @RequestBody SalvarDadosAutorizacaoRequest request
  ) {
    try {
      SalvarDadosAutorizacaoCommand command = new SalvarDadosAutorizacaoCommand(
              id,
              request.getStatusAutorizacao(),
              request.getNumeroGuia(),
              request.getSenhaAutorizacao(),
              request.getValidadeAutorizacao(),
              request.getTipoAcomodacao(),
              getUsuarioLogado()
      );

      PedidoCirurgico pedido = salvarDadosAutorizacaoUseCase.execute(command);

      return ResponseEntity.ok(UpdateResponse.sucesso(
              "Dados de autorização salvos com sucesso",
              null,  // Lista de alterações (pode ser null ou empty)
              toResponse(pedido)
      ));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(UpdateResponse.erro(
              e.getMessage(),
              (String) null  // ou List.of(e.getMessage())
      ));
    }
  }

  // ==================== MÉTODOS PRIVADOS ====================

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

            // DADOS CLÍNICOS
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

            // Médico executor
            .medicoExecutorNome(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getNome() : null)
            .medicoExecutorCrm(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getCrm() : null)
            .medicoExecutorEspecialidade(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getEspecialidade() : null)

            // Dados da guia
            .numeroGuia(p.getNumeroGuia())
            .registroAns(p.getRegistroAns())
            .numeroGuiaOperadora(p.getNumeroGuiaOperadora())
            .codigoOperadora(p.getCodigoOperadora())
            .nomeContratado(p.getNomeContratado())

            // Dados da internação
            .caraterAtendimento(p.getCaraterAtendimento())
            .tipoInternacao(p.getTipoInternacao())
            .regimeInternacao(p.getRegimeInternacao())
            .qtdDiariasSolicitadas(p.getQtdDiariasSolicitadas())

            // Documentos e observações
            .temDocumentos(!p.getDocumentosAnexados().isEmpty())
            .documentosAnexados(p.getDocumentosAnexados())
            .observacoes(p.getObservacoes())
            .quantidadeObservacoes(p.getObservacoes().size())

            // 🔥 NOVOS CAMPOS - CONSULTA PRÉ-OPERATÓRIA
            .consultaPreDataHora(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getDataHora() : null)
            .consultaPreCuidados(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getCuidados() : null)
            .consultaPreObservacoesEspeciais(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getObservacoesEspeciais() : null)

            .statusAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getStatus() != null ?
                    p.getDadosAutorizacao().getStatus().getValor() : null)
            .numeroGuiaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getNumeroGuia() != null ?
                    p.getDadosAutorizacao().getNumeroGuia().getValor() : null)
            .senhaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getSenha() != null ?
                    p.getDadosAutorizacao().getSenha().getValor() : null)
            .validadeAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getValidade() != null ?
                    p.getDadosAutorizacao().getValidade().getValor() : null)
            .tipoAcomodacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getTipoAcomodacao() != null ?
                    p.getDadosAutorizacao().getTipoAcomodacao().getValor() : null)
            .build();
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

            .consultaPreDataHora(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getDataHora() : null)
            .consultaPreCuidados(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getCuidados() : null)
            .consultaPreObservacoesEspeciais(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getObservacoesEspeciais() : null)

            .statusAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getStatus() != null ?
                    p.getDadosAutorizacao().getStatus().getValor() : null)
            .numeroGuiaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getNumeroGuia() != null ?
                    p.getDadosAutorizacao().getNumeroGuia().getValor() : null)
            .senhaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getSenha() != null ?
                    p.getDadosAutorizacao().getSenha().getValor() : null)
            .validadeAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getValidade() != null ?
                    p.getDadosAutorizacao().getValidade().getValor() : null)
            .tipoAcomodacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getTipoAcomodacao() != null ?
                    p.getDadosAutorizacao().getTipoAcomodacao().getValor() : null)

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

  private String getUsuarioLogado() {
    // TODO: Implementar com Spring Security
    // Por enquanto retorna um valor fixo
    return "usuario_atual";
  }
}