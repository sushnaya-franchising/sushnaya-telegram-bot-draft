package com.sushnaya.entity;

import java.util.Objects;

public class Coordinate {
    private float longitude;
    private float latitude;

    public Coordinate(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return Float.compare(that.getLongitude(), getLongitude()) == 0 &&
                Float.compare(that.getLatitude(), getLatitude()) == 0;
    }

    public int hashCode() {
        return Objects.hash(getLongitude(), getLatitude());
    }

    public static Coordinate parse(String point) {
        String[] coordinate = point.split("\\s", 2);

        return new Coordinate(Float.parseFloat(coordinate[0]), Float.parseFloat(coordinate[1]));
    }
}
