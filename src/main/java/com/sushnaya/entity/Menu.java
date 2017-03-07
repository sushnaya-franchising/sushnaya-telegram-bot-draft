package com.sushnaya.entity;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Menu extends Entity {
    private Locality locality;
    private List<MenuCategory> menuCategories;

    public Menu() {
    }

    public Menu(Locality locality, MenuCategory... menuCategories) {
        this.locality = locality;

        if (menuCategories != null) {
            for (MenuCategory menuCategory : menuCategories) {
                addCategory(menuCategory);
            }
        }
    }

    public void addCategory(MenuCategory category) {
        if (menuCategories == null) menuCategories = Lists.newLinkedList();

        category.setMenu(this);

        menuCategories.add(category);
    }

    public void removeCategory(int menuCategoryId) {
        if (menuCategories == null || menuCategories.isEmpty()) return;

        Iterator<MenuCategory> iterator = menuCategories.iterator();
        while (iterator.hasNext()) {
            MenuCategory category = iterator.next();
            if (category.getId() == menuCategoryId) {
                iterator.remove();
                return;
            }
        }
    }

    public List<MenuCategory> getMenuCategories() {
        if (menuCategories == null) return null;

        return Collections.unmodifiableList(menuCategories);
    }

    public MenuCategory getFirstCategory() {
        return getMenuCategories().get(0);
    }

    public Locality getLocality() {
        return locality;
    }

    public String getLocalityName() {
        return getLocality().getName();
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        if (!super.equals(o)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getLocality(), menu.getLocality()) &&
                Objects.equals(getMenuCategories(), menu.getMenuCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLocality(), getMenuCategories());
    }

    public List<MenuCategory> getCategoriesWithPublishedProducts() {
        if(getMenuCategories() == null) return null;

        return getMenuCategories().stream().filter(MenuCategory::hasPublishedProducts)
                .collect(Collectors.toList());
    }
}
