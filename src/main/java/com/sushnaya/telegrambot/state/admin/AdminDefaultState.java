package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.*;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.dialog.CategoryCreationDialog;
import com.sushnaya.telegrambot.dialog.MenuCreationDialog;
import com.sushnaya.telegrambot.dialog.ProductCreationDialog;
import com.sushnaya.telegrambot.state.user.UserDefaultState;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;

public class AdminDefaultState extends UserDefaultState {
    public static final AdminKeyboardMarkupFactory STARTUP_MARKUP_FACTORY =
            new StartupAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory DEFAULT_MARKUP_FACTORY =
            new DefaultAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY =
            new NoPublishedProductsAdminKeyboardMarkupFactory();
    private MenuCreationDialog menuCreationDialog;
    private CategoryCreationDialog categoryCreationDialog;
    private ProductCreationDialog productCreationDialog;

    public AdminDefaultState(SushnayaBot bot) {
        super(bot);
    }

    public AdminKeyboardMarkupFactory getKeyboardMarkupFactory() {
        return !bot.hasProducts() ? STARTUP_MARKUP_FACTORY :
                !bot.hasPublishedProducts() ? NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY :
                        DEFAULT_MARKUP_FACTORY;
    }

    @Override
    public boolean handle(Update update) {
        if (super.handle(update)) return true;

        switch (Command.parseCommand(update)) {
            case ADMIN_DASHBOARD:
            case BACK_TO_DASHBOARD:
                dashboard(update);
                return true;
            case CREATE_MENU:
                createMenu(update);
                return true;
            case CREATE_CATEGORY:
                createCategory(update, getMenu(update));
                return true;
            case CREATE_PRODUCT:
                createProduct(update);
                return true;
            case CREATE_PRODUCT_IN_MENU:
                createProductInMenu(update, getMenu(update));
                return true;
            case CREATE_PRODUCT_IN_CATEGORY:
                createProductInCategory(update, getCategory(update));
                return true;
            case EDIT_MENU:
                editMenu(update);
                return true;
            case CLOSE_DASHBOARD:
                menu(update);
                return true;
            default:
                return false;
        }
    }

    private Menu getMenu(Update update) {
        final Integer menuId = Command.parseId(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private MenuCategory getCategory(Update update) {
        final Integer categoryId = Command.parseId(update);

        return bot.getDataStorage().getMenuCategory(categoryId);
    }

    @Override
    protected String getGreetingMessageText() {
        return super.getGreetingMessageText() + "\n\n" +
                MESSAGES.administrationAllowed();
    }

    @Override
    protected String getHelpMessageText() {
        return super.getHelpMessageText() + "\n\n" +
                MESSAGES.administrationAllowed();
    }

    @Override
    public void menu(Update update) {
        if (!bot.hasPublishedProducts()) {
            dashboard(update);

        } else {
            super.menu(update);
        }
    }

    public void dashboard(Update update) {
        String message = getDashboardMessageText();

        if (update.hasCallbackQuery()) {
            bot.edit(update, message, getKeyboardMarkupFactory().dashboardMarkup());

        } else {
            bot.say(update, message, getKeyboardMarkupFactory().dashboardMarkup());
        }
    }

    private String getDashboardMessageText() {
        if (!bot.hasProducts()) {
            return MESSAGES.dashboardNoProductsMessage();
        }

        if (!bot.hasPublishedProducts()) {
            return MESSAGES.dashboardNoPublishedProductsMessage();
        }

        // todo: optimize queries count
        return MESSAGES.dashboardDefaultMessage(
                bot.getDataStorage().getTodayRevenue(),
                bot.getDataStorage().getTodayOrdersCount(),
                bot.getDataStorage().getYesterdayRevenue(),
                bot.getDataStorage().getYesterdayOrdersCount(),
                bot.getDataStorage().getLastNDaysRevenue(7),
                bot.getDataStorage().getLastNDaysOrdersCount(7)
        );
    }

    public void editMenu(Update update) {
        throw new UnsupportedOperationException();
    }

    public void createMenu(Update update) {
        ensureMenuCreationDialog().ask(update).then((u, menu) -> {
            bot.setAdminDefaultState(u);
            bot.getDataStorage().saveMenu(menu);
            bot.say(u, MESSAGES.menuCreationIsSuccessful(menu), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForMenuCreation(),
                    getKeyboardMarkupFactory().menuCreationFurtherCommands(menu));
        }).onCancel(this::cancelMenuCreation);
    }

    private MenuCreationDialog ensureMenuCreationDialog() {
        return menuCreationDialog != null ? menuCreationDialog :
                (menuCreationDialog = new MenuCreationDialog(bot, getKeyboardMarkupFactory()));
    }

    private void cancelMenuCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_MENU) return;

        String message = command == CANCEL ? MESSAGES.menuCreationIsCancelled(HELP) :
                MESSAGES.menuCreationIsInterrupted(HELP);

        bot.say(u, message, true);
    }

