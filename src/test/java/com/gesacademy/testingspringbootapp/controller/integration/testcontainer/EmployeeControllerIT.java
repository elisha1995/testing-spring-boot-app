package com.gesacademy.testingspringbootapp.controller.integration.testcontainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerIT {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // just to know the details of the container
        System.out.println("MySQL Container JDBC URL: " + mySQLContainer.getJdbcUrl());
        System.out.println("MySQL Container Username: " + mySQLContainer.getUsername());
        System.out.println("MySQL Container Password: " + mySQLContainer.getPassword());
        System.out.println("MySQL Container Database: " + mySQLContainer.getDatabaseName());

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();


        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        // given - precondition or setup
        List<Employee> listOfEmployees = List.of(
                Employee.builder().firstName("John").lastName("Doe").email("john.doe@example.com").build(),
                Employee.builder().firstName("Jane").lastName("Smith").email("jane.smith@example.com").build()
        );

        employeeRepository.saveAll(listOfEmployees);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {

        // given - precondition or setup
        Long employeeId = 1L;

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();


        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", savedEmployee.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    /**
     * JUnit test for update employee operation (negative scenario)
     *
     * @throws Exception
     */
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        // given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}", employeeId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
