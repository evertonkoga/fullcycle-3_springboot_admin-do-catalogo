package br.fullcycle.admin.catalogo.application.category.create;

import br.fullcycle.admin.catalogo.application.UseCase;
import br.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
