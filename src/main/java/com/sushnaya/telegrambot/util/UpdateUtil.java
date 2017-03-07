package com.sushnaya.telegrambot.util;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.Comparator;
import java.util.List;

public class UpdateUtil {
    private static final NullUser NULL_USER = new NullUser();

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
        if (update.hasInlineQuery()) {
            return update.getInlineQuery().getFrom();

        } else if (update.hasEditedMessage()) {
            return update.getEditedMessage().getFrom();

        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();

        } else if (update.hasMessage()) {
            return update.getMessage().getFrom();
        }

        return NULL_USER;
    }

    public static int getTelegramUserId(Update update) {
        return getFrom(update).getId();
    }

    public static long getChatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() : update.hasEditedMessage() ?
                update.getEditedMessage().getChatId() : update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage().getChatId() : -1;
    }

    public static int getMessageId(Update update) {
        return getMessage(update).getMessageId();
    }

    public static Message getMessage(Update update) {
        return update.hasMessage() ? update.getMessage() :
                update.getCallbackQuery().getMessage();
    }

    private static final class NullUser extends User {
        @Override
        public Integer getId() {
            return -1;
        }
    }
}
