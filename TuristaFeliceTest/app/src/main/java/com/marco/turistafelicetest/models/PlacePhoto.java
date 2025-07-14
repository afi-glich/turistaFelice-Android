package com.marco.turistafelicetest.models;

public class PlacePhoto {
    private int width;
    private int height;
    private String photo_reference;

    public PlacePhoto(int width, int height, String photo_reference) {
        this.width = width;
        this.height = height;
        this.photo_reference = photo_reference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    @Override
    public String toString() {
        return "PlacePhoto{" +
                "width=" + width +
                ", height=" + height +
                ", photo_reference='" + photo_reference + '\'' +
                '}';
    }
}
