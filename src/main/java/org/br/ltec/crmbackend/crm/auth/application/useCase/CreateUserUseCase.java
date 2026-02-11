package org.br.ltec.crmbackend.crm.auth.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.application.command.CreateUserCommand;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.port.UserRepository;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User execute(CreateUserCommand command) {
    // Validar email único
    Email email = new Email(command.getEmail());
    Optional<User> usuarioExistente = userRepository.findByEmail(email);

    if (usuarioExistente.isPresent()) {
      throw new IllegalArgumentException("Email já está em uso: " + command.getEmail());
    }

    // Criar usuário
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setNome(command.getNome());
    user.setEmail(email);
    user.setSenha(passwordEncoder.encode(command.getSenha()));
    user.setRole(command.getRole() != null ? command.getRole() : Role.ROLE_USER);
    user.setAtivo(command.getAtivo() != null ? command.getAtivo() : true);
    user.setCriadoEm(LocalDateTime.now());

    // Salvar usuário
    return userRepository.save(user);
  }
}