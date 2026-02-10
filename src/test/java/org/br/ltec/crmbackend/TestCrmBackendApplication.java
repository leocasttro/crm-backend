package org.br.ltec.crmbackend;

import org.springframework.boot.SpringApplication;

public class TestCrmBackendApplication {

  public static void main(String[] args) {
    SpringApplication.from(CrmBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
