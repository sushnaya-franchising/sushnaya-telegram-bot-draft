package com.sushnaya.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class Locality extends Entity {
    private String name;
    private String description;
    private Coordinate coordinate;

    public Locality(String name, String description, Coordinate coordinate) {
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return new StringBuilder().append(getName()).append(", ")
                .append(getDescription()).toString();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public static Locality parseYandexGeocodeJson(String json) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode geoObject = jsonNode.get("response")
                .get("GeoObjectCollection")
                .get("featureMember").get(0).get("GeoObject");
        String point = geoObject.get("Point").get("pos").asText();
        Coordinate coordinate = Coordinate.parse(point);
        String name = geoObject.get("name").asText();
        String description = geoObject.get("description").asText();

        return new Locality(name, description, coordinate);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locality)) return false;
        Locality locality = (Locality) o;
        return Objects.equals(getName(), locality.getName()) &&
                Objects.equals(getCoordinate(), locality.getCoordinate());
    }

    public int hashCode() {
        return Objects.hash(getName(), getCoordinate());
    }
}
