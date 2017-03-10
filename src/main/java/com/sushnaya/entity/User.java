package com.sushnaya.entity;

import java.util.Objects;

import static com.sushnaya.entity.Role.ADMIN;
import static com.sushnaya.entity.Role.USER;

public class User extends Entity {
    private Integer telegramId;
    private Integer digitsId;
    private String telegramUsername;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private Menu selectedMenu;

    public User(String firstName, String lastName, Integer telegramId, String telegramUsername, String phoneNumber) {
        this(firstName, lastName, telegramId, telegramUsername, phoneNumber, USER);
    }

    public User(String firstName, String lastName, Integer telegramId, String telegramUsername, String phoneNumber, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telegramId = telegramId;
        this.telegramUsername = telegramUsername;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTelegramId(int telegramId) {
        this.telegramId = telegramId;
    }

    public Menu getSelectedMenu() {
        return selectedMenu;
    }

    public boolean didSelectMenu() {
        return selectedMenu != null;
    }

    public void setSelectedMenu(Menu selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        StringBuilder b = new StringBuilder();

        b.append(getFirstName()).append(' ').append(getLastName());

        return b.toString();
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getDigitsId() {
        return digitsId;
    }

    public void setDigitsId(int digitsId) {
        this.digitsId = digitsId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public void setDigitsId(Integer digitsId) {
        this.digitsId = digitsId;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == ADMIN;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(getTelegramId(), user.getTelegramId()) &&
                Objects.equals(getDigitsId(), user.getDigitsId()) &&
                Objects.equals(getTelegramUsername(), user.getTelegramUsername()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getPhoneNumber(), user.getPhoneNumber()) &&
                Objects.equals(getRole(), user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTelegramId(), getDigitsId(), getTelegramUsername(), getFirstName(), getLastName(), getPhoneNumber(), getRole());
    }

    public static User fromTelegramUser(org.telegram.telegrambots.api.objects.User user) {
        return new User(
                user.getFirstName(),
                user.getLastName(),
                user.getId(),
                user.getUserName(),
                null);
    }
}
