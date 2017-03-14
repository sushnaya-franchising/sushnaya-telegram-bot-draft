package com.sushnaya.telegrambot;

import com.google.common.collect.Maps;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Map;

public class BotState implements UpdateHandler {
    protected final SushnayaBot bot;
    private final SushnayaBotUpdateHandler unknownUpdateHandler;
    private final Map<Command, UpdateHandler> handlers = Maps.newHashMap();

    public BotState(SushnayaBot bot) {
        this.bot = bot;

        unknownUpdateHandler = new SushnayaBotUpdateHandler(bot) {
            @Override
            public void handle(Update update) {
                bot.handleUnknownCommand(update);
                // todo: tell about crash if it happened
            }
        };
    }

    protected void registerUpdateHandler(Command command, UpdateHandler handler) {
        handlers.put(command, handler);
    }

    public void handle(Update update) {
        sanitize(getUpdateHandler(update)).handle(update);
    }

    public void handle(Update update, Command command) {
        sanitize(getUpdateHandler(command)).handle(update);
    }

    private UpdateHandler sanitize(UpdateHandler updateHandler) {
        return updateHandler == null ? unknownUpdateHandler : updateHandler;
    }

    protected UpdateHandler getUpdateHandler(Update update) {
        return getUpdateHandler(parseCommand(update));
    }

    protected Command parseCommand(Update update) {
        return Command.parseCommand(update);
    }

    protected UpdateHandler getUpdateHandler(Command command) {
        return handlers.get(command);
    }

}
