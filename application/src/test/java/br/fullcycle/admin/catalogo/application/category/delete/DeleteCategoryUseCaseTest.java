package br.fullcycle.admin.catalogo.application.category.delete;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;
    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);
        final var expectedId = aCategoryCreated.getId();

        Mockito.doNothing().when(categoryGateway).deleteBy(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteBy(expectedId);
    }
    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("id_qualquer");

        Mockito.doNothing().when(categoryGateway).deleteBy(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteBy(expectedId);
    }
    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategoryCreated = Category.newCategory("Film", " ", true);
        final var expectedId = aCategoryCreated.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteBy(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteBy(expectedId);
    }
}
