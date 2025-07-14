package com.marco.turistafelicetest.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trip implements Comparable<Trip>, Parcelable {

    private String departure;
    private String returnDate;
    private String title;
    private String city;
    private int id;
    private String placeName;
    private String urlToCityImage;

    public Trip() {

        id = 0;
        title = "";
        city = "";
        departure = "";
        returnDate = "";
        placeName = "";
        urlToCityImage = "";

    }

    public Trip(String details) {
        String[] detail = details.split(";");

        // array detail is defined as [id, trip_name, city, departure_date, return_date]
        id = Integer.parseInt(detail[0]);
        title = detail[1];
        city = detail[2];
        departure = detail[3];
        returnDate = detail[4];
        urlToCityImage = detail[5];
        placeName = detail[6];

    }

    public String getPlaceName() { return placeName;
    }

    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String aReturn) {
        returnDate = aReturn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrlToCityImage() {
        return urlToCityImage;
    }

    public void setUrlToCityImage(String urlToCityImage) {
        this.urlToCityImage = urlToCityImage;
    }

    @Override
    public String toString() {
        String temp = String.valueOf(id);

        return temp + ';' + title + ';' + city + ';' + departure + ';' + returnDate + ";" + urlToCityImage + ";" + placeName;
    }

    @Override
    public int compareTo(Trip o) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        Date d2 = null;
        try {
            d = sdf.parse(this.getDeparture());
            d2 = sdf.parse(o.getDeparture());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.compareTo(d2);
    }

    protected Trip(Parcel in) {
        departure = in.readString();
        returnDate = in.readString();
        title = in.readString();
        city = in.readString();
        id = in.readInt();
        placeName = in.readString();
        urlToCityImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departure);
        dest.writeString(returnDate);
        dest.writeString(title);
        dest.writeString(city);
        dest.writeInt(id);
        dest.writeString(placeName);
        dest.writeString(urlToCityImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

}
