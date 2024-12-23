package com.yashkolte.Assignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yashkolte.Assignment.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
	
}
