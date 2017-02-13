package com.sushnaya.telegrambot.state;

import com.sushnaya.telegrambot.SushnayaBot;
import org.telegram.telegrambots.api.objects.Update;

public abstract class SushnayaBotState {
    protected final SushnayaBot bot;

    public SushnayaBotState(SushnayaBot bot) {
        this.bot = bot;
    }

    public abstract void process(Update update);
}
