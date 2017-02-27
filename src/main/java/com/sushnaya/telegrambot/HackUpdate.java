package com.sushnaya.telegrambot;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

public class HackUpdate extends Update {

    private CallbackQuery callbackQuery;

    public HackUpdate(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public CallbackQuery getCallbackQuery() {
        return callbackQuery;
    }

    public void setCallbackQuery(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public boolean hasCallbackQuery() {
        return callbackQuery != null;
    }
}
