package br.fullcycle.admin.catalogo.application.category.update;

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

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;
    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategoryCreated.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(), expectedName, expectedDescription, expectedIsActive
        );

        when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategoryCreated)));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var categoryUpdated = useCase.execute(aCommand).get();

        Assertions.assertNotNull(categoryUpdated);
        Assertions.assertNotNull(categoryUpdated.id());

        Mockito.verify(categoryGateway, times(1)).findBy(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(aCategoryCreated.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategoryCreated.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.isNull(aUpdateCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategoryCreated.getId();
        final var expectedErrorMessage = "'name' should not be null or empty";
        final var expectedErrorCount = 1;

        when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategoryCreated)));

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);
        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErros().size());

        Mockito.verify(categoryGateway, times(0)).update(any());
    }
    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategoryCreated.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategoryCreated)));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategoryCreated.isActive());
        Assertions.assertNull(aCategoryCreated.getDeletedAt());

        final var output = useCase.execute(aCommand).get();

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());

        Mockito.verify(categoryGateway, times(1)).findBy(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(aCategoryCreated.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategoryCreated.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(aUpdateCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategoryCreated.getId();
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategoryCreated)));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        Assertions.assertTrue(aCategoryCreated.isActive());
        Assertions.assertNull(aCategoryCreated.getDeletedAt());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErros().size());

        Mockito.verify(categoryGateway, times(1)).findBy(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(aCategoryCreated.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategoryCreated.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(aUpdateCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = CategoryID.from("id_qualquer");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findBy(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var output = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount, output.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, output.firstError().message());

        Mockito.verify(categoryGateway, times(1)).findBy(eq(expectedId));
        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}
