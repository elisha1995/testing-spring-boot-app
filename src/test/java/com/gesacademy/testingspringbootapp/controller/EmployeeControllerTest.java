package com.gesacademy.testingspringbootapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;



@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
                Employee.builder().id(1L).firstName("John").lastName("Doe").email("john.doe@example.com").build(),
                Employee.builder().id(2L).firstName("Jane").lastName("Smith").email("jane.smith@example.com").build()
        );

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

}