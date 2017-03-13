package com.sushnaya.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MenuCategory extends Entity {
    private String name;
    private String subheading;
    private String photoUri;
    private String telegramPhotoFileId;
    private List<Product> products;
    private Menu menu;

    public MenuCategory() {
    }

    public MenuCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        if (products == null) products = Lists.newArrayList();

        product.setMenuCategory(this);
        
        if(!products.contains(product)) products.add(product);
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getTelegramPhotoFileId() {
        return telegramPhotoFileId;
    }

    public void setTelegramPhotoFileId(String telegramPhotoFileId) {
        this.telegramPhotoFileId = telegramPhotoFileId;
    }

    public List<Product> getProducts() {
        if (products == null) return null;

        return Collections.unmodifiableList(products);
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    public Product getFirstProduct() {
        return getProducts().get(0);
    }

    public String getDisplayName() {
        return StringUtils.capitalize(getName());
    }

    public String getDisplaySubheading() {
        return StringUtils.capitalize(getSubheading());
    }

    public String toString() {
        return name;
    }

    // todo: improve design
    public void removeProduct(Product product) {
        if (products == null || products.isEmpty()) return;

        products.remove(product);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuCategory)) return false;
        if (!super.equals(o)) return false;
        MenuCategory category = (MenuCategory) o;
        return Objects.equals(getName(), category.getName()) &&
                Objects.equals(getSubheading(), category.getSubheading()) &&
                Objects.equals(getPhotoUri(), category.getPhotoUri()) &&
                Objects.equals(getTelegramPhotoFileId(), category.getTelegramPhotoFileId()) &&
                Objects.equals(getProducts(), category.getProducts()) &&
                Objects.equals(getMenu(), category.getMenu());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getSubheading(), getPhotoUri(), getTelegramPhotoFileId(), getProducts(), getMenu());
    }

    public boolean hasPublishedProducts() {
        if (getProducts() == null) return false;

        return getProducts().stream().anyMatch(Product::isPublished);
    }

    public boolean hasPhoto() {
        return hasTelegramPhotoFile() || hasPhotoUri();
    }

    public boolean hasPhotoUri() {
        return photoUri != null;
    }

    public boolean hasTelegramPhotoFile() {
        return getTelegramPhotoFileId() != null;
    }

    public boolean hasSubheading() {
        return subheading != null;
    }
}
