package com.yashkolte.Assignment.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yashkolte.Assignment.model.Category;
import com.yashkolte.Assignment.model.Employee;
import com.yashkolte.Assignment.services.BellCurveService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class BellCurveServiceTest {

    private BellCurveService bellCurveService;

    @BeforeEach
    void setUp() {
        bellCurveService = new BellCurveService();
    }

    @Test
    void testCalculateActualPercentage() {
        // Arrange
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Alice", "A"),
                new Employee(2L, "Bob", "B"),
                new Employee(3L, "Charlie", "A"),
                new Employee(4L, "Daisy", "C")
        );

        List<Category> categories = Arrays.asList(
                new Category("A", 40.0),
                new Category("B", 30.0),
                new Category("C", 30.0)
        );

        // Act
        Map<String, Double> actualPercentage = bellCurveService.calculateActualPercentage(employees, categories);

        // Assert
        assertEquals(50.0, actualPercentage.get("A")); // 2 out of 4 employees are in "A"
        assertEquals(25.0, actualPercentage.get("B")); // 1 out of 4 employees are in "B"
        assertEquals(25.0, actualPercentage.get("C")); // 1 out of 4 employees are in "C"
    }

    @Test
    void testCalculateDeviation() {
        // Arrange
        Map<String, Double> actual = Map.of(
                "A", 50.0,
                "B", 25.0,
                "C", 25.0
        );

        List<Category> categories = Arrays.asList(
                new Category("A", 40.0),
                new Category("B", 30.0),
                new Category("C", 30.0)
        );

        // Act
        Map<String, Double> deviation = bellCurveService.calculateDeviation(actual, categories);

        // Assert
        assertEquals(10.0, deviation.get("A")); // Actual is 50%, standard is 40%
        assertEquals(-5.0, deviation.get("B")); // Actual is 25%, standard is 30%
        assertEquals(-5.0, deviation.get("C")); // Actual is 25%, standard is 30%
    }

    @Test
    void testSuggestAdjustments() {
        // Arrange
        Map<String, Double> deviation = Map.of(
                "A", 10.0,  // Overrepresented
                "B", -5.0,  // Underrepresented
                "C", -5.0   // Underrepresented
        );

        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Alice", "A"),
                new Employee(2L, "Bob", "A"),
                new Employee(3L, "Charlie", "B"),
                new Employee(4L, "Daisy", "C")
        );

        // Act
        List<Employee> adjustments = bellCurveService.suggestAdjustments(deviation, employees);

        // Assert
        assertEquals(2, adjustments.size()); // Two employees from category "A" need adjustment
        assertTrue(adjustments.stream().allMatch(emp -> emp.getRatingCategory().equals("A")));
    }
}
