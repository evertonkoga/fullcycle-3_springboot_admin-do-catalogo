package br.fullcycle.admin.catalogo.infrastructure.category;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        final var createdCategory = categoryGateway.create(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), createdCategory.getId());
        Assertions.assertEquals(expectedName, createdCategory.getName());
        Assertions.assertEquals(expectedDescription, createdCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, createdCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), createdCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), createdCategory.getDeletedAt());
        Assertions.assertNull(createdCategory.getDeletedAt());

        final var createdEntity = categoryRepository.findById(createdCategory.getId().getValue()).get();
        Assertions.assertEquals(aCategory.getId().getValue(), createdEntity.getId());
        Assertions.assertEquals(expectedName, createdEntity.getName());
        Assertions.assertEquals(expectedDescription, createdEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), createdEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), createdEntity.getDeletedAt());
        Assertions.assertNull(createdEntity.getDeletedAt());
    }
}
