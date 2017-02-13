package com.sushnaya.entity;

import java.util.List;
import java.util.Objects;

public class Product extends Entity {
    private int menuCategoryId;
    private int menuId;
    private String name;
    private float price;
    private String description;
    private String imageUri;
    private int likesCount;
    private List<Comment> comments;

    public int getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(int menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return getMenuCategoryId() == product.getMenuCategoryId() &&
                getMenuId() == product.getMenuId() &&
                Float.compare(product.getPrice(), getPrice()) == 0 &&
                getLikesCount() == product.getLikesCount() &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getImageUri(), product.getImageUri()) &&
                Objects.equals(getComments(), product.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMenuCategoryId(), getMenuId(), getName(), getPrice(), getDescription(), getImageUri(), getLikesCount(), getComments());
    }
}
