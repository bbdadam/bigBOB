package bbdadam.perfinal.bigBOB.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TBL_CITIES")
public class CityToVisit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "City_name")
  private String cityName;
  @Column(name = "City_latitude")
  private double latitude;
  @Column(name = "City_longitude")
  private double longitude;
  @Column(name = "City_seaside")
  private boolean seaside;

  public CityToVisit(){

  }

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
