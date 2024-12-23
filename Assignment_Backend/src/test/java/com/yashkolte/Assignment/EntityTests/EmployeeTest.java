package com.yashkolte.Assignment.EntityTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.yashkolte.Assignment.model.Employee;

public class EmployeeTest {
	@Test
    void testEmployeeCreation() {
        Employee employee = new Employee(5001L, "Harry 1", "A");

        assertEquals(5001, employee.getId());
        assertEquals("Harry 1", employee.getName());
        assertEquals("A", employee.getRatingCategory());
    }
}	
