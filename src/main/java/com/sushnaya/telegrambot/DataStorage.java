package com.sushnaya.telegrambot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sushnaya.entity.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.sushnaya.entity.Role.ADMIN;

// todo: make all structures immutable
public class DataStorage {
    private volatile static DataStorage INSTANCE;

    private static final Map<Integer, User> ADMINS_BY_TELEGRAM_ID = Maps.newHashMap();
    private static final Map<Integer, User> USERS_BY_TELEGRAM_ID = Maps.newHashMap();

    private static final Map<Integer, Locality> LOCALITIES_BY_ID = Maps.newHashMap();
    private static final Map<Integer, DeliveryZone> DELIVERY_ZONES_BY_ID = Maps.newHashMap();

    private static final Map<Integer, Menu> MENUS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, MenuCategory> CATEGORIES_BY_ID = Maps.newHashMap();
    private static final Map<Integer, Product> PRODUCTS_BY_ID = Maps.newHashMap();
    private static final Set<Coordinate> BOUND_LOCALITIES = Sets.newHashSet();

    private static final Map<Integer, Menu> DELETED_MENUS_BY_ID = Maps.newHashMap();
    private static final Map<Integer, MenuCategory> DELETED_CATEGORIES_BY_ID = Maps.newHashMap();
    private static final Map<Integer, Product> DELETED_PRODUCTS_BY_ID = Maps.newHashMap();

    // todo: deleted item tables

    public static DataStorage get() {
        if (INSTANCE == null) {
            synchronized (DataStorage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataStorage();
                }
            }
        }

