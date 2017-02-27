package com.sushnaya.telegrambot.state.admin;

import com.sun.xml.internal.xsom.impl.parser.Messages;
import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.function.BiConsumer;

import static com.sushnaya.telegrambot.util.UpdateUtil.isTextMessage;

public class AskDoubleValueState extends AdminBotDialogState<Double> {
    public static final String UNKNOWN_UPDATE_ERROR_MESSAGE = MESSAGES.inquireTextMessage();
    public static final String PARSING_ERROR = MESSAGES.doubleValueFormatError();

    public AskDoubleValueState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Double> ok,
                                BiConsumer<Update, String> ko) {
        if (isTextMessage(update)) {
            try {
                final String text = update.getMessage().getText().replace(',', '.');

                ok.accept(update, Double.parseDouble(text));

            } catch (Exception ex) {
                ko.accept(update, PARSING_ERROR);
            }

        } else {
            ko.accept(update, UNKNOWN_UPDATE_ERROR_MESSAGE);
        }
    }
}
