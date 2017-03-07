package com.sushnaya.telegrambot.user.state;

import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.BotState;
import com.sushnaya.telegrambot.DefaultCancelHandler;
import com.sushnaya.telegrambot.UpdateHandler;
import com.sushnaya.telegrambot.user.updatehandler.SendContactHandler;
import com.sushnaya.telegrambot.user.updatehandler.UnregisteredUserHelpMessage;
import com.sushnaya.telegrambot.user.updatehandler.UnregisteredUserMenuHandler;
import com.sushnaya.telegrambot.user.updatehandler.UnregisteredUserStartHandler;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.Command.MENU;

public class UnregisteredUserState extends BotState {
    public final SendContactHandler sendContactHandler;

    public UnregisteredUserState(SushnayaBot bot) {
        super(bot);

        registerUpdateHandler(Command.START, new UnregisteredUserStartHandler(bot));
        registerUpdateHandler(MENU, new UnregisteredUserMenuHandler(bot));
        registerUpdateHandler(Command.HELP, new UnregisteredUserHelpMessage(bot));
        registerUpdateHandler(Command.CANCEL, new DefaultCancelHandler(bot));

        sendContactHandler = new SendContactHandler(bot);
    }

    @Override
    protected UpdateHandler getUpdateHandler(Update update) {
        if (update.hasMessage() && update.getMessage().getContact() != null) {
            return sendContactHandler;
        }

        return super.getUpdateHandler(update);
    }
}
