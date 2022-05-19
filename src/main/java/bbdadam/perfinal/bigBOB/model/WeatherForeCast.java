package bbdadam.perfinal.bigBOB.model;

public class WeatherForeCast {
  private final double precipitation;
  private final double temperature;

  public WeatherForeCast(double precipitation, double temperature){
    this.precipitation = precipitation;
    this.temperature = temperature;
  }

  public double getPrecipitation() {
    return precipitation;
  }

  public double getTemperature() {
    return temperature;
  }
}
