package br.fullcycle.admin.catalogo.infrastructure.category;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.category.CategoryID;
import br.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import br.fullcycle.admin.catalogo.domain.pagination.Pagination;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Category update(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteBy(CategoryID anId) {
        final var aCategoriID = anId.getValue();
        if (this.repository.existsById(aCategoriID))
            this.repository.deleteById(anId.getValue());
    }

    @Override
    public Optional<Category> findBy(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    private Category save(Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
