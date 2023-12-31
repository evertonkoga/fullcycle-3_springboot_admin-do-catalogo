package br.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);
    ValidationHandler append(ValidationHandler anHandler);
    ValidationHandler validate(Validation aValidation);
    List<Error> getErros();
    default boolean hasError() {
        return getErros() != null && !getErros().isEmpty();
    }
    default Error firstError() {
        return hasError() ? getErros().get(0) : null;
    }
    interface Validation{
        void validate();
    }
}
