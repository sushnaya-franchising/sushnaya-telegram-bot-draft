package com.sushnaya.entity;

import java.util.Objects;

public class Comment extends Entity {
    private int userId;
    private String text;

    public Comment(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comment comment = (Comment) o;
        return userId == comment.userId &&
                Objects.equals(text, comment.text);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, text);
    }
}
