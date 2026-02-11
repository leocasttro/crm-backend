package org.br.ltec.crmbackend.crm.auth.application.useCase;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.application.command.LoginCommand;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.port.UserRepository;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginUseCase {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public String execute(LoginCommand command) {
    try {
      // Primeiro, tentar autenticar com Spring Security
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      command.getEmail(),
                      command.getSenha()
              )
      );

      if (!authentication.isAuthenticated()) {
        throw new IllegalArgumentException("Credenciais inválidas");
      }

      // Buscar usuário para gerar token
      Email email = new Email(command.getEmail());
      User user = userRepository.findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + command.getEmail()));

      // Verificar se usuário está ativo
      if (!user.getAtivo()) {
        throw new IllegalArgumentException("Usuário desativado");
      }

      // Gerar token JWT
      return jwtService.generateToken(user);

    } catch (AuthenticationException e) {
      throw new IllegalArgumentException("Credenciais inválidas", e);
    }
  }

  // Método alternativo sem AuthenticationManager (se preferir)
  public String executeAlternative(LoginCommand command) {
    // Buscar usuário por email
    Email email = new Email(command.getEmail());
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

    // Verificar senha
    if (!passwordEncoder.matches(command.getSenha(), user.getSenha())) {
      throw new IllegalArgumentException("Credenciais inválidas");
    }

    // Verificar se usuário está ativo
    if (!user.getAtivo()) {
      throw new IllegalArgumentException("Usuário desativado");
    }

    // Gerar token JWT
    return jwtService.generateToken(user);
  }
}