package com.example.joesp.aidiary;

/**
 * Weather class for the database
 * Created by joesp on 16/11/2017.
 */

public class Weather {

    private int id;
    private int date;
    private String displayDate;
    private String country;
    private String area;
    private String weatherDescription;
    private String weatherImage;
    private String temperature;
    private String windSpeed;
    private String pressure;
    private String humidity;
    private String visibility;
    private String sunrise;
    private String sunset;

    Weather() {
    }

    public Weather(int date, String dateTime, String country, String area, String weatherDescription,
                   String weatherImage, String temperature, String windSpeed, String pressure, String humidity, String visibility, String sunrise, String sunset) {
        super();
        this.date = date;
        this.displayDate = dateTime;
        this.country = country;
        this.area = area;
        this.weatherDescription = weatherDescription;
        this.weatherImage = weatherImage;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "databaseDate=" + date +
                "displayDate='" + displayDate + '\'' +
                ", country='" + country + '\'' +
                ", area='" + area + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", weatherImage='" + weatherImage + '\'' +
                ", temperature='" + temperature + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", visibility='" + visibility + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                '}';
    }

    //getters & setters
    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getDate() {
        return date;
    }

    void setDate(int date) {
        this.date = date;
    }

    String getDisplayDate() {
        return displayDate;
    }

    void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }

    String getArea() {
        return area;
    }

    void setArea(String area) {
        this.area = area;
    }

    String getWeatherDescription() {
        return weatherDescription;
    }

    void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    String getWeatherImage() {
        return weatherImage;
    }

    void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }

    String getTemperature() {
        return temperature;
    }

    void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    String getWindSpeed() {
        return windSpeed;
    }

    void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    String getPressure() {
        return pressure;
    }

    void setPressure(String pressure) {
        this.pressure = pressure;
    }

    String getHumidity() {
        return humidity;
    }

    void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    String getVisibility() {
        return visibility;
    }

    void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    String getSunrise() {
        return sunrise;
    }

    void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    String getSunset() {
        return sunset;
    }

    void setSunset(String sunset) {
        this.sunset = sunset;
    }

}
