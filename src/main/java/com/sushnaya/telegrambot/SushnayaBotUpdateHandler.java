package com.sushnaya.telegrambot;

public abstract class SushnayaBotUpdateHandler implements UpdateHandler {
    protected final SushnayaBot bot;

    public SushnayaBotUpdateHandler(SushnayaBot bot) {
        this.bot = bot;
    }

}
