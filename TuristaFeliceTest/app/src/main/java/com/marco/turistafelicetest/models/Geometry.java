package com.marco.turistafelicetest.models;

public class Geometry {
    private PlaceLocation location;
    private Object viewport;

    public Geometry(PlaceLocation location, Object viewport) {
        this.location = location;
        this.viewport = viewport;
    }

    public PlaceLocation getLocation() {
        return location;
    }

    public void setLocation(PlaceLocation location) {
        this.location = location;
    }

    public Object getViewport() {
        return viewport;
    }

    public void setViewport(Object viewport) {
        this.viewport = viewport;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                ", viewport=" + viewport +
                '}';
    }
}
