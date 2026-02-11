package org.br.ltec.crmbackend.crm.paciente.adapter.web;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.application.command.UpdatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.application.useCase.*;
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Documento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

  private final CreatePacienteUseCase createPacienteUseCase;
  private final UpdatePacienteUseCase updatePacienteUseCase;
  private final FindPacienteUseCase findPacienteUseCase;
  private final DeletePacienteUseCase deletePacienteUseCase;

  public PacienteController(
          CreatePacienteUseCase createPacienteUseCase,
          UpdatePacienteUseCase updatePacienteUseCase,
          FindPacienteUseCase findPacienteUseCase,
          DeletePacienteUseCase deletePacienteUseCase) {
    this.createPacienteUseCase = createPacienteUseCase;
    this.updatePacienteUseCase = updatePacienteUseCase;
    this.findPacienteUseCase = findPacienteUseCase;
    this.deletePacienteUseCase = deletePacienteUseCase;
  }

  @PostMapping
  public ResponseEntity<PacienteResponse> create(@Valid @RequestBody CreatePacienteCommand command) {
    Paciente paciente = createPacienteUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(PacienteResponse.fromDomain(paciente));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PacienteResponse> update(
          @PathVariable String id,
          @Valid @RequestBody UpdatePacienteCommand command) {
    command.setPacienteId(id);
    Paciente paciente = updatePacienteUseCase.execute(command);
    return ResponseEntity.ok(PacienteResponse.fromDomain(paciente));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PacienteResponse> findById(@PathVariable String id) {
    return findPacienteUseCase.findById(id)
            .map(paciente -> ResponseEntity.ok(PacienteResponse.fromDomain(paciente)))
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/documento/{documento}")
  public ResponseEntity<PacienteResponse> findByDocumento(@PathVariable String documento) {
    return findPacienteUseCase.findByDocumento(documento)
            .map(paciente -> ResponseEntity.ok(PacienteResponse.fromDomain(paciente)))
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<PacienteResponse> findByEmail(@PathVariable String email) {
    return findPacienteUseCase.findByEmail(email)
            .map(paciente -> ResponseEntity.ok(PacienteResponse.fromDomain(paciente)))
            .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<Page<PacienteResponse>> findAll(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size) {

    List<Paciente> pacientes = findPacienteUseCase.findAll(page, size);
    long total = findPacienteUseCase.countAll();

    List<PacienteResponse> content = pacientes.stream()
            .map(PacienteResponse::fromDomain)
            .collect(Collectors.toList());

    Pageable pageable = PageRequest.of(page, size);
    Page<PacienteResponse> responsePage = new PageImpl<>(content, pageable, total);

    return ResponseEntity.ok(responsePage);
  }

  @GetMapping("/buscar")
  public ResponseEntity<Page<PacienteResponse>> findByNome(
          @RequestParam String nome,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size) {

    List<Paciente> pacientes = findPacienteUseCase.findByNome(nome, page, size);
    long total = findPacienteUseCase.countByNome(nome);

    List<PacienteResponse> content = pacientes.stream()
            .map(PacienteResponse::fromDomain)
            .collect(Collectors.toList());

    Pageable pageable = PageRequest.of(page, size);
    Page<PacienteResponse> responsePage = new PageImpl<>(content, pageable, total);

    return ResponseEntity.ok(responsePage);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    deletePacienteUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/documento/{documento}")
  public ResponseEntity<Void> deleteByDocumento(@PathVariable String documento) {
    deletePacienteUseCase.executeByDocumento(documento);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/exists")
  public ResponseEntity<Boolean> existsById(@PathVariable String id) {
    boolean exists = findPacienteUseCase.findById(id).isPresent();
    return ResponseEntity.ok(exists);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> count() {
    long total = findPacienteUseCase.countAll();
    return ResponseEntity.ok(total);
  }
}