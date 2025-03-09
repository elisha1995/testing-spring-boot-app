package com.gesacademy.testingspringbootapp.service;

import com.gesacademy.testingspringbootapp.exception.ResourceAlreadyExistsException;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.repository.EmployeeRepository;
import com.gesacademy.testingspringbootapp.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // Given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // When - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for save employee operation which throws exception")
    @Test
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        // Given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // When - action or the behaviour that we are going to test
       assertThrows(ResourceAlreadyExistsException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // Then
        //verify(employeeRepository, never()).save(employee);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for get all employees operation")
    @Test
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // Given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();

        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // When - action or the behaviour that we are going to test
        List<Employee> employees = employeeService.getAllEmployees();

        // Then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }
}