    public void createCategory(Update update, Menu menu) {
        if (menu == null) {
            askMenuToCreateCategoryIn(update);
            return;
        }

        ensureCategoryCreationDialog().ask(update).then((u, category) -> {
            bot.setAdminDefaultState(u);

            menu.addCategory(category);
            bot.getDataStorage().saveMenu(menu);

            bot.say(u, MESSAGES.categoryCreationIsSuccessful(category), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForCategoryCreation(),
                    getKeyboardMarkupFactory().categoryCreationFurtherCommands(menu, category));
        }).onCancel(this::cancelCategoryCreation);
    }

    private void cancelCategoryCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_CATEGORY) return;

        String message = command == CANCEL ? MESSAGES.categoryCreationIsCancelled(HELP) :
                MESSAGES.categoryCreationIsInterrupted(HELP);

        bot.say(u, message, true);
    }

    private void askMenuToCreateCategoryIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.categoryCreationInquireMenuCreation(CREATE_MENU),
                    true);

        } else if (menus.size() == 1) {
            createCategory(update, menus.get(0));

        } else {
            bot.say(update, MESSAGES.selectMenuForCategoryCreation(),
                    getKeyboardMarkupFactory().menus(menus, CREATE_CATEGORY));
        }
    }

    private CategoryCreationDialog ensureCategoryCreationDialog() {
        return categoryCreationDialog != null ? categoryCreationDialog :
                (categoryCreationDialog = new CategoryCreationDialog(bot, getKeyboardMarkupFactory()));
    }

    private void createProduct(Update update) {
        createProductInMenu(update, null);
    }

    public void createProductInMenu(Update update, Menu menu) {
        askCategoryToCreateProductIn(update, menu);
    }

    private void askCategoryToCreateProductIn(Update update, Menu menu) {
        if (menu == null) {
            askMenuToCreateProductIn(update);
            return;
        }

        final List<MenuCategory> categories = menu.getMenuCategories();

        if (categories == null || categories.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireCategoryCreation(CREATE_CATEGORY),
                    true);

        } else if (categories.size() == 1) {
            createProductInCategory(update, categories.get(0));

        } else {
            bot.say(update, MESSAGES.selectCategoryForProductCreation(),
                    getKeyboardMarkupFactory().categories(categories, CREATE_PRODUCT_IN_CATEGORY));
        }
    }

    private void askMenuToCreateProductIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireMenuCreation(CREATE_MENU),
                    true);

        } else if (menus.size() == 1) {
            createProductInMenu(update, menus.get(0));

        } else {
            bot.say(update, MESSAGES.selectMenuForProductCreation(),
                    getKeyboardMarkupFactory().menus(menus, CREATE_PRODUCT_IN_MENU));
        }
    }

    public void createProductInCategory(Update update, MenuCategory category) {
        if (category == null) {
            createProduct(update);
            return;
        }

        ensureProductCreationDialog().ask(update).then((u, product) -> {
            bot.setAdminDefaultState(u);

            category.addProduct(product);
            bot.getDataStorage().saveCategory(category);

            bot.say(u, MESSAGES.productCreationIsSuccessful(product), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForProductCreation(),
                    getKeyboardMarkupFactory().productCreationFurtherCommands(category.getMenu(), category));
        }).onCancel(this::cancelProductCreation);
    }

    private ProductCreationDialog ensureProductCreationDialog() {
        return productCreationDialog != null ? productCreationDialog :
                (productCreationDialog = new ProductCreationDialog(bot, getKeyboardMarkupFactory()));
    }

    private void cancelProductCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_PRODUCT ||
                command == CREATE_PRODUCT_IN_CATEGORY ||
                command == CREATE_PRODUCT_IN_MENU) return;

        String message = command == CANCEL ? MESSAGES.productCreationIsCancelled(HELP) :
                MESSAGES.productCreationIsInterrupted(HELP);

        bot.say(u, message, true);
    }
}
