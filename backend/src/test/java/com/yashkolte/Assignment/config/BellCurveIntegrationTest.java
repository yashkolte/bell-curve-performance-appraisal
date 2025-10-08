//package com.yashkolte.Assignment.config;
//
//import com.yashkolte.Assignment.controllers.BellCurveController;
//import com.yashkolte.Assignment.model.Employee;
//import com.yashkolte.Assignment.repositories.EmployeeRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.cache.CacheManager;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@TestPropertySource(properties = {
//        "spring.cache.type=simple", // Use simple cache for testing instead of Redis
//        "spring.data.redis.host=localhost",
//        "spring.data.redis.port=6379"
//})
//public class BellCurveIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @MockitoSpyBean
//    private BellCurveController bellCurveController;
//
//    @Autowired
//    private EmployeeRepo employeeRepo;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @BeforeEach
//    public void setup() {
//        // Clear the database before each test
//        employeeRepo.deleteAll();
//
//        // Clear all caches
//        cacheManager.getCacheNames().forEach(cacheName -> {
//            cacheManager.getCache(cacheName).clear();
//        });
//
//        // Add test data
//        List<Employee> employees = Arrays.asList(
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
//        employeeRepo.saveAll(employees);
//    }
//
//    @Test
//    public void testGetAllEmployees() {
//        // First call - should hit the database
//        ResponseEntity<List> response1 = restTemplate.getForEntity(
//                "http://localhost:" + port + "/api/bell-curve/employees", List.class);
//
//        // Second call - should be cached
//        ResponseEntity<List> response2 = restTemplate.getForEntity(
//                "http://localhost:" + port + "/api/bell-curve/employees", List.class);
//
//        // Verify successful responses
//        assertEquals(HttpStatus.OK, response1.getStatusCode());
//        assertEquals(HttpStatus.OK, response2.getStatusCode());
//
//        // Verify method was called twice (once for actual execution, once from cache)
//        verify(bellCurveController, times(2)).getAllEmployees();
//    }
//
//    @Test
//    public void testAnalyzeEndpoint() {
//        // First call - should hit the database
//        ResponseEntity<Map> response1 = restTemplate.getForEntity(
//                "http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//
//        // Second call - should be cached
//        ResponseEntity<Map> response2 = restTemplate.getForEntity(
//                "http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//
//        // Verify successful responses
//        assertEquals(HttpStatus.OK, response1.getStatusCode());
//        assertEquals(HttpStatus.OK, response2.getStatusCode());
//
//        // Verify method was called twice (once for actual execution, once from cache)
//        verify(bellCurveController, times(2)).analyze();
//
//        // Verify response contents
//        assertNotNull(response1.getBody());
//        assertNotNull(response1.getBody().get("actualPercentage"));
//        assertNotNull(response1.getBody().get("deviation"));
//        assertNotNull(response1.getBody().get("adjustments"));
//    }
//
//    @Test
//    public void testCacheEvictionWhenAddingEmployee() {
//        // First, call the analyze endpoint to cache results
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//
//        // Add a new employee
//        Employee newEmployee = new Employee(11L, "New Employee", "A");
//        HttpEntity<Employee> request = new HttpEntity<>(newEmployee);
//        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(
//                "http://localhost:" + port + "/api/bell-curve/add", request, Employee.class);
//
//        // Verify successful employee addition
//        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
//
//        // Call analyze again - should hit the database because cache was evicted
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//
//        // Verify analyze was called twice (not using cached result after employee addition)
//        verify(bellCurveController, times(2)).analyze();
//    }
//
//    @Test
//    public void testManualCacheClear() {
//        // First, call endpoints to cache results
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/employees", List.class);
//
//        // Clear cache manually
//        ResponseEntity<String> clearResponse = restTemplate.exchange(
//                "http://localhost:" + port + "/api/bell-curve/clear-cache",
//                HttpMethod.POST, null, String.class);
//
//        // Verify successful cache clearing
//        assertEquals(HttpStatus.OK, clearResponse.getStatusCode());
//        assertEquals("All caches cleared successfully", clearResponse.getBody());
//
//        // Call endpoints again - should hit the database
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/analyze", Map.class);
//        restTemplate.getForEntity("http://localhost:" + port + "/api/bell-curve/employees", List.class);
//
//        // Verify methods were called again after cache clearing
//        verify(bellCurveController, times(2)).analyze();
//        verify(bellCurveController, times(2)).getAllEmployees();
//    }
//}