package br.fullcycle.admin.catalogo.application;

import br.fullcycle.admin.catalogo.domain.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}