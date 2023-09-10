package br.fullcycle.admin.catalogo.application.category.retrieve.get;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.category.CategoryID;
import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;
    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var aCategoryCreated = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategoryCreated.getId();

        Mockito.when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategoryCreated)));

        final var categoryFound = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, categoryFound.id());
        Assertions.assertEquals(expectedName, categoryFound.name());
        Assertions.assertEquals(expectedDescription, categoryFound.description());
        Assertions.assertEquals(expectedIsActive, categoryFound.isActive());
        Assertions.assertEquals(aCategoryCreated.getCreatedAt(), categoryFound.createAt());
        Assertions.assertEquals(aCategoryCreated.getUpdatedAt(), categoryFound.updateAt());
        Assertions.assertEquals(aCategoryCreated.getDeletedAt(), categoryFound.deletedAt());
    }
    @Test
    public void givenInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("id_qualquer");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());
        final var expectedErrorCount = 1;

        Mockito.when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var aException = Assertions.assertThrows(
                DomainException.class, () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aException.firstError().message());
    }
    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("id_qualquer");
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(categoryGateway.findBy(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aException = Assertions.assertThrows(
                IllegalStateException.class, () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, aException.getMessage());
    }
}
