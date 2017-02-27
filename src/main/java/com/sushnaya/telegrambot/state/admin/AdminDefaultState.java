package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.telegrambot.*;
import com.sushnaya.telegrambot.dialog.MenuCreationDialog;
import com.sushnaya.telegrambot.state.user.UserDefaultState;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.HELP;
import static com.sushnaya.telegrambot.util.UpdateUtil.getUserId;

public class AdminDefaultState extends UserDefaultState {
    public static final AdminKeyboardMarkupFactory STARTUP_MARKUP_FACTORY =
            new StartupAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory DEFAULT_MARKUP_FACTORY =
            new DefaultAdminKeyboardMarkupFactory();
    public static final AdminKeyboardMarkupFactory NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY =
            new NoPublishedProductsAdminKeyboardMarkupFactory();
    private MenuCreationDialog menuCreationDialog;

    public AdminDefaultState(SushnayaBot bot) {
        super(bot);
    }

    public AdminKeyboardMarkupFactory getKeyboardMarkupFactory() {
        return !bot.hasProducts() ? STARTUP_MARKUP_FACTORY :
                !bot.hasPublishedProducts() ? NO_PUBLISHED_PRODUCTS_MARKUP_FACTORY :
                        DEFAULT_MARKUP_FACTORY;
    }

    @Override
    protected void handleUpdate(Update update) {
        switch (Command.parse(update)) {
            case ADMIN_DASHBOARD:
                if (isCancellable()) cancel(update);
                dashboard(update);
                break;
            case CREATE_MENU:
                if (isCancellable()) cancel(update);
                createMenu(update);
                break;
            case EDIT_MENU:
                if (isCancellable()) cancel(update);
                editMenu(update);
                break;
            default:
                super.handleUpdate(update);
                break;
        }
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
    public void home(Update update) {
        if (!bot.hasPublishedProducts()) {
            dashboard(update);

        } else {
            super.home(update);
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

    protected String getDashboardMessageText() {
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

    private void createMenu(Update update) {
        ensureMenuCreationDialog().ask(update).then((u, menu) -> {
            bot.setAdminDefaultState(u);
            bot.getDataStorage().saveMenu(menu);
            // todo: ask add one more product or return to administration
        }).onCancel(this::cancelMenuCreation);
    }

    private MenuCreationDialog ensureMenuCreationDialog() {
        return menuCreationDialog != null ? menuCreationDialog :
                (menuCreationDialog = new MenuCreationDialog(bot, getKeyboardMarkupFactory()));
    }

    private void cancelMenuCreation(Update u) {
        bot.setAdminDefaultState(u);
        bot.say(u, MESSAGES.menuCreationIsCancelled(HELP));
    }
}
