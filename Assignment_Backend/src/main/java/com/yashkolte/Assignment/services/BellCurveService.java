package com.yashkolte.Assignment.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yashkolte.Assignment.model.Category;
import com.yashkolte.Assignment.model.Employee;

// Mark this class as a service component for business logic
@Service
public class BellCurveService {

    /**
     * Calculate the actual percentage distribution of employees across categories.
     *
     * @param employees List of employees.
     * @param categories List of performance categories with standard percentages.
     * @return A map of category names to actual percentage distribution.
     */
    public Map<String, Double> calculateActualPercentage(List<Employee> employees, List<Category> categories) {
        // Group employees by their rating category and count the occurrences
        Map<String, Long> countByCategory = employees.stream()
                .collect(Collectors.groupingBy(Employee::getRatingCategory, Collectors.counting()));

        // Total number of employees
        int totalEmployees = employees.size();

        // Map to store actual percentages for each category
        Map<String, Double> actualPercentage = new HashMap<>();

        // Calculate the percentage of employees in each category
        for (Category category : categories) {
            long count = countByCategory.getOrDefault(category.getName(), 0L); // Get count or default to 0
            double percentage = (double) count / totalEmployees * 100;        // Calculate percentage
            actualPercentage.put(category.getName(), percentage);            // Store in map
        }

        return actualPercentage;
    }

    /**
     * Calculate the deviation of actual percentages from the standard percentages.
     *
     * @param actual Map of actual percentages.
     * @param categories List of performance categories with standard percentages.
     * @return A map of category names to deviation values.
     */
    public Map<String, Double> calculateDeviation(Map<String, Double> actual, List<Category> categories) {
        // Map to store deviation values for each category
        Map<String, Double> deviation = new HashMap<>();

        // Calculate deviation for each category
        for (Category category : categories) {
            double standard = category.getStandardPercentage();               // Standard percentage
            double actualPercentage = actual.getOrDefault(category.getName(), 0.0); // Actual percentage
            deviation.put(category.getName(), actualPercentage - standard);  // Calculate and store deviation
        }

        return deviation;
    }

    /**
     * Suggest adjustments to align employee distribution with the bell curve.
     *
     * @param deviation Map of deviations for each category.
     * @param employees List of employees.
     * @return A list of employees who might need adjustments.
     */
    public List<Employee> suggestAdjustments(Map<String, Double> deviation, List<Employee> employees) {
        // List to store employees needing adjustment
        List<Employee> adjustments = new ArrayList<>();

        // Identify overrepresented categories and suggest adjustments
        for (Map.Entry<String, Double> entry : deviation.entrySet()) {
            if (entry.getValue() > 0) { // Overrepresented category
                String category = entry.getKey(); // Category name
                adjustments.addAll(employees.stream()
                        .filter(emp -> emp.getRatingCategory().equals(category)) // Filter employees in this category
                        .limit((long) Math.ceil(entry.getValue()))              // Limit adjustments to deviation count
                        .collect(Collectors.toList()));                        // Collect adjusted employees
            }
        }

        return adjustments;
    }
}
