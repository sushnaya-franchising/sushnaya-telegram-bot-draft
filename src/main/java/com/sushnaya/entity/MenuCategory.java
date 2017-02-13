package com.sushnaya.entity;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MenuCategory extends Entity {
    private int menuId;
    private String name;
    private List<Product> products;

    public MenuCategory(int menuId, String name) {
        this.menuId = menuId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        if (products == null) products = Lists.newArrayList();

        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuCategory)) return false;
        if (!super.equals(o)) return false;
        MenuCategory that = (MenuCategory) o;
        return getMenuId() == that.getMenuId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getProducts(), that.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMenuId(), getName(), getProducts());
    }
}
