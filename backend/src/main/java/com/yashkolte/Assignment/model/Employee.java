package com.yashkolte.Assignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@Data

// Lombok annotation to generate a no-arguments constructor
@NoArgsConstructor

// Lombok annotation to generate an all-arguments constructor
@AllArgsConstructor

// Mark this class as a JPA entity for persistence
@Entity
public class Employee {

    // Primary key for the Employee entity
    @Id
    private Long id;

    // Name of the employee
    private String name;

    // Rating category assigned to the employee (e.g., "A", "B", "C")
    private String ratingCategory;
}