        return INSTANCE;
    }

    private DataStorage() {
        User igorkurylenko = new User("Igor", "Kurylenko",
                109002411, "igorkurylenko",
                "+79997901088", ADMIN);

        saveUser(igorkurylenko);

        Menu moscowMenu = new Menu(new Locality("Москва", "Россия",
                new Coordinate(55.7558f, 37.6173f)));
        MenuCategory rolls = new MenuCategory("роллы");
        Product philadelphia = new Product("филадельфия", 350.0, true);
        Product california = new Product("калифорния", 390.0, true);
        rolls.addProduct(philadelphia);
        rolls.addProduct(california);
        MenuCategory sets = new MenuCategory("сеты");
        Product setNumberOne = new Product("Сет № 1", 2179.0, true);
        Product setNumberTwo = new Product("Сет № 2", 689.0, true);
        sets.addProduct(setNumberOne);
        sets.addProduct(setNumberTwo);
        moscowMenu.addCategory(rolls);
        moscowMenu.addCategory(sets);

        Menu peterMenu = new Menu(new Locality("Питер", "Россия",
                new Coordinate(59.9343f, 30.3351f)));
        rolls = new MenuCategory("роллы");
        philadelphia = new Product("филадельфия", 250.0, true);
        california = new Product("калифорния", 290.0, true);
        rolls.addProduct(philadelphia);
        rolls.addProduct(california);
        sets = new MenuCategory("сеты");
        setNumberOne = new Product("Сет № 1", 1900.0, true);
        setNumberTwo = new Product("Сет № 2", 600.0, true);
        sets.addProduct(setNumberOne);
        sets.addProduct(setNumberTwo);
        peterMenu.addCategory(rolls);
        peterMenu.addCategory(sets);

        saveMenu(moscowMenu);
        saveMenu(peterMenu);
    }

    public User getUserByTelegramId(Integer telegramId) {
        if (telegramId == null) return null;

        return USERS_BY_TELEGRAM_ID.get(telegramId);
    }

    public User getAdmin(int userId) {
        return ADMINS_BY_TELEGRAM_ID.get(userId);
    }

    public Collection<User> getAdmins() {
        return Collections.unmodifiableCollection(ADMINS_BY_TELEGRAM_ID.values());
    }

    public void saveUser(@NotNull User user) {
        // todo: merge if already signed up with digits
        if (user == null) return;

        if (StringUtils.isEmpty(user.getPhoneNumber())) {
            throw new Error("User phone number must be provided");
        }

        USERS_BY_TELEGRAM_ID.put(user.getTelegramId(), user);

        if (user.getRole().equals(Role.ADMIN) ||
                user.getRole().equals(Role.MODERATOR)) {
            saveAdmin(user);
        }
    }

    private void saveAdmin(@NotNull User admin) {
        if (admin == null) return;

        ADMINS_BY_TELEGRAM_ID.put(admin.getTelegramId(), admin);
    }

    public int getMenusCount() {
        return MENUS_BY_ID.size();
    }

    public List<Menu> getMenus() {
        return Collections.unmodifiableList(new ArrayList<>(MENUS_BY_ID.values()));
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(USERS_BY_TELEGRAM_ID.values());
    }

    public Collection<DeliveryZone> getDeliveryZones() {
        return Collections.unmodifiableCollection(DELIVERY_ZONES_BY_ID.values());
    }

    public Collection<Locality> getCities() {
        return Collections.unmodifiableCollection(LOCALITIES_BY_ID.values());
    }

    public Locality getCityId(int cityId) {
        return LOCALITIES_BY_ID.get(cityId);
    }

    public Collection<MenuCategory> getMenuCategories(int menuId) {
        return CATEGORIES_BY_ID.values().stream().filter(c ->
                c.getMenu().getId() == menuId).collect(Collectors.toList());
    }

    public List<Product> getPublishedProducts() {
        return Collections.unmodifiableList(new ArrayList<>(PRODUCTS_BY_ID.values()));
    }

    public boolean isLocalityAlreadyBoundToMenu(Locality locality) {
        return BOUND_LOCALITIES.contains(locality.getCoordinate());
    }

    public void saveLocality(Locality locality) {
        LOCALITIES_BY_ID.put(locality.getId(), locality);
        BOUND_LOCALITIES.add(locality.getCoordinate());
    }

    // todo: create entity constraints and storage have to validate it
    public void saveMenu(Menu menu) {
        MENUS_BY_ID.put(menu.getId(), menu);
        // if selectCategoryKeyboard is already bound to locality
        saveLocality(menu.getLocality());
        menu.getCategories().forEach(this::saveCategory);
    }

    public void saveCategory(MenuCategory category) {
        CATEGORIES_BY_ID.put(category.getId(), category);

        if (category.hasProducts()) category.getProducts().forEach(this::saveProduct);
    }

    public MenuCategory getMenuCategory(int id) {
        return CATEGORIES_BY_ID.get(id);
    }

    public void saveProduct(Product product) {
        PRODUCTS_BY_ID.put(product.getId(), product);
    }

    public Menu getMenu(int id) {
        return MENUS_BY_ID.get(id);
    }

    public boolean hasPublishedProducts() {
        if (!hasProducts()) return false;

        for (Product product : getPublishedProducts()) {
            if (product.isPublished()) return true;
        }

        return false;
    }

    public boolean hasPublishedProducts(int menuId) {
        if (!hasProducts()) return false;

        for (Product product : getPublishedProducts()) {
            if (product.getCategory().getMenu().getId() == menuId && product.isPublished())
                return true;
        }

        return false;
    }

    public boolean hasProducts() {
        return !getPublishedProducts().isEmpty();
    }

    public double getLastNDaysRevenue(int days) {
        return 0;
    }

    public int getLastNDaysOrdersCount(int days) {
        return 0;
    }

    public double getTodayRevenue() {
        return 0;
    }

    public int getTodayOrdersCount() {
        return 0;
    }

    public double getYesterdayRevenue() {
        return 0;
    }

    public int getYesterdayOrdersCount() {
        return 0;
    }

    public List<Menu> getMenusWithPublishedProducts() {
        final List<Menu> result = Lists.newArrayList();
        final List<Menu> menus = getMenus();

        for (Menu menu : menus) {
            if (hasPublishedProducts(menu.getId())) {
                result.add(menu);
            }
        }

        return result;
    }

    public List<Product> getPublishedProducts(int categoryId, int cursor, int count) {
        if (cursor < 0 || count < 1) return null;

        final List<Product> products = getCategoryPublishedProducts(categoryId);

        final int from = Math.min(products.size(), cursor);
        final int to = Math.min(products.size(), cursor + count);

        return products.subList(from, to);
    }

    public List<Product> getCategoryPublishedProducts(int categoryId) {
        final MenuCategory menuCategory = getMenuCategory(categoryId);

        return menuCategory.getProducts().stream()
                .filter(Product::isPublished).collect(Collectors.toList());
    }

    public int getCategoryPublishedProductsCount(int categoryId) {
        return getCategoryPublishedProducts(categoryId).size();
    }

    public Product getProduct(int productId) {
        return PRODUCTS_BY_ID.get(productId);
    }

    public void deleteMenu(Menu menu) {
        menu.getCategories().forEach(this::deleteCategory);

        MENUS_BY_ID.remove(menu.getId());
        DELETED_MENUS_BY_ID.put(menu.getId(), menu);
        BOUND_LOCALITIES.remove(menu.getLocality().getCoordinate());

        USERS_BY_TELEGRAM_ID.entrySet().forEach(e -> e.getValue().setSelectedMenu(null));
    }

    public Menu recoverMenu(int menuId) {
        final Menu menu = DELETED_MENUS_BY_ID.get(menuId);

        if (menu == null) throw new IndexOutOfBoundsException();// todo: handle gently

        recoverMenu(menu);

        return menu;
    }

    private void recoverMenu(Menu menu) {
        menu.getCategories().forEach(this::recoverCategory);

        DELETED_MENUS_BY_ID.remove(menu.getId());
        MENUS_BY_ID.put(menu.getId(), menu);
        BOUND_LOCALITIES.add(menu.getLocality().getCoordinate());
    }

    public void deleteCategory(MenuCategory category) {
        category.getProducts().forEach(this::deleteProduct);

        CATEGORIES_BY_ID.remove(category.getId());
        DELETED_CATEGORIES_BY_ID.put(category.getId(), category);
    }

    public MenuCategory recoverCategory(int categoryId) {
        MenuCategory category = DELETED_CATEGORIES_BY_ID.get(categoryId);

        if (category != null) recoverCategory(category);

        return category;
    }

    private void recoverCategory(MenuCategory category) {
        if (category == null) return;

        final Menu menu = category.getMenu();

        category.getProducts().forEach(this::recoverProduct);

        DELETED_CATEGORIES_BY_ID.remove(category.getId());
        CATEGORIES_BY_ID.put(category.getId(), category);

        menu.addCategory(category);
    }

    public void deleteProduct(Product product) {
        PRODUCTS_BY_ID.remove(product.getId());
        DELETED_PRODUCTS_BY_ID.put(product.getId(), product);
    }

    public Product recoverProduct(int productId) {
        Product product = DELETED_PRODUCTS_BY_ID.get(productId);

        if(product != null) recoverProduct(product);

        return product;
    }

    private void recoverProduct(Product product) {
        if(product == null) return;

        DELETED_PRODUCTS_BY_ID.remove(product.getId());
        PRODUCTS_BY_ID.put(product.getId(), product);

        product.getCategory().addProduct(product);
    }
}
