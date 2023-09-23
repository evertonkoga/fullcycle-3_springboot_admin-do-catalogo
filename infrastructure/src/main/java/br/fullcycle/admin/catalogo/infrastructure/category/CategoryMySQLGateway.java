package br.fullcycle.admin.catalogo.infrastructure.category;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import br.fullcycle.admin.catalogo.domain.category.CategoryID;
import br.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import br.fullcycle.admin.catalogo.domain.pagination.Pagination;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import br.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(filter -> !filter.isBlank())
                .map(filter -> {
                    final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", filter);
                    final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", filter);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
