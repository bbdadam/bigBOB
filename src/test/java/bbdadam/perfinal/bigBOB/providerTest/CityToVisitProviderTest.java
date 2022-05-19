package bbdadam.perfinal.bigBOB.providerTest;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import bbdadam.perfinal.bigBOB.entity.Employee;
import bbdadam.perfinal.bigBOB.model.WeatherForeCast;
import bbdadam.perfinal.bigBOB.provider.CityToVisitProvider;
import bbdadam.perfinal.bigBOB.provider.WeatherForeCastProvider;
import bbdadam.perfinal.bigBOB.repository.CityToVisitRepository;
import bbdadam.perfinal.bigBOB.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.util.*;

@SpringBootTest
public class CityToVisitProviderTest {

  @InjectMocks
  private CityToVisitProvider cityProvider;
  @Mock
  private WeatherForeCastProvider forecastProvider;
  @Mock
  private EmployeeRepository employeeRepository;
  @Mock
  private CityToVisitRepository cityToVisitRepository;

  private final CityToVisit city = new CityToVisit("Berlin", 52.5170365, 13.3888599, false);
  private final List<CityToVisit> cities = new ArrayList<>(Arrays.asList(city));
  private final List<Employee> employees = new ArrayList<>(Arrays.asList(new Employee("Pista")));
  private final Map<DayOfWeek, WeatherForeCast> foreCast = new TreeMap<>();

  @BeforeEach
  public void init() {
    Mockito.when(cityToVisitRepository.findAll()).thenReturn(cities);
    Mockito.when(employeeRepository.findEmployeeByName(ArgumentMatchers.anyString())).thenReturn(employees);
    Mockito.when(forecastProvider.getForeCastByCity(ArgumentMatchers.any())).thenReturn(foreCast);
  }

  @Test
  public void testProvider() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0)));
    setUpWeather(weathers);
    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(city, cityByProvider);
  }

  @Test
  public void avgTempUnderMIN() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0),
            new WeatherForeCast(0.0, 9.0)));
    setUpWeather(weathers);

    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(null, cityByProvider);
  }

  @Test
  public void avgTempOverMAXNotSeasideCity() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0)));
    setUpWeather(weathers);

    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(null, cityByProvider);
  }

  @Test
  public void avgTempOverMAXSeasideCity() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0),
            new WeatherForeCast(0.0, 31.0)));
    CityToVisit citySeaside = new CityToVisit("Rio de Janeiro", -22.9110137, -43.2093727, true);
    setUpWeather(weathers);
    cities.add(citySeaside);

    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(citySeaside, cityByProvider);
  }

  @Test
  public void provideFurthestCity() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(0.0, 25.0)));
    CityToVisit furthestCity = new CityToVisit("Rio de Janeiro", -22.9110137, -43.2093727, true);
    setUpWeather(weathers);
    cities.add(furthestCity);

    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(furthestCity, cityByProvider);
  }

  @Test
  public void precipitationOverMAX() {
    List<WeatherForeCast> weathers = new ArrayList<>(Arrays.asList(new WeatherForeCast(3.0, 25.0),
            new WeatherForeCast(4.0, 25.0),
            new WeatherForeCast(0.0, 25.0),
            new WeatherForeCast(5.3, 25.0),
            new WeatherForeCast(5.2, 25.0),
            new WeatherForeCast(5.2, 25.0),
            new WeatherForeCast(0.0, 25.0)));

    setUpWeather(weathers);

    CityToVisit cityByProvider = cityProvider.getCityToVisit("Pista");

    Assertions.assertEquals(null, cityByProvider);
  }

  private void setUpWeather(List<WeatherForeCast> weathers) {
    if (weathers.size() != 7) {
      throw new IllegalArgumentException("Weathers have to be provided for a week! Current size: " + weathers.size());
    }
    foreCast.put(DayOfWeek.MONDAY, new WeatherForeCast(weathers.get(0).getPrecipitation(), weathers.get(0).getTemperature()));
    foreCast.put(DayOfWeek.TUESDAY, new WeatherForeCast(weathers.get(1).getPrecipitation(), weathers.get(1).getTemperature()));
    foreCast.put(DayOfWeek.WEDNESDAY, new WeatherForeCast(weathers.get(2).getPrecipitation(), weathers.get(2).getTemperature()));
    foreCast.put(DayOfWeek.THURSDAY, new WeatherForeCast(weathers.get(3).getPrecipitation(), weathers.get(3).getTemperature()));
    foreCast.put(DayOfWeek.FRIDAY, new WeatherForeCast(weathers.get(4).getPrecipitation(), weathers.get(4).getTemperature()));
    foreCast.put(DayOfWeek.SATURDAY, new WeatherForeCast(weathers.get(5).getPrecipitation(), weathers.get(5).getTemperature()));
    foreCast.put(DayOfWeek.SUNDAY, new WeatherForeCast(weathers.get(6).getPrecipitation(), weathers.get(6).getTemperature()));
  }
}
