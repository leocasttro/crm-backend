package org.br.ltec.crmbackend.crm.auth.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.application.command.CreateUserCommand;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.port.UserRepository;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User execute(CreateUserCommand command) {
    Email email = new Email(command.getEmail());

    userRepository.findByEmail(email)
            .ifPresent(u -> { throw new IllegalStateException("Usuário já existe"); });

    String senhaHash = passwordEncoder.encode(command.getSenha());

    User user = User.novo(
            command.getNome(),
            email,
            senhaHash,
            command.getRole()
    );

    return userRepository.save(user); // retorna o persistido
  }
}

