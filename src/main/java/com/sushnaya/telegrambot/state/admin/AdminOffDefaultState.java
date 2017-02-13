package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.Command;
import com.sushnaya.telegrambot.command.admin.*;
import com.sushnaya.telegrambot.command.user.ShowHelp;
import com.sushnaya.telegrambot.command.user.Start;
import com.sushnaya.telegrambot.state.user.DefaultState;
import org.telegram.telegrambots.api.objects.Update;

public class AdminOffDefaultState extends DefaultState {

//    private final Command switchAdminModeOnCommand = createSwitchAdminModeOnCommand();

    public AdminOffDefaultState(SushnayaBot bot) {
        super(bot);
    }

    protected Command getCommand(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            switch (parseCommandUri(update.getMessage().getText())) {
//                case ADMIN_ON_URI:
//                    return switchAdminModeOnCommand;
//                // todo: ignore self command
//            }
//        }

        return super.getCommand(update);
    }

    protected Start createStartCommand() {
        return new Start(bot);
    }

    protected ShowHelp createShowHelpCommand() {
        return new AdminOffShowHelp(bot);
    }
}
