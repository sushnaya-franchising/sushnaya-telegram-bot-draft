package com.sushnaya.entity;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

import static com.sushnaya.entity.Permission.*;

public class Role extends Entity {

    public static final Role ADMIN = new Role("Администратор", Lists.newArrayList(
            READ_BRANDS, WRITE_BRANDS,
            READ_MENU, WRITE_MENU,
            READ_MENU_CATEGORIES, WRITE_MENU_CATEGORIES,
            READ_PRODUCTS, WRITE_PRODUCTS,
            READ_USERS, WRITE_USERS,
            READ_ROLES, WRITE_ROLES,
            READ_DELIVERY_ZONES, WRITE_DELIVERY_ZONES
    ));

    public static final Role MODERATOR = new Role("Модератор", Lists.newArrayList(
            READ_BRANDS, WRITE_BRANDS,
            READ_MENU, WRITE_MENU,
            READ_MENU_CATEGORIES, WRITE_MENU_CATEGORIES,
            READ_PRODUCTS, WRITE_PRODUCTS,
            READ_DELIVERY_ZONES, WRITE_DELIVERY_ZONES
    ));

    public static final Role USER = new Role("Пользователь", Lists.newArrayList(
            READ_BRANDS, READ_MENU, READ_MENU_CATEGORIES, READ_PRODUCTS
    ));

    private String name;
    private List<Permission> permissions;

    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(getName(), role.getName()) &&
                Objects.equals(getPermissions(), role.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getPermissions());
    }
}
