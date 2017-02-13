package com.sushnaya.telegrambot.command.user;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class UnknownCommand extends ReplyCommand {
    public UnknownCommand(SushnayaBot bot) {
        super(bot);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN)
                .setText("Не знаю, что ответить. /help");
    }

    public String getUri() {
        throw new UnsupportedOperationException();
    }

    public String getButtonLabel() {
        throw new UnsupportedOperationException();
    }

}
