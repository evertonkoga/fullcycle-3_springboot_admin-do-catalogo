package br.fullcycle.admin.catalogo.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var categoryCreated = Category.newCategory(
                expectedName, expectedDescription, expectedIsActive
        );

        Assertions.assertNotNull(categoryCreated);
        Assertions.assertNotNull(categoryCreated.getId());
        Assertions.assertEquals(expectedName, categoryCreated.getName());
        Assertions.assertEquals(expectedDescription, categoryCreated.getDescription());
        Assertions.assertEquals(expectedIsActive, categoryCreated.isActive());
        Assertions.assertNotNull(categoryCreated.getCreatedAt());
        Assertions.assertNotNull(categoryCreated.getUpdatedAt());
        Assertions.assertNull(categoryCreated.getDeletedAt());
    }
}
