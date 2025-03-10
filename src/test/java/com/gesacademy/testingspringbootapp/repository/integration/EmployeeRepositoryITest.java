package com.gesacademy.testingspringbootapp.repository.integration;

import com.gesacademy.testingspringbootapp.controller.integration.testcontainer.AbstractContainerBaseTest;
import com.gesacademy.testingspringbootapp.exception.ResourceNotFoundException;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITest extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }

    // Junit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // Given - precondition or setup


        // When - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for get all employees operation
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

        // Given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // When - action or the behaviour that we are going to test
        var employees = employeeRepository.findAll();

        // Then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    // JUnit test for get employee by id operation
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findById(employee.getId());

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employee.getId()));

        savedEmployee.setFirstName("Jane");
        savedEmployee.setLastName("Doe");
        savedEmployee.setEmail("jane.doe@example.com");

        var updatedEmployee = employeeRepository.save(savedEmployee);

        // Then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(updatedEmployee.getEmail()).isEqualTo("jane.doe@example.com");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        employeeRepository.delete(employee);
        var savedEmployee = employeeRepository.findById(employee.getId());

        // Then - verify the output
        assertThat(savedEmployee).isEmpty();
    }

    // Junit test for custom query using JPQL with named parameters
    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom query using JPQL index parameters
    @DisplayName("JUnit test for custom query using JPQL index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLIndexed_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        String firstName = "John";
        String lastName = "Doe";

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findByJPQLIndexed(firstName, lastName);

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom query using native SQL with named parameters
    @DisplayName("JUnit test for custom query using native SQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom query using native SQL with index parameters
    @DisplayName("JUnit test for custom query using native SQL with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLIndexed_thenReturnEmployeeObject() {

        // Given - precondition or setup
        employeeRepository.save(employee);

        // When - action or the behaviour that we are going to test
        var savedEmployee = employeeRepository.findByNativeSQLIndexed(employee.getFirstName(), employee.getLastName());

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
