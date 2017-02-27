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
    private String imageUri;
    private String telegramFilePath;
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
        products.add(product);
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTelegramFilePath() {
        return telegramFilePath;
    }

    public void setTelegramFilePath(String telegramFilePath) {
        this.telegramFilePath = telegramFilePath;
    }

    public List<Product> getProducts() {
        if (products == null) return null;

        return Collections.unmodifiableList(products);
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

    public void removeProduct(int productId) {
        if (products == null || products.isEmpty()) return;

        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product next = iterator.next();
            if (next.getId() == productId) {
                iterator.remove();
                return;
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuCategory)) return false;
        if (!super.equals(o)) return false;
        MenuCategory category = (MenuCategory) o;
        return Objects.equals(getName(), category.getName()) &&
                Objects.equals(getSubheading(), category.getSubheading()) &&
                Objects.equals(getImageUri(), category.getImageUri()) &&
                Objects.equals(getTelegramFilePath(), category.getTelegramFilePath()) &&
                Objects.equals(getProducts(), category.getProducts()) &&
                Objects.equals(getMenu(), category.getMenu());
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getSubheading(), getImageUri(), getTelegramFilePath(), getProducts(), getMenu());
    }
}
