package com.sushnaya.telegrambot.state.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.function.BiConsumer;

import static com.sushnaya.telegrambot.util.UpdateUtil.isTextMessage;

public class AskTextState extends AdminBotDialogState<String> {
    public static final String UNKNOWN_UPDATE_ERROR_MESSAGE = MESSAGES.inquireTextMessage();

    public AskTextState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, String> ok, BiConsumer<Update, String> ko) {
        if (isTextMessage(update)) {
            ok.accept(update, update.getMessage().getText());

        } else {
            ko.accept(update, UNKNOWN_UPDATE_ERROR_MESSAGE);
        }
    }
}
