package br.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);
    ValidationHandler append(ValidationHandler anHandler);
    ValidationHandler validate(Validation anValidation);
    List<Error> getErros();
    default boolean hasError() {
        return getErros() != null && !getErros().isEmpty();
    }
    interface Validation{
        void validate();
    }
}
