package bbdadam.perfinal.bigBOB.provider;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import bbdadam.perfinal.bigBOB.entity.Employee;
import bbdadam.perfinal.bigBOB.repository.CityToVisitRepository;
import bbdadam.perfinal.bigBOB.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("cityProvider")
public class CityToVisitProvider {
  private final static double AVG_TEMP_THRESHOLD_MIN = 10.0;
  private final static double AVG_TEMP_THRESHOLD_MAX = 30.0;
  private final static double PRECIPITATION_THRESHOLD_MAX = 5.0;
  private final static double HQ_LATITUDE = 47.4979937;
  private final static double HQ_LONGITUDE = 19.0403594;

  @Autowired
  private WeatherForeCastProvider forecastProvider;
  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private CityToVisitRepository cityToVisitRepository;

  public CityToVisit getCityToVisit(String employeeName){
    Employee employee = employeeRepository.findEmployeeByName(employeeName).get(0);
    List<CityToVisit> cities = cityToVisitRepository.findAll();
    List<CityToVisit> possibleCitiesToVisit = new ArrayList<>();

    return null;
  }
  /**
   * Provides the distance between cities, based on latitude and longitude.
   * Using the Haversine formula
   * @param cityA
   * @return distanceBetweenCitiesInKM
   */
  private double getDistanceBetweenCities(CityToVisit cityA){
    final double RADIUS_OF_EARTH = 6371;

    double latiA = (cityA.getLatitude() * Math.PI) / 180;
    double lonA = (cityA.getLongitude() * Math.PI) / 180;
    double latiB = (HQ_LATITUDE * Math.PI) / 180;
    double lonB = (HQ_LONGITUDE * Math.PI) / 180;

    double dLat = latiB - latiA;
    double dLon = lonB - lonA;

    double havA = Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(latiA) * Math.cos(latiB)
            * Math.pow(Math.sin(dLon / 2),2);

    double havC = 2 * Math.asin(Math.sqrt(havA));

    return (havC * RADIUS_OF_EARTH);
  }

}
