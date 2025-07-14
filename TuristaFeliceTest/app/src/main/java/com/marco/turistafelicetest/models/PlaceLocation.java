package com.marco.turistafelicetest.models;

public class PlaceLocation {
    private String lat;
    private String lng;

    @Override
    public String toString() {
        return "PlaceLocation{" +
                "lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    public PlaceLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
