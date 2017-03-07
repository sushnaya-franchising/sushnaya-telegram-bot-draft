package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.BotState;
import com.sushnaya.telegrambot.DefaultCancelHandler;
import com.sushnaya.telegrambot.admin.updatehandler.*;
import com.sushnaya.telegrambot.user.updatehandler.UserNextProductInCategoryHandler;

import static com.sushnaya.telegrambot.Command.*;

public class AdminDefaultState extends BotState {
    public AdminDefaultState(SushnayaBot bot) {
        super(bot);

        registerUpdateHandler(START, new AdminStartHandler(bot));
        registerUpdateHandler(MENU, new AdminMenuHandler(bot));
        registerUpdateHandler(NEXT_PRODUCT_IN_CATEGORY, new UserNextProductInCategoryHandler(bot));
        registerUpdateHandler(CANCEL, new DefaultCancelHandler(bot));
        registerUpdateHandler(HELP, new AdminHelpHandler(bot));
        registerUpdateHandler(ADMIN_DASHBOARD, new DashboardHandler(bot));
        registerUpdateHandler(CREATE_MENU, new CreateMenuHandler(bot));
        registerUpdateHandler(CREATE_CATEGORY, new CreateCategoryHandler(bot));
        registerUpdateHandler(CREATE_PRODUCT, new CreateProductInMenuHandler(bot));
        registerUpdateHandler(CREATE_PRODUCT_IN_CATEGORY, new CreateProductInCategoryHandler(bot));
    }
}
