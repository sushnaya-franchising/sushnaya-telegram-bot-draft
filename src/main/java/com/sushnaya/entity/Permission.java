package com.sushnaya.entity;

import java.util.Objects;

public class Permission extends Entity {
    public static final Permission READ_BRANDS = new Permission("read_brands");
    public static final Permission WRITE_BRANDS = new Permission("write_brands");

    public static final Permission READ_MENU = new Permission("read_menu");
    public static final Permission WRITE_MENU = new Permission("write_menu");

    public static final Permission READ_MENU_CATEGORIES = new Permission("read_menu_categories");
    public static final Permission WRITE_MENU_CATEGORIES = new Permission("write_menu_categories");

    public static final Permission READ_PRODUCTS = new Permission("read_products");
    public static final Permission WRITE_PRODUCTS = new Permission("write_products");

    public static final Permission READ_USERS = new Permission("read_users");
    public static final Permission WRITE_USERS = new Permission("write_users");

    public static final Permission READ_ROLES = new Permission("read_roles");
    public static final Permission WRITE_ROLES = new Permission("write_roles");

    public static final Permission READ_DELIVERY_ZONES = new Permission("read_delivery_zones");
    public static final Permission WRITE_DELIVERY_ZONES = new Permission("write_delivery_zone");

    private String permissionName;

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return Objects.equals(getPermissionName(), that.getPermissionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPermissionName());
    }
}
