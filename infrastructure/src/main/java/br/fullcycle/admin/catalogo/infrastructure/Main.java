package br.fullcycle.admin.catalogo.infrastructure;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import br.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("oi");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    public ApplicationRunner runner(CategoryRepository repository) {
        return args -> {
            Category filmes = Category.newCategory("Filems", null, true);
            repository.saveAndFlush(CategoryJpaEntity.from(filmes));

            List<CategoryJpaEntity> all = repository.findAll();
            all.forEach(category -> {
                String mensagem = String.format("ID: %s, Name: %s", category.getId(), category.getName());
                System.out.println(mensagem);
            });

            repository.deleteAll();
        };
    }
}