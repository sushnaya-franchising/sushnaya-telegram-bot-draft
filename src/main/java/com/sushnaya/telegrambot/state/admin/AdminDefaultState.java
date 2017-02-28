package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.*;
import com.sushnaya.telegrambot.dialog.CategoryCreationDialog;
import com.sushnaya.telegrambot.dialog.MenuCreationDialog;
import com.sushnaya.telegrambot.dialog.ProductCreationDialog;
import com.sushnaya.telegrambot.state.user.UserDefaultState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.CANCEL;
import static com.sushnaya.telegrambot.Command.HELP;

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
                startMenuCreationDialog(update);
                return true;
            case CREATE_PRODUCT_IN_CATEGORY:
                // todo: implement create product in category
                return false;
            case CREATE_PRODUCT_IN_MENU:
                // todo: implement create product in menu
                return false;
            case CREATE_CATEGORY:
                Menu menu = getMenu(update);

                if (menu != null) {
                    startCategoryCreationDialog(update, menu);
                    return true;

                } else {
                    // todo: implement category creation with menu selection at the beginning
                    // todo: cancel dialog state
                    return false;
                }

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

    public void startMenuCreationDialog(Update update) {
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
        String message = Command.parseCommand(u) == CANCEL ?
                MESSAGES.menuCreationIsCancelled(HELP) :
                MESSAGES.menuCreationIsInterrupted(HELP);

        bot.setAdminDefaultState(u);
        bot.say(u, message, true);
    }

    public void startCategoryCreationDialog(Update update, Menu menu) {
        ensureCategoryCreationDialog().ask(update).then((u, category) -> {
            menu.addCategory(category);
            bot.setAdminDefaultState(u);
            bot.getDataStorage().saveMenu(menu);
            bot.say(u, MESSAGES.categoryCreationIsSuccessful(category), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForMenuCreation(),
                    getKeyboardMarkupFactory().menuCreationFurtherCommands(menu, category));
        });
    }

    private CategoryCreationDialog ensureCategoryCreationDialog() {
        return categoryCreationDialog != null ? categoryCreationDialog :
                (categoryCreationDialog = new CategoryCreationDialog(bot, getKeyboardMarkupFactory()));
    }
}
