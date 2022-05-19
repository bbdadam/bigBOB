package bbdadam.perfinal.bigBOB.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_EMPLOYEES")
public class Employee {
  private final static int NR_OF_CITIES_VISITED_THRESHOLD = 2;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "Employee_name")
  private String name;
  @ManyToMany
  private final List<CityToVisit> visitedCities = new ArrayList<>();

  public Employee(){

  }

  public Employee(String name) {
    this.name = name;
  }

  public void bookTrip(CityToVisit cityToVisit){
    this.visitedCities.add(cityToVisit);
  }

  public boolean visitedRecently(CityToVisit cityToVisit){
    boolean alreadyVisited = false;
    int i = visitedCities.size()-1;
    int lastVisitedCityIndex = i - NR_OF_CITIES_VISITED_THRESHOLD;
    while (i > -1 &&
            i > lastVisitedCityIndex){
      if (visitedCities.get(i).equals(cityToVisit)){
        alreadyVisited = true;
      }
      i--;
    }

    return alreadyVisited;
  }

  public String getName() {
    return this.name;
  }
}
