package br.fullcycle.admin.catalogo.application.category.create;

import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    @InjectMocks
    private DefaultCreateCategoryUseCase  useCase;
    @Mock
    private CategoryGateway categoryGateway;
    @Test
    public void givenValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var categoryCreated = useCase.execute(aCommand);

        Assertions.assertNotNull(categoryCreated);
        Assertions.assertNotNull(categoryCreated.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
            Objects.equals(expectedName, aCategory.getName())
                && Objects.equals(expectedDescription, aCategory.getDescription())
                && Objects.equals(expectedIsActive, aCategory.isActive())
                && Objects.nonNull(aCategory.getId())
                && Objects.nonNull(aCategory.getCreatedAt())
                && Objects.nonNull(aCategory.getUpdatedAt())
                && Objects.isNull(aCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null or empty";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class, () ->  useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, times(0)).create(any());
    }
    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var categoryCreated = useCase.execute(aCommand);

        Assertions.assertNotNull(categoryCreated);
        Assertions.assertNotNull(categoryCreated.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class, () ->  useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }
}
