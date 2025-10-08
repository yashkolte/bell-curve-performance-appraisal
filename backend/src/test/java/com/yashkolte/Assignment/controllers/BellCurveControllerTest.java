package com.yashkolte.Assignment.controllers;

import com.yashkolte.Assignment.services.BellCurveService;
import com.yashkolte.Assignment.model.Employee;
import com.yashkolte.Assignment.repositories.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BellCurveControllerTest {

    @Mock
    private BellCurveService bellCurveService;

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private BellCurveController bellCurveController;

    private List<Employee> employees;
    private Map<String, Double> actualPercentages;
    private Map<String, Double> deviations;
    private List<Employee> suggestions;

    @BeforeEach
    public void setup() {
        // Initialize test data
        employees = Arrays.asList(
                new Employee(1L, "John Doe", "A"),
                new Employee(2L, "Jane Smith", "B"),
                new Employee(3L, "Bob Johnson", "C")
        );

        actualPercentages = Map.of(
                "A", 33.33,
                "B", 33.33,
                "C", 33.33,
                "D", 0.0,
                "E", 0.0
        );

        deviations = Map.of(
                "A", 23.33,
                "B", 13.33,
                "C", -6.67,
                "D", -20.0,
                "E", -10.0
        );

        suggestions = List.of(
                new Employee(1L, "John Doe", "A")
        );
    }

    @Test
    public void testAnalyze() {
        // Setup mocks
        when(employeeRepo.findAll()).thenReturn(employees);
        when(bellCurveService.calculateActualPercentage(anyList(), anyList())).thenReturn(actualPercentages);
        when(bellCurveService.calculateDeviation(any(), anyList())).thenReturn(deviations);
        when(bellCurveService.suggestAdjustments(any(), anyList())).thenReturn(suggestions);

        // Execute the controller method
        ResponseEntity<Map<String, Object>> response = bellCurveController.analyze();

        // Verify the HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);

        // Check if all expected keys are present
        assertTrue(responseBody.containsKey("actualPercentage"));
        assertTrue(responseBody.containsKey("deviation"));
        assertTrue(responseBody.containsKey("adjustments"));

        // Verify that service methods were called
        verify(employeeRepo, times(1)).findAll();
        verify(bellCurveService, times(1)).calculateActualPercentage(anyList(), anyList());
        verify(bellCurveService, times(1)).calculateDeviation(any(), anyList());
        verify(bellCurveService, times(1)).suggestAdjustments(any(), anyList());
    }

    @Test
    public void testAddEmployee() {
        // Create test employee
        Employee employee = new Employee(1L, "John Doe", "A");
        Employee savedEmployee = new Employee(1L, "John Doe", "A");

        // Setup mock
        when(employeeRepo.save(any(Employee.class))).thenReturn(savedEmployee);

        // Execute the controller method
        ResponseEntity<Employee> response = bellCurveController.addEmployee(employee);

        // Verify the HTTP status
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verify the response body
        Employee responseEmployee = response.getBody();
        assertNotNull(responseEmployee);
        assertEquals(1L, responseEmployee.getId());
        assertEquals("John Doe", responseEmployee.getName());
        assertEquals("A", responseEmployee.getRatingCategory());

        // Verify that repository method was called
        verify(employeeRepo, times(1)).save(any(Employee.class));
    }

    @Test
    public void testAddEmployees() {
        // Create test employees
        List<Employee> employeesToAdd = Arrays.asList(
                new Employee(1L, "John Doe", "A"),
                new Employee(2L, "Jane Smith", "B")
        );

        // Setup mock
        when(employeeRepo.saveAll(anyList())).thenReturn(employeesToAdd);

        // Execute the controller method
        ResponseEntity<List<Employee>> response = bellCurveController.addEmployees(employeesToAdd);

        // Verify the HTTP status
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verify the response body
        List<Employee> responseEmployees = response.getBody();
        assertNotNull(responseEmployees);
        assertEquals(2, responseEmployees.size());

        // Verify that repository method was called
        verify(employeeRepo, times(1)).saveAll(anyList());
    }

    @Test
    public void testGetAllEmployees() {
        // Setup mock
        when(employeeRepo.findAll()).thenReturn(employees);

        // Execute the controller method
        ResponseEntity<List<Employee>> response = bellCurveController.getAllEmployees();

        // Verify the HTTP status
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the response body
        List<Employee> responseEmployees = response.getBody();
        assertNotNull(responseEmployees);
        assertEquals(3, responseEmployees.size());

        // Verify that repository method was called
        verify(employeeRepo, times(1)).findAll();
    }
}