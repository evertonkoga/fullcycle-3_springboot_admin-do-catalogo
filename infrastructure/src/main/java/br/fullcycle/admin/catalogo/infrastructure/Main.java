package br.fullcycle.admin.catalogo.infrastructure;

import br.fullcycle.admin.catalogo.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println(new UseCase().execute());
    }
}