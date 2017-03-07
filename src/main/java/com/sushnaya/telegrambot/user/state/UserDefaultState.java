package com.sushnaya.telegrambot.user.state;

import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.BotState;
import com.sushnaya.telegrambot.DefaultCancelHandler;
import com.sushnaya.telegrambot.user.updatehandler.UserHelpHandler;
import com.sushnaya.telegrambot.user.updatehandler.UserMenuHandler;
import com.sushnaya.telegrambot.user.updatehandler.UserNextProductInCategoryHandler;
import com.sushnaya.telegrambot.user.updatehandler.UserStartHandler;

public class UserDefaultState extends BotState {
    public UserDefaultState(SushnayaBot bot) {
        super(bot);

        registerUpdateHandler(Command.START, new UserStartHandler(bot));
        registerUpdateHandler(Command.MENU, new UserMenuHandler(bot));
        registerUpdateHandler(Command.NEXT_PRODUCT_IN_CATEGORY, new UserNextProductInCategoryHandler(bot));
        registerUpdateHandler(Command.CANCEL, new DefaultCancelHandler(bot));
        registerUpdateHandler(Command.HELP, new UserHelpHandler(bot));
    }
}
