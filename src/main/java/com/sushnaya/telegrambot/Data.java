package com.sushnaya.telegrambot;

import com.google.common.collect.Maps;
import com.sushnaya.entity.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sushnaya.entity.Role.ADMIN;
import static com.sushnaya.entity.Role.MODERATOR;

// todo: make all structures immutable
public class Data {
    private volatile static Data INSTANCE;

    private static final Map<Integer, User> ADMINS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, User> USERS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, Brand> BRANDS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, Menu> MENU_BY_ID = Maps.newHashMap();
    private static final Map<Integer, MenuCategory> MENU_CATEGORIES_BY_ID = Maps.newHashMap();
    private static final Map<Integer, Product> PRODUCTS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, City> CITIES_BY_ID = Maps.newHashMap();
    private static final Map<Integer, DeliveryZone> DELIVERY_ZONES_BY_ID = Maps.newHashMap();

    public static Data get() {
        if (INSTANCE == null) {
            synchronized (Data.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Data();
                }
            }
        }

        return INSTANCE;
    }

    private Data() {
        User igorkurylenko = new User("Igor", "Kurylenko",
                109002411, "igorkurylenko",
                "+79997901088", ADMIN);
        User johnsmith = new User("John", "Smith",
                109002412, "johnsmith",
                "+79997901088", MODERATOR);

        saveUser(igorkurylenko);
        saveUser(johnsmith);

//        for (int i = 0; i < 9; i++) {
//            saveBrand(new Brand(UUID.randomUUID().toString().substring(0, new Random().nextInt(25)+5)));
//        }
    }

    public User getUser(int userId) {
        return USERS_BY_ID.get(userId);
    }

    public User getAdmin(int userId) {
        return ADMINS_BY_ID.get(userId);
    }

    public Collection<User> getAdmins() {
        return Collections.unmodifiableCollection(ADMINS_BY_ID.values());
    }

    public void saveBrand(Brand brand) {
        if (brand == null) return;

        BRANDS_BY_ID.put(brand.getId(), brand);
    }

    public void saveUser(@NotNull User user) {
        // todo: merge if already signed up with digits
        if (user == null) return;

        USERS_BY_ID.put(user.getTelegramId(), user);

        if (user.getRole().equals(Role.ADMIN) ||
                user.getRole().equals(Role.MODERATOR)) {
            saveAdmin(user);
        }
    }

    private void saveAdmin(@NotNull User admin) {
        if (admin == null) return;

        ADMINS_BY_ID.put(admin.getTelegramId(), admin);
    }

    public Brand getBrand(int brandId) {
        return BRANDS_BY_ID.get(brandId);
    }

    public Collection<Brand> getBrands() {
        return Collections.unmodifiableCollection(BRANDS_BY_ID.values());
    }

    public Collection<Menu> getMenu() {
        return Collections.unmodifiableCollection(MENU_BY_ID.values());
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(USERS_BY_ID.values());
    }

    public Collection<DeliveryZone> getDeliveryZones() {
        return Collections.unmodifiableCollection(DELIVERY_ZONES_BY_ID.values());
    }

    public Collection<City> getCities() {
        return Collections.unmodifiableCollection(CITIES_BY_ID.values());
    }

    public City getCityId(int cityId) {
        return CITIES_BY_ID.get(cityId);
    }

    public Collection<MenuCategory> getMenuCategories(int menuId) {
        return MENU_CATEGORIES_BY_ID.values().stream().filter(c ->
                c.getMenuId() == menuId).collect(Collectors.toList());
    }
}
