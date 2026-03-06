package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import org.br.ltec.crmbackend.crm.pedidos.application.command.DownloadArquivoCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.command.UploadArquivoCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.ConsultarArquivoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.DownloadArquivoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.UploadArquivoUseCase;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/arquivos")
public class PedidoArquivoController {

  private final UploadArquivoUseCase uploadUseCase;
  private final DownloadArquivoUseCase downloadUseCase;
  private final ConsultarArquivoUseCase consultarUseCase;

  public PedidoArquivoController(
          UploadArquivoUseCase uploadUseCase,
          DownloadArquivoUseCase downloadUseCase,
          ConsultarArquivoUseCase consultarUseCase) {
    this.uploadUseCase = uploadUseCase;
    this.downloadUseCase = downloadUseCase;
    this.consultarUseCase = consultarUseCase;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UploadArquivoResponse> uploadArquivo(
          @PathVariable String pedidoId,
          @RequestPart("arquivo") MultipartFile arquivo,
          @RequestParam(required = false) String observacao,
          @RequestParam(required = false) Long checklistItemId) {

    UploadArquivoCommand command = new UploadArquivoCommand(
            pedidoId,
            arquivo,
            observacao,
            getUsuarioLogado()
    );

    command.setChecklistItemId(checklistItemId);
    ResultadoOperacao<PedidoArquivoRepository.PedidoArquivoSalvo> resultado =
            uploadUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.badRequest()
              .body(UploadArquivoResponse.erro(resultado.getMensagem()));
    }

    return ResponseEntity.ok(UploadArquivoResponse.sucesso(
            resultado.getDados(),
            resultado.getMensagem()
    ));
  }

  @GetMapping
  public ResponseEntity<byte[]> downloadArquivo(
          @PathVariable String pedidoId,
          @RequestParam(required = false) String arquivoId) {

    String idArquivo = arquivoId;

    if (idArquivo == null) {
      ResultadoOperacao<List<PedidoArquivoRepository.PedidoArquivoSalvo>> arquivos =
              consultarUseCase.listarArquivosDoPedido(pedidoId);

      if (!arquivos.isSucesso() || arquivos.getDados().isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      idArquivo = arquivos.getDados().get(0).id().toString();
    }

    DownloadArquivoCommand command = new DownloadArquivoCommand(pedidoId, idArquivo);
    DownloadArquivoUseCase.DownloadResult resultado = downloadUseCase.execute(command);

    if (!resultado.isSucesso()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resultado.getNomeArquivo() + "\"")
            .contentType(MediaType.parseMediaType(resultado.getContentType()))
            .body(resultado.getConteudo());
  }

  @GetMapping("/checklist/{checklistItemId}/existe")
  public ResponseEntity<Boolean> verificarArquivoExistente(
          @PathVariable String pedidoId,
          @PathVariable Long checklistItemId
  ) {
    ResultadoOperacao<Boolean> resultado = consultarUseCase.existeArquivoParaItem(
            pedidoId, checklistItemId
    );

    return resultado.isSucesso()
            ? ResponseEntity.ok(resultado.getDados())
            : ResponseEntity.badRequest().build();
  }

  @GetMapping("/checklist/{checklistItemId}")
  public ResponseEntity<UploadArquivoResponse> buscarArquivoPorItem(
          @PathVariable String pedidoId,
          @PathVariable Long checklistItemId
  ) {
    ResultadoOperacao<PedidoArquivoRepository.PedidoArquivoSalvo> resultadoOperacao = consultarUseCase.buscarArquivoPorItem(
            pedidoId, checklistItemId
    );

    if (!resultadoOperacao.isSucesso()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(UploadArquivoResponse.sucesso(
            resultadoOperacao.getDados(),
            "Arquivo encontrado"
    ));
  }

  @GetMapping("lista")
  public ResponseEntity<List<UploadArquivoResponse>> listaArquivos(
          @PathVariable String pedidoId) {
    ResultadoOperacao<List<PedidoArquivoRepository.PedidoArquivoSalvo>> resultadoOperacao =
            consultarUseCase.listarArquivosDoPedido(pedidoId);

    if (!resultadoOperacao.isSucesso()) {
      return ResponseEntity.badRequest().build();
    }

    List<UploadArquivoResponse> responses = resultadoOperacao.getDados().stream()
            .map(salvo -> UploadArquivoResponse.sucesso(salvo, "Arquivo listado")) // USANDO LAMBDA
            .collect(Collectors.toList());

    return ResponseEntity.ok(responses);
  }

  @GetMapping("/checklist/{checklistItemId}/download")
  public ResponseEntity<byte[]> downloadArquivoPorItem(
          @PathVariable String pedidoId,
          @PathVariable Long checklistItemId) {

    ResultadoOperacao<DownloadArquivoUseCase.DownloadResult> resultado =
            consultarUseCase.downloadArquivoPorItem(pedidoId, checklistItemId);

    if (!resultado.isSucesso()) {
      return ResponseEntity.notFound().build();
    }

    DownloadArquivoUseCase.DownloadResult downloadResult = resultado.getDados();

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + downloadResult.getNomeArquivo() + "\"")
            .contentType(MediaType.parseMediaType(downloadResult.getContentType()))
            .body(downloadResult.getConteudo());
  }


  private String getUsuarioLogado() {
    return "usuario_atual";
  }
}