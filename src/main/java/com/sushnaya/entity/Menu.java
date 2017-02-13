package com.sushnaya.entity;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Menu extends Entity {
    private int cityId;
    private List<MenuCategory> menuCategories;

    public Menu(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void addCategory(MenuCategory category) {
        if(menuCategories == null) menuCategories = Lists.newLinkedList();

        menuCategories.add(category);
    }

    public void removeCategory(int menuCategoryId) {
        if(menuCategories == null || menuCategories.isEmpty()) return;

        Iterator<MenuCategory> iterator = menuCategories.iterator();
        while (iterator.hasNext()) {
            MenuCategory category = iterator.next();
            if(category.getId() == menuCategoryId) {
                iterator.remove();
                return;
            }
        }
    }

    public List<MenuCategory> getMenuCategories() {
        return Collections.unmodifiableList(menuCategories);
    }
}
