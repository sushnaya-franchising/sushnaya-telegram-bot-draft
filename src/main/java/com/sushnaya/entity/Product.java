package com.sushnaya.entity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class Product extends Entity {
    private MenuCategory menuCategory;
    private String name;
    private Double price;
    private String subheading;
    private String description;
    private String imageUri;
    private String telegramFilePath;
    private int likesCount;
    private List<Comment> comments;
    private boolean isPublished;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, Double price, boolean isPublished) {
        this(name, price, isPublished, null);
    }

    public Product(String name, Double price, boolean isPublished, MenuCategory menuCategory) {
        this.name = name;
        this.price = price;
        this.isPublished = isPublished;
        this.menuCategory = menuCategory;
    }

    public String getTelegramFilePath() {
        return telegramFilePath;
    }

    public void setTelegramFilePath(String telegramFilePath) {
        this.telegramFilePath = telegramFilePath;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasSubheading() {
        return subheading != null;
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

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getDisplayName(Locale locale) {
        StringBuilder sb = new StringBuilder().append(capitalize(getName()));

        if (hasPrice()) {
            final NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
            sb.append(", ").append(currency.format(getPrice()));
        }

        return sb.toString();
    }

    protected boolean hasPrice() {
        return getPrice() != null;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return getLikesCount() == product.getLikesCount() &&
                isPublished() == product.isPublished() &&
                Objects.equals(getMenuCategory(), product.getMenuCategory()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getSubheading(), product.getSubheading()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getImageUri(), product.getImageUri()) &&
                Objects.equals(getTelegramFilePath(), product.getTelegramFilePath()) &&
                Objects.equals(getComments(), product.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMenuCategory(), getName(), getPrice(), getSubheading(), getDescription(), getImageUri(), getTelegramFilePath(), getLikesCount(), getComments(), isPublished());
    }
}
