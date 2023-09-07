package br.fullcycle.admin.catalogo.domain.validation.handler;

import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import br.fullcycle.admin.catalogo.domain.validation.Error;
import br.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErros());
    }

    @Override
    public ValidationHandler validate(final Validation anValidation) {
        try {
            anValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErros() {
        return List.of();
    }
}
