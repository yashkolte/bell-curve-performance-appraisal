package com.yashkolte.Assignment.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yashkolte.Assignment.model.Category;
import com.yashkolte.Assignment.model.Employee;
import com.yashkolte.Assignment.repositories.EmployeeRepo;
import com.yashkolte.Assignment.services.BellCurveService;

// Mark this class as a REST controller to handle API requests
@RestController

// Allow cross-origin requests from a specific frontend application (React running on localhost:3000)
@CrossOrigin(origins = "http://localhost:3000")

// Define the base path for all endpoints in this controller
@RequestMapping("/api/bell-curve")
public class BellCurveController {

    // Inject BellCurveService to use its methods for business logic
    @Autowired
    private BellCurveService service;

    // Inject EmployeeRepo to interact with the database for employee-related operations
    @Autowired
    private EmployeeRepo employeeRepo;

    // Endpoint to analyze employee performance data and calculate the bell curve
    @GetMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze() {
        // Define performance categories and their target percentages
        List<Category> categories = List.of(
                new Category("A", 10.0), // Top performers (10%)
                new Category("B", 20.0), // High performers (20%)
                new Category("C", 40.0), // Average performers (40%)
                new Category("D", 20.0), // Below average performers (20%)
                new Category("E", 10.0)  // Low performers (10%)
        );

        // Fetch all employees from the database
        List<Employee> employees = employeeRepo.findAll();

        // Calculate the actual performance percentage for each category
        Map<String, Double> actual = service.calculateActualPercentage(employees, categories);

        // Calculate the deviation of actual percentages from target percentages
        Map<String, Double> deviation = service.calculateDeviation(actual, categories);

        // Generate suggestions for performance adjustments based on deviations
        List<Employee> suggestions = service.suggestAdjustments(deviation, employees);

        // Prepare the response as a map containing all the calculated data
        Map<String, Object> response = new HashMap<>();
        response.put("actualPercentage", actual); // Actual performance percentages
        response.put("deviation", deviation);     // Deviations from target percentages
        response.put("adjustments", suggestions); // Suggested adjustments for employees

        // Return the response as JSON with HTTP status 200 (OK)
        return ResponseEntity.ok(response);
    }

    // Endpoint to add a single employee to the database
    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        // Save the employee object to the database
        Employee savedEmployee = employeeRepo.save(employee);

        // Return the saved employee object with HTTP status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    // Endpoint to add multiple employees to the database at once
    @PostMapping("/add-all")
    public ResponseEntity<List<Employee>> addEmployees(@RequestBody List<Employee> employees) {
        // Save all employee objects to the database
        List<Employee> savedEmployees = employeeRepo.saveAll(employees);

        // Return the saved list of employees with HTTP status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployees);
    }

    // Endpoint to retrieve all employees from the database
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        // Fetch all employees from the database
        List<Employee> employees = employeeRepo.findAll();

        // Return the list of employees with HTTP status 200 (OK)
        return ResponseEntity.ok(employees);
    }
}