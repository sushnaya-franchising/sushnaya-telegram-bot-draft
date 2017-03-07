package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.admin.state.dialog.CategoryCreationDialog;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.twoColumnsInlineKeyboard;

public class CreateCategoryHandler extends SushnayaBotUpdateHandler {
    private CategoryCreationDialog categoryCreationDialog;

    public CreateCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        Menu menu = getMenu(update);

        createCategory(update, menu);
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
                    bot.getAdminKeyboardFactory().categoryCreationFurtherCommands(menu, category));
        }).onCancel(this::cancelCategoryCreation);
    }

    private Menu getMenu(Update update) {
        final Integer menuId = Command.parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
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
                    selectMenuToCreateCategoryKeyboard(menus));
        }
    }

    private CategoryCreationDialog ensureCategoryCreationDialog() {
        return categoryCreationDialog != null ? categoryCreationDialog :
                (categoryCreationDialog = new CategoryCreationDialog(bot));
    }

    private InlineKeyboardMarkup selectMenuToCreateCategoryKeyboard(List<Menu> menus) {
        final Function<Menu, String> buttonTextProvider = Menu::getLocalityName;
        final Function<Menu, String> callbackDataProvider =
                m -> buildCommandUri(CREATE_CATEGORY, m.getId());

        return twoColumnsInlineKeyboard(menus, buttonTextProvider, callbackDataProvider);
    }
}
