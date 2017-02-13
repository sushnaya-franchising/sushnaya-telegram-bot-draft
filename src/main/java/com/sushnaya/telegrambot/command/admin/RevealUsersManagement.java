package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class RevealUsersManagement extends ReplyCommand {


    public RevealUsersManagement(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
