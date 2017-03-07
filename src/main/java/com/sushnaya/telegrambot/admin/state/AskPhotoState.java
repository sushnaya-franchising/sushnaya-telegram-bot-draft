package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

import java.util.function.BiConsumer;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.UpdateUtil.getPhoto;
import static com.sushnaya.telegrambot.util.UpdateUtil.isPhotoMessage;

public class AskPhotoState extends AdminBotDialogState<String> {
    public static final String UNKNOWN_UPDATE_ERROR_MESSAGE = MESSAGES.inquirePhotoMessage();
    public static final String GET_FILEPATH_ERROR = MESSAGES.gettingPhotoFilepathError();

    public AskPhotoState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, String> ok, BiConsumer<Update, String> ko) {
        if (isPhotoMessage(update)) {
            try {
                ok.accept(update, getPhoto(update).getFileId());

            } catch (Exception e) {
                // todo: log exceptions throughout the project
                ko.accept(update, GET_FILEPATH_ERROR);
            }
        } else {
            ko.accept(update, UNKNOWN_UPDATE_ERROR_MESSAGE);
        }
    }
}
