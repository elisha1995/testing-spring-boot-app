package com.gesacademy.testingspringbootapp.service;

import com.gesacademy.testingspringbootapp.exception.ResourceAlreadyExistsException;
import com.gesacademy.testingspringbootapp.exception.ResourceNotFoundException;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.repository.EmployeeRepository;
import com.gesacademy.testingspringbootapp.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        List<Employee> employeesList = employeeService.getAllEmployees();

        // Then - verify the output
        assertThat(employeesList).isNotNull();
        assertThat(employeesList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get all employees operation (negative scenario)")
    @Test
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // Given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();

        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // When - action or the behaviour that we are going to test
        List<Employee> employeesList = employeeService.getAllEmployees();

        // Then - verify the output
        assertThat(employeesList).isEmpty();
        assertThat(employeesList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // Given - precondition or setup
        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // When - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employee.getId()));

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Given - precondition or setup
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("ram@gmail.com");
        employee.setFirstName("Ram");

        // When - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // Then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
    }

    @DisplayName("JUnit test for delete employee operation")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // Given - precondition or setup

        BDDMockito.willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // When - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employee.getId());

        // Then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }

}