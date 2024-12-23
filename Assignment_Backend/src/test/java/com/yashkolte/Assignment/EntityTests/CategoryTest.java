package com.yashkolte.Assignment.EntityTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.yashkolte.Assignment.model.Category;

public class CategoryTest {
	@Test
    void testCategoryCreation() {
        Category category = new Category("A", 10.0);

        assertEquals("A", category.getName());
        assertEquals(10.0, category.getStandardPercentage());
    }
}
