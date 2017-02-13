package com.sushnaya.entity;

import java.util.Objects;

public class City extends Entity {
    private String name;
    private Coordinate coordinate;

    public City(String name) {
        this(name, null);
    }

    public City(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return Objects.equals(getName(), city.getName()) &&
                Objects.equals(getCoordinate(), city.getCoordinate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCoordinate());
    }
}
