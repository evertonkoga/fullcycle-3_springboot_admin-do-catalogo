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
        return (getErros() == null || getErros().isEmpty()) ? null : getErros().get(0);
    }
    interface Validation{
        void validate();
    }
}
