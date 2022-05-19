package bbdadam.perfinal.bigBOB.provider;

import bbdadam.perfinal.bigBOB.repository.CityToVisitRepository;
import bbdadam.perfinal.bigBOB.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
