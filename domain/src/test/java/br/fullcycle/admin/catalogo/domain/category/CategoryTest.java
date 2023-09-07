package br.fullcycle.admin.catalogo.domain.category;

import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import br.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class CategoryTest {
    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var categoryCreated = Category.newCategory(
                expectedName, expectedDescription, expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> categoryCreated.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(categoryCreated);
        Assertions.assertNotNull(categoryCreated.getId());
        Assertions.assertEquals(expectedName, categoryCreated.getName());
        Assertions.assertEquals(expectedDescription, categoryCreated.getDescription());
        Assertions.assertEquals(expectedIsActive, categoryCreated.isActive());
        Assertions.assertNotNull(categoryCreated.getCreatedAt());
        Assertions.assertNotNull(categoryCreated.getUpdatedAt());
        Assertions.assertNull(categoryCreated.getDeletedAt());
    }
    @ParameterizedTest
    @NullAndEmptySource
    public void givenInvalidNullOrEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError(String expectedName) {
        final var expectedErrorMessage = "'name' should not be null or empty";
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
    @ParameterizedTest
    @ValueSource(ints = {2, 256})
    public void givenInvalidNameLengthLessThan3OrMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError(int characters) {
        final var expectedName = RandomStringUtils.randomAlphabetic(characters).concat(" ");
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
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
    @Test
    public void givenValidEmptyDescription_whenCallNewCategoryAndValidate_thenInstantiateCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var categoryCreated = Category.newCategory(
                expectedName, expectedDescription, expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> categoryCreated.validate(new ThrowsValidationHandler()));
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
