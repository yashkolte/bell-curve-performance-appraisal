package com.yashkolte.Assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@Data

// Lombok annotation to generate an all-arguments constructor
@AllArgsConstructor
public class Category {

    // Name of the category (e.g., "A", "B", "C")
    private String name;

    // Standard percentage for this category (e.g., 10.0 for 10%)
    private double standardPercentage;
}
