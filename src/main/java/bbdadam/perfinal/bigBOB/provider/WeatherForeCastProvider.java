package bbdadam.perfinal.bigBOB.provider;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import bbdadam.perfinal.bigBOB.model.WeatherForeCast;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WeatherForeCastProvider {
  private static final String TEMPLATE = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,precipitation";

  /**
   * Returns the weatherForecast by days of the week.
   * @param city
   * @return A TreeMap K -> DayOfWeek, V -> WeatherForeCast
   */
  public Map<DayOfWeek, WeatherForeCast> getForeCastByCity(CityToVisit city){
    return callOpenMeteoAPI(city.getLatitude(), city.getLongitude());
  }

  /**
   * Calls the open-meteo API to get the actual weatherForecast for the week. Then transfer the JSON map into the desired tree map.
   * @param latitude
   * @param longitude
   * @return A TreeMap K -> DayOfWeek, V -> WeatherForeCast
   */
  private Map<DayOfWeek, WeatherForeCast> callOpenMeteoAPI(double latitude, double longitude){
    String url = String.format(TEMPLATE, latitude, longitude);
    RestTemplate restTemplate = new RestTemplate();
    String resp = restTemplate.getForObject(url, String.class);
    JsonParser jsonParser = JsonParserFactory.getJsonParser();
    Map<String, Object> map = jsonParser.parseMap(resp);

    return getForeCast(map);
  }

  /**
   * Returns the foreCast
   * @param map JSON map provided by the open-meteo API
   * @return A TreeMap K -> DayOfWeek, V -> WeatherForeCast
   */
  private Map<DayOfWeek, WeatherForeCast> getForeCast(Map<String, Object> map){
    Map<String, Object> hourlyEntry = (Map) map.get("hourly");
    List<Double> temps = convertToDoubletList((List) hourlyEntry.get("temperature_2m"));
    List<Double> prec = convertToDoubletList((List) hourlyEntry.get("precipitation"));
    List<String> hours = (List<String>) hourlyEntry.get("time");

    Map<DayOfWeek, WeatherForeCast> foreCastByWeek = new TreeMap<>();

    double precPerDay = 0;
    double tempAvgPerDay = 0;
    for (int i = 0; i < hours.size(); i++) {
      DayOfWeek day = LocalDateTime.parse(hours.get(i)).getDayOfWeek();
      precPerDay += prec.get(i);
      tempAvgPerDay += temps.get(i);
      if ((i+1)%24==0){
        tempAvgPerDay = tempAvgPerDay / 24;
        foreCastByWeek.put(day, new WeatherForeCast(precPerDay, tempAvgPerDay));
        precPerDay = 0;
        tempAvgPerDay = 0;
      }
    }

    return foreCastByWeek;
  }

  /**
   * This method ensures to convert every nr. type member of the JSON map to Double
   * @param toConvert
   * @return list of doubles
   */
  private List<Double> convertToDoubletList(List toConvert){
    List<Double> doubleList = new ArrayList<>();

    for (int i = 0; i < toConvert.size(); i++){
      if (toConvert.get(i).getClass().getSimpleName().equals("Integer")){
        Integer integer = (Integer) toConvert.get(i);
        doubleList.add(Double.valueOf(integer));
      } else {
        doubleList.add((Double) toConvert.get(i));
      }
    }
    return doubleList;
  }
}
