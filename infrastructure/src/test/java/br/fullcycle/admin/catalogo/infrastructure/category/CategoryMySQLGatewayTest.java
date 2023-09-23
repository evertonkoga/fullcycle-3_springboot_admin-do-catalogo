package br.fullcycle.admin.catalogo.infrastructure.category;

import br.fullcycle.admin.catalogo.domain.category.Category;
import br.fullcycle.admin.catalogo.domain.category.CategoryID;
import br.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import br.fullcycle.admin.catalogo.infrastructure.MySQLGatewayTest;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        final var createdCategory = categoryGateway.create(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), createdCategory.getId());
        Assertions.assertEquals(expectedName, createdCategory.getName());
        Assertions.assertEquals(expectedDescription, createdCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, createdCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), createdCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), createdCategory.getDeletedAt());
        Assertions.assertNull(createdCategory.getDeletedAt());

        final var createdEntity = categoryRepository.findById(createdCategory.getId().getValue()).get();
        Assertions.assertEquals(aCategory.getId().getValue(), createdEntity.getId());
        Assertions.assertEquals(expectedName, createdEntity.getName());
        Assertions.assertEquals(expectedDescription, createdEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), createdEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), createdEntity.getDeletedAt());
        Assertions.assertNull(createdEntity.getDeletedAt());
    }
    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedInvalidName = "Series";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedInvalidName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var createdInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals(expectedInvalidName, createdInvalidEntity.getName());
        Assertions.assertEquals(expectedDescription, createdInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdInvalidEntity.isActive());

        final var updatedCategory = Category.clone(aCategory).update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryGateway.update(updatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var createdEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals(aCategory.getId().getValue(), createdEntity.getId());
        Assertions.assertEquals(expectedName, createdEntity.getName());
        Assertions.assertEquals(expectedDescription, createdEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, createdEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), createdEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(createdEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), createdEntity.getDeletedAt());
        Assertions.assertNull(createdEntity.getDeletedAt());
    }
    @Test
    public void givenValidCategoryID_whenCallsDeleteById_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("Filmes", null, true);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        categoryGateway.deleteBy(aCategory.getId());
        Assertions.assertEquals(0, categoryRepository.count());
    }
    @Test
    public void givenInvalidCategoryID_whenCallsDeleteById_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryGateway.deleteBy(CategoryID.from("invalidID"));
        Assertions.assertEquals(0, categoryRepository.count());
    }
    @Test
    public void givenValidCategoryID_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var category = categoryGateway.findBy(CategoryID.from(aCategory.getId().getValue())).get();
        Assertions.assertEquals(aCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), category.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), category.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), category.getDeletedAt());
        Assertions.assertNull(category.getDeletedAt());
    }
    @Test
    public void givenInvalidCategoryID_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var category = categoryGateway.findBy(CategoryID.from("emptyID"));

        Assertions.assertTrue(category.isEmpty());
    }
    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(documentarios.getId(), resultList.items().get(0).getId());
    }
    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(0, resultList.items().size());
    }
    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(documentarios.getId(), resultList.items().get(0).getId());

        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(filmes.getId(), resultList.items().get(0).getId());

        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(series.getId(), resultList.items().get(0).getId());
    }
    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(documentarios.getId(), resultList.items().get(0).getId());
    }
    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "Uma categoria top 10", true);
        final var documentarios = Category.newCategory("Documentarios", "A categoria menos assistídas", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "description", "asc");
        final var resultList = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, resultList.currentPage());
        Assertions.assertEquals(expectedPerPage, resultList.perPage());
        Assertions.assertEquals(expectedTotal, resultList.total());
        Assertions.assertEquals(expectedPerPage, resultList.items().size());
        Assertions.assertEquals(filmes.getId(), resultList.items().get(0).getId());
    }
}
