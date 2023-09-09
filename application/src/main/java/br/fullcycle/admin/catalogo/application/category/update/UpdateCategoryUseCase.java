package br.fullcycle.admin.catalogo.application.category.update;

import br.fullcycle.admin.catalogo.application.UseCase;
import br.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
