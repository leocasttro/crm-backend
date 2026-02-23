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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

  private final CreatePedidoUseCase createUseCase;
  private final FindPedidoUseCase findUseCase;
  private final AgendarPedidoUseCase agendarUseCase;
  private final StatusPedidoUseCase statusUseCase;
  private final UpdatePedidoUseCase updateUseCase;
  private final CreatePedidoFromPdfUseCase createFromPdfUseCase;

  // ✅ NOVO: para criar paciente dentro do POST /api/pedidos quando pacienteId não vier
  private final CreatePacienteUseCase createPacienteUseCase;

  @PostMapping
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CreatePedidoRequest req) {

    // ✅ Resolver pacienteId:
    // - se veio pacienteId -> usa
    // - se não veio -> cria paciente com req.getPaciente() e usa o id gerado
    String pacienteId = req.getPacienteId();

    if (pacienteId == null || pacienteId.isBlank()) {
      CreatePacienteCommand pacienteCmd = req.getPaciente();

      if (pacienteCmd == null) {
        throw new IllegalArgumentException("Informe pacienteId ou os dados do paciente (campo 'paciente').");
      }

      Paciente pacienteCriado = createPacienteUseCase.execute(pacienteCmd);
      pacienteId = pacienteCriado.getId().getValue().toString();
    }

    // Converter string para enum de Prioridade
    Prioridade.Tipo tipoPrioridade = Prioridade.Tipo.fromString(req.getPrioridade());

    // Criar command usando setters
    CreatePedidoCommand command = new CreatePedidoCommand();

    // Configurar campos básicos
    command.setPacienteId(pacienteId);
    command.setUsuarioCriacao("usuario_atual");
    command.setDataPedido(req.getDataPedido() != null ? req.getDataPedido() : LocalDate.now());

    // Configurar médico solicitante
    command.setMedicoSolicitanteNome(req.getMedicoSolicitanteNome());
    command.setMedicoSolicitanteCrm(req.getMedicoSolicitanteCrm());
    command.setMedicoSolicitanteEspecialidade(req.getMedicoSolicitanteEspecialidade());

    // Configurar procedimento
    command.setProcedimentoCodigoTUSS(req.getProcedimentoCodigo());
    command.setProcedimentoDescricao(req.getProcedimentoNome());
    command.setProcedimentoCategoria(req.getProcedimentoCategoria());

    // Configurar convênio
    command.setConvenioNome(req.getConvenioNome());
    command.setConvenioNumeroCarteira(req.getConvenioNumeroCarteira());
    command.setConvenioValidadeCarteira(req.getConvenioValidadeCarteira());
    command.setConvenioTipoPlano(req.getConvenioTipoPlano());

    // Configurar CID
    command.setCidCodigo(req.getCidCodigo());
    command.setCidDescricao(req.getCidDescricao());

    // Configurar prioridade
    command.setPrioridade(new Prioridade(tipoPrioridade, req.getPrioridadeJustificativa()));

    // Configurar lateralidade
    if (req.getLateralidade() != null) {
      command.setLateralidade(new Lateralidade(req.getLateralidade()));
    }

    // Configurar status inicial
    command.setStatus(StatusPedido.Tipo.RASCUNHO);

    // Configurar observação inicial
    List<String> observacoes = new ArrayList<>();
    if (req.getObservacaoInicial() != null && !req.getObservacaoInicial().trim().isEmpty()) {
      observacoes.add(req.getObservacaoInicial());
    }
    command.setObservacoes(observacoes);

    // Configurar agendamento pretendido (se fornecido)
    if (req.getDataAgendamentoPretendida() != null) {
      command.setAgendamentoDataHora(req.getDataAgendamentoPretendida());
      command.setAgendamentoObservacoes("Agendamento pretendido informado na criação");
    }

    // Lista vazia de documentos inicialmente
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
  public ResponseEntity<PedidoResponse> importarPdf(
          @RequestPart("pdf") MultipartFile pdf,
          @RequestParam("pacienteId") String pacienteId
  ) throws IOException {

    CreatePedidoFromPdfCommand cmd = new CreatePedidoFromPdfCommand();
    cmd.setPdfBytes(pdf.getBytes());
    cmd.setOriginalFilename(pdf.getOriginalFilename());
    cmd.setContentType(pdf.getContentType());
    cmd.setPacienteId(pacienteId);

    PedidoCirurgico pedido = createFromPdfUseCase.execute(cmd);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(pedido));
  }

  private PedidoResponse toResponse(PedidoCirurgico p) {
    return PedidoResponse.builder()
            .id(p.getId().getValue().toString())
            .pacienteId(p.getPacienteId().getValue().toString())
            .medicoSolicitante(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteEspecialidade(p.getMedicoSolicitante().getEspecialidade())
            .procedimento(p.getProcedimento().getDescricao())
            .procedimentoCodigo(p.getProcedimento().getCodigoTUSS())
            .convenio(p.getConvenio().getNome())
            .convenioValidadeCarteira(p.getConvenio().getValidadeCarteira())
            .cid(p.getCid() != null ? p.getCid().getCodigo() : null)
            .cidDescricao(p.getCid() != null ? p.getCid().getDescricao() : null)
            .prioridade(p.getPrioridade().getTipo().name())
            .prioridadeJustificativa(p.getPrioridade().getJustificativa())
            .status(p.getStatus().getTipo().name())
            .lateralidade(p.getLateralidade().getTipo().name())
            .dataPedido(p.getDataPedido())
            .criadoEm(p.getCriadoEm())
            .agendadoPara(p.temAgendamento() ? p.getAgendamento().getDataHora() : null)
            .quantidadeObservacoes(p.getObservacoes().size())
            .temDocumentos(p.temDocumentos())
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
            .build();
  }
}