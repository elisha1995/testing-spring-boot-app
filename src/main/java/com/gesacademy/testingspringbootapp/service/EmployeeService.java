package com.gesacademy.testingspringbootapp.service;

import com.gesacademy.testingspringbootapp.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();
}
