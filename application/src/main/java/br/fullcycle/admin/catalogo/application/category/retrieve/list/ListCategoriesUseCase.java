package br.fullcycle.admin.catalogo.application.category.retrieve.list;

import br.fullcycle.admin.catalogo.application.UseCase;
import br.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import br.fullcycle.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
