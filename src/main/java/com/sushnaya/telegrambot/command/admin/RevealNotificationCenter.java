package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class RevealNotificationCenter extends ReplyCommand{
    public static final String URI = "/notify";
    public static final String BUTTON_LABEL = "\uD83D\uDCE3 Оповещение";

    public RevealNotificationCenter(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
