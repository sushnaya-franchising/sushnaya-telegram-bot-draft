package com.sushnaya.entity;

import java.util.List;
import java.util.Objects;

public class DeliveryZone extends Entity{
    private int cityId;
    private float price;
    private List<List<Coordinate>> polygons;

    public int getCityId() {
        return cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryZone)) return false;
        if (!super.equals(o)) return false;
        DeliveryZone that = (DeliveryZone) o;
        return getCityId() == that.getCityId() &&
                Float.compare(that.price, price) == 0 &&
                Objects.equals(polygons, that.polygons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCityId(), price, polygons);
    }
}
