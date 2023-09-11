package br.fullcycle.admin.catalogo.infrastructure;

import br.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("oi");
        SpringApplication.run(WebServerConfig.class, args);
    }
}