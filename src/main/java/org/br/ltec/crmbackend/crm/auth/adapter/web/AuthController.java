package org.br.ltec.crmbackend.crm.auth.adapter.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.application.command.CreateUserCommand;
import org.br.ltec.crmbackend.crm.auth.application.command.LoginCommand;
import org.br.ltec.crmbackend.crm.auth.application.useCase.CreateUserUseCase;
import org.br.ltec.crmbackend.crm.auth.application.useCase.LoginUseCase;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final CreateUserUseCase createUserUseCase;
  private final LoginUseCase loginUseCase;

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
    CreateUserCommand command = CreateUserCommand.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .senha(request.getSenha())
            .role(request.getRole())
            .build();

    User user = createUserUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(toUserResponse(user));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginCommand command = LoginCommand.builder()
            .email(request.getEmail())
            .senha(request.getSenha())
            .build();

    String token = loginUseCase.execute(command);
    return ResponseEntity.ok(TokenResponse.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .build());
  }

  private UserResponse toUserResponse(User user) {
    return UserResponse.builder()
            .id(user.getId())
            .nome(user.getNome())
            .email(user.getEmail().getValue())
            .role(user.getRole())
            .ativo(user.getAtivo())
            .criadoEm(user.getCriadoEm())
            .build();
  }
}