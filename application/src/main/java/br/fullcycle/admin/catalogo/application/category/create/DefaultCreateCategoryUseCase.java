package br.fullcycle.admin.catalogo.application.category.create;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand aCommand) {
        final var notification = Notification.create();
        final var aCategory = Category.newCategory(
                aCommand.name(),
                aCommand.description(),
                aCommand.isActive()
        );
        aCategory.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
        return API.Try(() -> this.categoryGateway.create(aCategory))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
        /*
        Os código abaixo são similares ao acima:

        #1
        return API.Try(() -> this.categoryGateway.create(aCategory))
                .toEither()
                .map(CreateCategoryOutput::from)
                .mapLeft(Notification::create);

        #2
        try {
            return API.Right(CreateCategoryOutput.from(this.categoryGateway.create(aCategory)));
        } catch (Throwable t) {
            return API.Left(Notification.create(t));
        }*/
    }
}
