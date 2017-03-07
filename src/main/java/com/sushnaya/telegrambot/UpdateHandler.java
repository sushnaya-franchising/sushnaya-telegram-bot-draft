package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update);
}
