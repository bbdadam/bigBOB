package bbdadam.perfinal.bigBOB.repositoryTest;

import bbdadam.perfinal.bigBOB.entity.Employee;
import bbdadam.perfinal.bigBOB.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class EmployeeRepositoryTest {
  @Autowired
  private EmployeeRepository repo;

  @Test
  public void findByEmployeeByNameTest(){
    String empName = "Gazsi";
    Employee emp = new Employee(empName);
    repo.save(emp);

    assertEquals(emp, repo.findEmployeeByName(empName).get(0));
  }
}
