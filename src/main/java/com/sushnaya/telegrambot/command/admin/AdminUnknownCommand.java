package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.user.UnknownCommand;
import com.sushnaya.telegrambot.state.admin.AdminOnDefaultState;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class AdminUnknownCommand extends UnknownCommand {

    public AdminUnknownCommand(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected SendMessage createSendMessage(Update update) {
        return super.createSendMessage(update)
                .setReplyMarkup(AdminOnDefaultState.KEYBOARD_MARKUP);
    }
}
