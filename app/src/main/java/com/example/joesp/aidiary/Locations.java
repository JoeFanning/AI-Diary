package com.example.joesp.aidiary;

/**
 * A Locations class for the database
 * Created by joesp on 26/11/2017.
 */

class Locations {

    static Double historicalLongitude = 0.0;
    static Double historicalLatitude = 0.0;
    static Double historicalUpdateTime = 0.0;

    private int id;
    private int date;
    private String longitude = "";
    private String latitude = "";
    private String lastLocationUpdateTime = "";

    Locations() {
    }

    public Locations(int id, int date, String longitude, String latitude, String lastLocationUpdateTime) {
        super();
        this.id = id;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.lastLocationUpdateTime = lastLocationUpdateTime;
    }

    @Override
    public String toString() {
        return " Location id: " + id + " Date: " + id + ", Longitude: " + longitude + ", Latitude: " + latitude +
                "Last Location Update Time: " + lastLocationUpdateTime;
    }

    //getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    String getLongitude() {
        return longitude;
    }

    void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String getLatitude() {
        return latitude;
    }

    void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    void setLastLocationUpdateTime(String lastLocationUpdateTime) {
        this.latitude = lastLocationUpdateTime;
    }

    String getLastLocationUpdateTime() {
        return lastLocationUpdateTime;
    }

}



