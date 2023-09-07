package br.fullcycle.admin.catalogo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void testCreateCategory() {
        Assertions.assertNotNull(new Category());
    }
}
