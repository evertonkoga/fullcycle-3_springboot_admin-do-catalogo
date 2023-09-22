package br.fullcycle.admin.catalogo.infrastructure.category;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
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
    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedInvalidName = "Series";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedInvalidName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var createdInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals(expectedInvalidName, createdInvalidEntity.getName());
        Assertions.assertEquals(expectedDescription, createdInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdInvalidEntity.isActive());

        final var updatedCategory = Category.clone(aCategory).update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryGateway.update(updatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var createdEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals(aCategory.getId().getValue(), createdEntity.getId());
        Assertions.assertEquals(expectedName, createdEntity.getName());
        Assertions.assertEquals(expectedDescription, createdEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(createdEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), createdEntity.getDeletedAt());
        Assertions.assertNull(createdEntity.getDeletedAt());
    }
}
