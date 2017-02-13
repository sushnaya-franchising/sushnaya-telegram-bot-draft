package com.sushnaya.entity;

import java.util.Objects;

public class Brand extends Entity {
    private String name;
    private String description;
    private String logoImageUri;
    private String iOSAppUrl;
    private String androidAppUrl;

    public Brand(String name) {
        this(name, null);
    }

    public Brand(String name, String logoImageUri) {
        this.name = name;
        this.logoImageUri = logoImageUri;
    }

    public String getIdAsHexString() {
        return Integer.toHexString(getId());
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

    public String getLogoImageUri() {
        return logoImageUri;
    }

    public void setLogoImageUri(String logoImageUri) {
        this.logoImageUri = logoImageUri;
    }

    public String getIOSAppUrl() {
        return iOSAppUrl;
    }

    public void setIOSAppUrl(String iOSAppUrl) {
        this.iOSAppUrl = iOSAppUrl;
    }

    public String getAndroidAppUrl() {
        return androidAppUrl;
    }

    public void setAndroidAppUrl(String androidAppUrl) {
        this.androidAppUrl = androidAppUrl;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand)) return false;
        Brand brand = (Brand) o;
        return Objects.equals(getName(), brand.getName()) &&
                Objects.equals(getDescription(), brand.getDescription()) &&
                Objects.equals(getLogoImageUri(), brand.getLogoImageUri()) &&
                Objects.equals(iOSAppUrl, brand.iOSAppUrl) &&
                Objects.equals(getAndroidAppUrl(), brand.getAndroidAppUrl());
    }

    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getLogoImageUri(), iOSAppUrl, getAndroidAppUrl());
    }
}
