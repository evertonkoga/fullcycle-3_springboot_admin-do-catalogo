package br.fullcycle.admin.catalogo.infrastructure;

import br.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import br.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import br.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import br.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("oi");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    ApplicationRunner runner(
            CreateCategoryUseCase createCategoryUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            GetCategoryByIdUseCase getCategoryByIdUseCase,
            ListCategoriesUseCase listCategoriesUseCase
    ) {
        return args -> {};
    }
}