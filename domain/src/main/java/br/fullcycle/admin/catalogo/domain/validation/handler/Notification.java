package br.fullcycle.admin.catalogo.domain.validation.handler;

import br.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import br.fullcycle.admin.catalogo.domain.validation.Error;
import br.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {
    private final List<Error> errors;

    public Notification(List<Error> anErrors) {
        this.errors = anErrors;
    }
    public static Notification create() {
        return new Notification(new ArrayList<>());
    }
    public static Notification create(Error anError) {
        return create().append(anError);
    }
    public static Notification create(Throwable aThrowable) {
        return create(new Error(aThrowable.getMessage()));
    }

    @Override
    public Notification append(final Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErros());
        return this;
    }

    @Override
    public Notification validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final  DomainException dex) {
            this.errors.addAll(dex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErros() {
        return this.errors;
    }
}
