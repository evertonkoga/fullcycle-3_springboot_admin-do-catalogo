package br.fullcycle.admin.catalogo.domain.category;

import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import br.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
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
    @Test
    public void givenValidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var categoryCreated = Category.newCategory(
                expectedName, expectedDescription, expectedIsActive
        );

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> categoryCreated.validate(new ThrowsValidationHandler())
        );
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
