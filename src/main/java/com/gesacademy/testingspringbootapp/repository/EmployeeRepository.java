package com.gesacademy.testingspringbootapp.repository;

import com.gesacademy.testingspringbootapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // Define a custom query method using JPQL with named parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Optional<Employee> findByJPQL(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Define a custom query method using JPQL index parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Optional<Employee> findByJPQLIndexed(String firstName, String lastName);

    // Define a custom query method using native SQL with named parameters
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = :firstName AND e.last_name = :lastName", nativeQuery = true)
    Optional<Employee> findByNativeSQL(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Define a custom query method using native SQL with index parameters
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = ?1 AND e.last_name = ?2", nativeQuery = true)
    Optional<Employee> findByNativeSQLIndexed(String firstName, String lastName);
}
