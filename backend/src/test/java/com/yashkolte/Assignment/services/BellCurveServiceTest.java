package com.yashkolte.Assignment.services;

import com.yashkolte.Assignment.model.Category;
import com.yashkolte.Assignment.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BellCurveServiceTest {

    @InjectMocks
    private BellCurveService bellCurveService;

    private List<Employee> employees;
    private List<Category> categories;

    @BeforeEach
    public void setup() {
        // Initialize test data
//        employees = Arrays.asList(
//                new Employee(1L, "John Doe", "A"),
//                new Employee(2L, "Jane Smith", "B"),
//                new Employee(3L, "Bob Johnson", "B"),
//                new Employee(4L, "Alice Brown", "C"),
//                new Employee(5L, "Charlie Davis", "C"),
//                new Employee(6L, "Eva Wilson", "C"),
//                new Employee(7L, "Frank Miller", "C"),
//                new Employee(8L, "Grace Taylor", "D"),
//                new Employee(9L, "Henry Martinez", "D"),
//                new Employee(10L, "Ivy Robinson", "E")
//        );

        categories = Arrays.asList(
                new Category("A", 10.0),
                new Category("B", 20.0),
                new Category("C", 40.0),
                new Category("D", 20.0),
                new Category("E", 10.0)
        );
    }

    @Test
    public void testCalculateActualPercentage() {
        // Execute the method
        Map<String, Double> result = bellCurveService.calculateActualPercentage(employees, categories);

        // Assert the results
        assertEquals(10.0, result.get("A"), 0.01);
        assertEquals(20.0, result.get("B"), 0.01);
        assertEquals(40.0, result.get("C"), 0.01);
        assertEquals(20.0, result.get("D"), 0.01);
        assertEquals(10.0, result.get("E"), 0.01);
    }

    @Test
    public void testCalculateActualPercentageWithEmptyList() {
        // Test with empty employee list
        Map<String, Double> result = bellCurveService.calculateActualPercentage(
                List.of(), categories);

        // All categories should have 0%
        for (Category category : categories) {
            assertEquals(0.0, result.get(category.getName()), 0.01);
        }
    }

    @Test
    public void testCalculateDeviation() {
        // Setup actual percentages
        Map<String, Double> actual = Map.of(
                "A", 15.0,
                "B", 25.0,
                "C", 30.0,
                "D", 20.0,
                "E", 10.0
        );

        // Execute the method
        Map<String, Double> deviation = bellCurveService.calculateDeviation(actual, categories);

        // Assert the results
        assertEquals(5.0, deviation.get("A"), 0.01);
        assertEquals(5.0, deviation.get("B"), 0.01);
        assertEquals(-10.0, deviation.get("C"), 0.01);
        assertEquals(0.0, deviation.get("D"), 0.01);
        assertEquals(0.0, deviation.get("E"), 0.01);
    }

    @Test
    public void testCalculateDeviationWithMissingCategory() {
        // Setup actual percentages with a missing category
        Map<String, Double> actual = Map.of(
                "A", 15.0,
                "B", 25.0,
                "C", 30.0,
                "D", 30.0
                // E is missing
        );

        // Execute the method
        Map<String, Double> deviation = bellCurveService.calculateDeviation(actual, categories);

        // Assert the results including the missing category
        assertEquals(-10.0, deviation.get("E"), 0.01);
    }

    @Test
    public void testSuggestAdjustments() {
        // Setup deviation data where A and B are overrepresented
        Map<String, Double> deviation = Map.of(
                "A", 5.0,   // 5% too many in category A
                "B", 3.0,   // 3% too many in category B
                "C", -5.0,  // 5% too few in category C
                "D", -2.0,  // 2% too few in category D
                "E", -1.0   // 1% too few in category E
        );

        // Execute the method
        List<Employee> adjustments = bellCurveService.suggestAdjustments(deviation, employees);

        // We should have suggestions for categories A and B only
        assertTrue(adjustments.stream().anyMatch(e -> e.getRatingCategory().equals("A")));
        assertTrue(adjustments.stream().anyMatch(e -> e.getRatingCategory().equals("B")));
        assertFalse(adjustments.stream().anyMatch(e -> e.getRatingCategory().equals("C")));
        assertFalse(adjustments.stream().anyMatch(e -> e.getRatingCategory().equals("D")));
        assertFalse(adjustments.stream().anyMatch(e -> e.getRatingCategory().equals("E")));

        // For this sample size, we expect 1 adjustment from category A and 1 from category B
        assertEquals(2, adjustments.size());
    }

    @Test
    public void testSuggestAdjustmentsWithNoOverrepresentedCategories() {
        // Setup deviation data where no categories are overrepresented
        Map<String, Double> deviation = Map.of(
                "A", 0.0,
                "B", -2.0,
                "C", -3.0,
                "D", 0.0,
                "E", -1.0
        );

        // Execute the method
        List<Employee> adjustments = bellCurveService.suggestAdjustments(deviation, employees);

        // We should have no suggestions
        assertTrue(adjustments.isEmpty());
    }
}