package org.br.ltec.crmbackend.crm.auth.infra.security;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.domain.port.UserRepository;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var email = new Email(username);

    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

    return new SecurityUser(user);
  }
}
