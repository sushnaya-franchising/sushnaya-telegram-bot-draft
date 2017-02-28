package com.sushnaya.telegrambot.util;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.Comparator;
import java.util.List;

public class UpdateUtil {
    private UpdateUtil() {
    }

    public static boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public static boolean isPhotoMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    public static PhotoSize getPhoto(Update update) {
        List<PhotoSize> photos = update.getMessage().getPhoto();

        return photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null);
    }

    public static User getFrom(Update update) {
        User from = null;

        if (update.hasInlineQuery()) {
            from = update.getInlineQuery().getFrom();

        } else if (update.hasEditedMessage()) {
            from = update.getEditedMessage().getFrom();

        } else if (update.hasCallbackQuery()) {
            from = update.getCallbackQuery().getFrom();

        } else if (update.hasMessage()) {
            from = update.getMessage().getFrom();
        }

        return from;
    }

    public static Integer getTelegramUserId(Update update) {
        User from = getFrom(update);

        return from != null ? from.getId() : null;
    }

    public static Long getChatId(Update update) {
        return update.hasMessage() ? update.getMessage().getChatId() :
                update.hasEditedMessage() ? update.getEditedMessage().getChatId() :
                        update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() :
                                null;
    }

    public static int getMessageId(Update update) {
        return getMessage(update).getMessageId();
    }

    public static Message getMessage(Update update) {
        return update.hasMessage() ? update.getMessage() :
                update.getCallbackQuery().getMessage();
    }
}
