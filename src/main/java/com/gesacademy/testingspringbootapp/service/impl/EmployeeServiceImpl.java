package com.gesacademy.testingspringbootapp.service.impl;

import com.gesacademy.testingspringbootapp.exception.ResourceAlreadyExistsException;
import com.gesacademy.testingspringbootapp.model.Employee;
import com.gesacademy.testingspringbootapp.repository.EmployeeRepository;
import com.gesacademy.testingspringbootapp.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceAlreadyExistsException("Employee already exist with given email:" + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
}
