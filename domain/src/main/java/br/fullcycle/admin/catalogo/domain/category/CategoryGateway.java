package br.fullcycle.admin.catalogo.domain.category;

import br.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);
    Category update(Category aCategory);
    void deleteBy(CategoryID anId);
    Optional<Category> findBy(CategoryID anId);
    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
