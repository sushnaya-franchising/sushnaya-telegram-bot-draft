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
    private String photoUri;
    private String telegramPhotoFileId;
    private int likesCount;
    private List<Comment> comments;
    private boolean isPublished;
    private MenuCategory category;

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

    public String getTelegramPhotoFileId() {
        return telegramPhotoFileId;
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

    public void setTelegramPhotoFileId(String telegramPhotoFileId) {
        this.telegramPhotoFileId = telegramPhotoFileId;
    }

    public MenuCategory getCategory() {
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
        this.price = Math.abs(price);
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

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
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

    public String getDisplayName() {
        return capitalize(getName());
    }

    public String getDisplayNameWithPrice(Locale locale) {
        StringBuilder sb = new StringBuilder().append(getDisplayName());

        if (hasPrice()) {
            final NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
            currency.setMinimumFractionDigits(price % 1 != 0 ? 2 : 0);

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
                Objects.equals(getCategory(), product.getCategory()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getSubheading(), product.getSubheading()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getPhotoUri(), product.getPhotoUri()) &&
                Objects.equals(getTelegramPhotoFileId(), product.getTelegramPhotoFileId()) &&
                Objects.equals(getComments(), product.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategory(), getName(), getPrice(), getSubheading(), getDescription(), getPhotoUri(), getTelegramPhotoFileId(), getLikesCount(), getComments(), isPublished());
    }

}
