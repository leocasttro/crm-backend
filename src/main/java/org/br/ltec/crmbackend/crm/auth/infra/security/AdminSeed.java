package org.br.ltec.crmbackend.crm.auth.infra.security;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.application.useCase.CreateUserUseCase;
import org.br.ltec.crmbackend.crm.auth.application.command.CreateUserCommand;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeed implements CommandLineRunner {

  private final CreateUserUseCase createUserUseCase;

  @Override
  public void run(String... args) {
    try {
      // Criar admin se não existir
      CreateUserCommand adminCommand = CreateUserCommand.builder()
              .nome("Administrador")
              .email("admin@hospital.com")
              .senha("Admin123!")
              .role(Role.ROLE_ADMIN)
              .ativo(true)
              .build();

      createUserUseCase.execute(adminCommand);
      System.out.println("Admin user created successfully");
    } catch (Exception e) {
      System.out.println("Admin user already exists or error: " + e.getMessage());
    }
  }
}