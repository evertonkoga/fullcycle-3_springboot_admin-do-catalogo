package br.fullcycle.admin.catalogo.domain.category;

import br.fullcycle.admin.catalogo.domain.AggregateRoot;
import br.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category clone(final Category aCategory) {
        return new Category(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }
    public Category activate() {
        this.active = true;
        this.deletedAt = null;
        this.updatedAt = Instant.now();

        return this;
    }
    public Category deactivate() {
        this.active = false;
        if(getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.updatedAt = Instant.now();

        return this;
    }
    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if (isActive) activate(); else deactivate();

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();

        return this;
    }
    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }
    public static Category with(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreatedAt,
                aUpdatedAt,
                aDeletedAt
        );
    }
    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
