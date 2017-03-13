package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import com.sushnaya.telegrambot.admin.state.dialog.MenuCreationDialog;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class CreateMenuHandler extends SushnayaBotUpdateHandler {

    public CreateMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        // todo: refactor design to avoid new instance creation
        new MenuCreationDialog(bot).ask(update).then((u, menu) -> {
            bot.setAdminDefaultState(u);
            bot.getDataStorage().saveMenu(menu);
            bot.say(u, MESSAGES.menuCreationIsSuccessful(menu), true);
            bot.say(u, MESSAGES.proposeFurtherCommandsForMenuCreation(),
                    bot.getAdminKeyboardFactory().menuCreationFurtherCommands(menu));
        }).onCancel(this::cancelMenuCreation);
    }

    private void cancelMenuCreation(Update u) {
        bot.setAdminDefaultState(u);

        final Command command = Command.parseCommand(u);

        if (command == CREATE_MENU) return;

        String message = command == CANCEL ? MESSAGES.menuCreationIsCancelled() :
                MESSAGES.menuCreationIsInterrupted(HELP);

        bot.say(u, message, true);
    }
}
