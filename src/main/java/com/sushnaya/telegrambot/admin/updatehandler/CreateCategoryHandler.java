package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.admin.state.dialog.CategoryCreationDialog;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;

public class CreateCategoryHandler extends SushnayaBotUpdateHandler {

    public CreateCategoryHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final Menu menu = getMenu(update);

        if (menu == null) {
            askMenuToCreateCategoryIn(update);

        } else {
            createCategory(update, menu);
        }
    }

    private Menu getMenu(Update update) {
        final Integer menuId = Command.parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private void askMenuToCreateCategoryIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.categoryCreationInquireMenuCreation(CREATE_MENU),
                    true);

        } else if (menus.size() == 1) {
            createCategory(update, menus.get(0));

        } else {
            bot.answer(update, MESSAGES.selectMenuForCategoryCreation(),
                    selectMenuKeyboard(menus, m -> buildCommandUri(CREATE_CATEGORY, m.getId())));
        }
    }

    private void createCategory(Update update, Menu menu) {
        // todo: refactor design to avoid new instance creation
        new CategoryCreationDialog(bot).ask(update).then((u, category) -> {
            bot.setAdminDefaultState(u);

            menu.addCategory(category);
            bot.getDataStorage().saveMenu(menu);

            bot.say(u, MESSAGES.categoryCreationIsSuccessful(category), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForCategoryCreation(),
                    bot.getAdminKeyboardFactory().categoryCreationFurtherCommands(menu, category));
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
}
