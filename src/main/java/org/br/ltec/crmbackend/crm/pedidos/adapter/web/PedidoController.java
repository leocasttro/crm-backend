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
  private final UpdatePedidoUseCase updatePedidoUseCase;
  private final CreatePedidoFromPdfUseCase createFromPdfUseCase;
  private final CreatePacienteUseCase createPacienteUseCase;
  private final PedidoResponseMapper mapper;
  private final PedidoCommandFactory commandFactory;

  @PostMapping
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody CreatePedidoRequest req) {
    String pacienteId = req.getPacienteId();

    if (pacienteId == null || pacienteId.isBlank()) {
      if (req.getPaciente() == null) {
        throw new IllegalArgumentException("Informe pacienteId ou os dados do paciente.");
      }
      Paciente pacienteCriado = createPacienteUseCase.execute(req.getPaciente());
      pacienteId = pacienteCriado.getId().getValue().toString();
    }

    PedidoCirurgico pedido = createUseCase.execute(
            commandFactory.toCreateCommand(req, pacienteId, getUsuarioLogado())
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(pedido));
  }

  @GetMapping
  public ResponseEntity<List<PedidoResponse>> listar(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(
            findUseCase.findAll(page, size).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList())
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<PedidoDetalhadoResponse> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(
            mapper.toDetalhadoResponse(
                    findUseCase.findById(id)
                            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id))
            )
    );
  }

  @PostMapping(value = "/importar-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImportPedidoPdfResponse> importarPdf(@RequestPart("pdf") MultipartFile pdf) throws IOException {
    return ResponseEntity.status(HttpStatus.CREATED).body(createFromPdfUseCase.execute(pdf));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UpdateResponse> atualizarPedido(
          @PathVariable String id,
          @Valid @RequestBody UpdatePedidoRequest request) {

    ResultadoOperacao<PedidoCirurgico> resultado = updatePedidoUseCase.execute(
            commandFactory.toUpdateCommand(id, request, getUsuarioLogado())
    );

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest().body(UpdateResponse.erro(resultado.getMensagem(), resultado.getErros()));
    }

    return ResponseEntity.ok(UpdateResponse.sucesso(
            resultado.getMensagem(),
            resultado.getErros(),
            mapper.toResponse(resultado.getDados())
    ));
  }

  @GetMapping("/{id}/pode-editar")
  public ResponseEntity<PermissaoEdicaoResponse> podeEditar(@PathVariable String id) {
    PedidoCirurgico pedido = findUseCase.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    return ResponseEntity.ok(new PermissaoEdicaoResponse(
            pedido.podeSerEditado(),
            pedido.getStatus().getTipo().name()
    ));
  }

  private String getUsuarioLogado() {
    return "usuario_atual";
  }
}