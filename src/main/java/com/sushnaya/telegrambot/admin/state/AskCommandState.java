package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.function.BiConsumer;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;

public class AskCommandState extends AdminBotDialogState<Void> {
    public static final String UNKNOWN_UPDATE_ERROR_MESSAGE = MESSAGES.askCommandUnknownMessage();

    public AskCommandState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Void> ok, BiConsumer<Update, String> ko) {
        ko.accept(update, UNKNOWN_UPDATE_ERROR_MESSAGE);
    }
}
