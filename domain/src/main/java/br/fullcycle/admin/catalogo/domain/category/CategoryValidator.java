package br.fullcycle.admin.catalogo.domain.category;

import br.fullcycle.admin.catalogo.domain.validation.Error;
import br.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import br.fullcycle.admin.catalogo.domain.validation.Validator;
import org.apache.commons.lang3.StringUtils;

public class CategoryValidator extends Validator {
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;
    public CategoryValidator(final Category aCategory, final ValidationHandler handler) {
        super(handler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if(StringUtils.isEmpty(name)) {
            this.validationHandler().append(new Error("'name' should not be null or empty"));
            return;
        }

        final var length = name.trim().length();
        if(length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
