package br.com.selecao.locadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
// Classe principal para inicializacao da aplicacao Spring Boot
public class LeilaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeilaoApplication.class, args);
    }

    // Define o bean de criptografia BCrypt para as senhas de usuarios
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}