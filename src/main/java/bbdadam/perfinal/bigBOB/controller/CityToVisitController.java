package bbdadam.perfinal.bigBOB.controller;

import bbdadam.perfinal.bigBOB.entity.CityToVisit;
import bbdadam.perfinal.bigBOB.provider.CityToVisitProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityToVisitController {
  @Autowired
  private CityToVisitProvider cityProvider;

  @GetMapping("/cityToVisit")
  public String greeting(@RequestParam(value = "employeeName", defaultValue="User") String employeeName){
    CityToVisit city = cityProvider.getCityToVisit(employeeName);
    if (city == null){
      return "None of the cities are applicable to visit currently!";
    } else {
      return city.getCityName();
    }
  }
}
