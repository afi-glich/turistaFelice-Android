package com.marco.turistafelicetest.models;

import java.util.List;

public class Place {
    private String business_status;
    /*private String geometry_location_latitude;
    private String geometry_location_longitude;
    private String geometry_viewport_northeast_latitude;
    private String geometry_viewport_northeast_longitude;
    private String geometry_viewport_southwest_latitude;
    private String geometry_viewport_southwest_longitude;*/
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private String place_id;
    //private String plus_code;
    private String rating;
    private String vicinity;
    List<PlacePhoto> photos;
    List<String> types;

    public Place(String business_status, Geometry geometry,/*String geometry_location_latitude, String geometry_location_longitude, String geometry_viewport_northeast_latitude, String geometry_viewport_northeast_longitude, String geometry_viewport_southwest_latitude, String geometry_viewport_southwest_longitude,*/ String icon_url, String id, String name, String place_id, String plus_code, String rating, String vicinity, List<PlacePhoto> photos, List<String> types) {
        this.business_status = business_status;
        /*this.geometry_location_latitude = geometry_location_latitude;
        this.geometry_location_longitude = geometry_location_longitude;
        this.geometry_viewport_northeast_latitude = geometry_viewport_northeast_latitude;
        this.geometry_viewport_northeast_longitude = geometry_viewport_northeast_longitude;
        this.geometry_viewport_southwest_latitude = geometry_viewport_southwest_latitude;
        this.geometry_viewport_southwest_longitude = geometry_viewport_southwest_longitude;*/
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        //this.plus_code = plus_code;
        this.rating = rating;
        this.vicinity = vicinity;
        this.photos = photos;
        this.types = types;
    }

    public String getBusiness_status() {
        return business_status;
    }

    /*public String getGeometry_location_latitude() {
        return geometry_location_latitude;
    }

    public String getGeometry_location_longitude() {
        return geometry_location_longitude;
    }

    public String getGeometry_viewport_northeast_latitude() {
        return geometry_viewport_northeast_latitude;
    }

    public String getGeometry_viewport_northeast_longitude() {
        return geometry_viewport_northeast_longitude;
    }

    public String getGeometry_viewport_southwest_latitude() {
        return geometry_viewport_southwest_latitude;
    }

    public String getGeometry_viewport_southwest_longitude() {
        return geometry_viewport_southwest_longitude;
    }*/
    public Geometry getGeometry(){
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    /*public String getPlus_code() {
        return plus_code;
    }*/

    public String getRating() {
        return rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    /*public void setGeometry_location_latitude(String geometry_location_latitude) {
        this.geometry_location_latitude = geometry_location_latitude;
    }

    public void setGeometry_location_longitude(String geometry_location_longitude) {
        this.geometry_location_longitude = geometry_location_longitude;
    }

    public void setGeometry_viewport_northeast_latitude(String geometry_viewport_northeast_latitude) {
        this.geometry_viewport_northeast_latitude = geometry_viewport_northeast_latitude;
    }

    public void setGeometry_viewport_northeast_longitude(String geometry_viewport_northeast_longitude) {
        this.geometry_viewport_northeast_longitude = geometry_viewport_northeast_longitude;
    }

    public void setGeometry_viewport_southwest_latitude(String geometry_viewport_southwest_latitude) {
        this.geometry_viewport_southwest_latitude = geometry_viewport_southwest_latitude;
    }

    public void setGeometry_viewport_southwest_longitude(String geometry_viewport_southwest_longitude) {
        this.geometry_viewport_southwest_longitude = geometry_viewport_southwest_longitude;
    }*/
    public void setGeometry(Geometry geometry){
        this.geometry = geometry;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    /*public void setPlus_code(String plus_code) {
        this.plus_code = plus_code;
    }*/

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setPhotos(List<PlacePhoto> photos) {
        this.photos = photos;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Place{" +
                "business_status='" + business_status + '\'' +
                /*", geometry_location_latitude=" + geometry_location_latitude +
                ", geometry_location_longitude=" + geometry_location_longitude +
                ", geometry_viewport_northeast_latitude=" + geometry_viewport_northeast_latitude +
                ", geometry_viewport_northeast_longitude=" + geometry_viewport_northeast_longitude +
                ", geometry_viewport_southwest_latitude=" + geometry_viewport_southwest_latitude +
                ", geometry_viewport_southwest_longitude=" + geometry_viewport_southwest_longitude +*/
                ", geometry='" + geometry.toString() + '\'' +
                ", icon_url='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", place_id='" + place_id + '\'' +
                //", plus_code='" + plus_code + '\'' +
                ", rating=" + rating +
                ", vicinity='" + vicinity + '\'' +
                ", photos=" + photos +
                ", types=" + types +
                '}';
    }
}
