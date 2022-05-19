package bbdadam.perfinal.bigBOB.repository;

import bbdadam.perfinal.bigBOB.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  public List<Employee> findEmployeeByName(String name);
}
