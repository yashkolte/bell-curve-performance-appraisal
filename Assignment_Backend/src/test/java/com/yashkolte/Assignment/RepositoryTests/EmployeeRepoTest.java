package com.yashkolte.Assignment.RepositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import com.yashkolte.Assignment.model.Employee;
import com.yashkolte.Assignment.repositories.EmployeeRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setUp() {
        // Clear the database before each test (optional)
        employeeRepo.deleteAll();
    }

    @Test
    @Rollback(false)
    void testFindAll() {
        // Arrange
        employeeRepo.save(new Employee(5001L, "Harry 1", "A"));
        employeeRepo.save(new Employee(5002L, "Harry 3", "B"));

        // Act
        List<Employee> employees = employeeRepo.findAll();

        // Assert
        assertEquals(2, employees.size());
    }
}
