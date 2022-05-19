package bbdadam.perfinal.bigBOB.provider;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import bbdadam.perfinal.bigBOB.entity.Employee;
import bbdadam.perfinal.bigBOB.model.WeatherForeCast;
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

  /**
   * Provides the city where the employee should travel based on weather conditions and previously visited cities.
   * If no city is applicable for a visit, this method return NULL!
   * @param employeeName
   * @return City object. If not applicable could be NULL!
   */
  public CityToVisit getCityToVisit(String employeeName){
    Employee employee = employeeRepository.findEmployeeByName(employeeName).get(0);
    List<CityToVisit> cities = cityToVisitRepository.findAll();
    List<CityToVisit> possibleCitiesToVisit = new ArrayList<>();

    for (CityToVisit city : cities){
      Map<DayOfWeek, WeatherForeCast> foreCastByCity = forecastProvider.getForeCastByCity(city);
      double avgTempPerWeek = getAvgTempPerWeek(foreCastByCity);
      if (avgTempIsFine(avgTempPerWeek, city.isSeaside()) &&
              precipitationIsFine(foreCastByCity) &&
              !employee.visitedRecently(city)){
        possibleCitiesToVisit.add(city);
      }
    }

    if (possibleCitiesToVisit.isEmpty()){
      return null;
    }

    if (possibleCitiesToVisit.size() > 1){
      return getFarthestCityToVisit(possibleCitiesToVisit);
    } else {
      return possibleCitiesToVisit.get(0);
    }
  }

  /**
   * Find the farthest city from the HQ
   * @param possibleCitiesToVisit
   * @return Farthest city
   */
  private CityToVisit getFarthestCityToVisit(List<CityToVisit> possibleCitiesToVisit){
    double longestDistance = 0.0;
    CityToVisit farthestCityToVisit = null;
    for (CityToVisit city : possibleCitiesToVisit){
      double currentDistance = getDistanceBetweenCities(city);
      if (currentDistance > longestDistance){
        farthestCityToVisit = city;
        longestDistance = currentDistance;
      }
    }
    return farthestCityToVisit;
  }

  /**
   * Checks if the average temperature is within the threshold values.
   * @param avgTemp
   * @param isSeasideCity
   * @return boolean
   */
  private static boolean avgTempIsFine(double avgTemp, boolean isSeasideCity){
    if (avgTemp < AVG_TEMP_THRESHOLD_MIN){
      return false;
    }
    if (avgTemp > AVG_TEMP_THRESHOLD_MAX){
      return isSeasideCity;
    }
    return true;
  }

  /**
   * Checks if the precipitation is within under the threshold valu.
   * @param foreCastByCity
   * @return boolean
   */
  private boolean precipitationIsFine(Map<DayOfWeek, WeatherForeCast> foreCastByCity){
    int overThresholdCount = 0;
    boolean precipitationIsFine = true;
    for (DayOfWeek day : foreCastByCity.keySet()){
      if (foreCastByCity.get(day).getPrecipitation() > PRECIPITATION_THRESHOLD_MAX){
        overThresholdCount++;
      } else {
        overThresholdCount = 0;
      }
      if (overThresholdCount > 2){
        precipitationIsFine = false;
      }
    }

    return precipitationIsFine;
  }

  /**
   * Provides the average temperature of the week.
   * @param foreCastPerWeek
   * @return avgTemp
   */
  private double getAvgTempPerWeek(Map<DayOfWeek, WeatherForeCast> foreCastPerWeek){
    if (foreCastPerWeek.keySet().size() != 7){
      throw new IllegalArgumentException("The map should contains the forecast by week! The current size of the map: " + foreCastPerWeek.keySet().size());
    }

    double avgTempPerWeek = 0.0;
    for (DayOfWeek day : foreCastPerWeek.keySet()){
      avgTempPerWeek += foreCastPerWeek.get(day).getTemperature();
    }

    return avgTempPerWeek/7;
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
