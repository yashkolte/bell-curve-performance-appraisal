package com.yashkolte.Assignment.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashkolte.Assignment.controllers.BellCurveController;
import com.yashkolte.Assignment.model.Category;
import com.yashkolte.Assignment.model.Employee;
import com.yashkolte.Assignment.services.BellCurveService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BellCurveController.class)
class BellCurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BellCurveService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAnalyze() throws Exception {
        // Arrange
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Alice", "A"),
                new Employee(2L, "Bob", "B"),
                new Employee(3L, "Charlie", "A"),
                new Employee(4L, "Daisy", "C")
        );

        List<Category> categories = List.of(
                new Category("A", 10.0),
                new Category("B", 20.0),
                new Category("C", 40.0),
                new Category("D", 20.0),
                new Category("E", 10.0)
        );

        Map<String, Double> actualPercentage = Map.of(
                "A", 50.0,
                "B", 25.0,
                "C", 25.0
        );

        Map<String, Double> deviation = Map.of(
                "A", 40.0,
                "B", 5.0,
                "C", -15.0
        );

        List<Employee> adjustments = List.of(
                new Employee(1L, "Alice", "A"),
                new Employee(3L, "Charlie", "A")
        );

        when(service.calculateActualPercentage(employees, categories)).thenReturn(actualPercentage);
        when(service.calculateDeviation(actualPercentage, categories)).thenReturn(deviation);
        when(service.suggestAdjustments(deviation, employees)).thenReturn(adjustments);

        // Act & Assert
        mockMvc.perform(post("/api/bell-curve/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employees)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actualPercentage.A", is(50.0)))
                .andExpect(jsonPath("$.actualPercentage.B", is(25.0)))
                .andExpect(jsonPath("$.actualPercentage.C", is(25.0)))
                .andExpect(jsonPath("$.deviation.A", is(40.0)))
                .andExpect(jsonPath("$.deviation.B", is(5.0)))
                .andExpect(jsonPath("$.deviation.C", is(-15.0)))
                .andExpect(jsonPath("$.adjustments", hasSize(2)))
                .andExpect(jsonPath("$.adjustments[0].name", is("Alice")))
                .andExpect(jsonPath("$.adjustments[1].name", is("Charlie")));
    }
}
