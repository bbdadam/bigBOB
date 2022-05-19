package bbdadam.perfinal.bigBOB.entity;

import java.util.Objects;

public class CityToVisit {
  private Long id;
  private String cityName;
  private double latitude;
  private double longitude;
  private boolean seaside;

  public CityToVisit(String cityName, double latitude, double longitude, boolean seaside) {
    this.cityName = cityName;
    this.latitude = latitude;
    this.longitude = longitude;
    this.seaside = seaside;
  }

  @Override
  public boolean equals(Object cityToCompare) {
    if (this == cityToCompare) return true;
    if (cityToCompare == null || getClass() != cityToCompare.getClass()) return false;
    CityToVisit that = (CityToVisit) cityToCompare;
    return cityName.equals(that.cityName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityName);
  }

  public String getCityName() {
    return cityName;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public boolean isSeaside() {
    return seaside;
  }
}
