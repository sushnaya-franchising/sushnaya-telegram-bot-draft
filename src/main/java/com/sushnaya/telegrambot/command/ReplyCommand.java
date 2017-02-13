package com.sushnaya.telegrambot.command;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public abstract class ReplyCommand extends Command {
    private Message sentMessage;

    public ReplyCommand(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        SendMessage message = createSendMessage(update);
        sendMessage(message);
    }

    protected void sendMessage(SendMessage message) {
        try {
            sentMessage = bot.sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected abstract SendMessage createSendMessage(Update update);

    public Message getSentMessage() {
        return sentMessage;
    }
}
