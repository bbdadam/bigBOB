package bbdadam.perfinal.bigBOB.repository;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityToVisitRepository extends JpaRepository<CityToVisit, Long> {
}

