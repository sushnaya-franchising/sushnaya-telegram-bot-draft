package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.telegrambot.BotState;
import com.sushnaya.telegrambot.DefaultCancelHandler;
import com.sushnaya.telegrambot.SushnayaBot;
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
        registerUpdateHandler(CREATE_PRODUCT, new CreateProductHandler(bot));
        registerUpdateHandler(EDIT_MENU, new EditMenuHandler(bot));
        registerUpdateHandler(EDIT_CATEGORY, new EditCategoryHandler(bot));
        registerUpdateHandler(EDIT_PRODUCT, new EditProductHandler(bot));
        registerUpdateHandler(DELETE_MENU, new DeleteMenuHandler(bot));
        registerUpdateHandler(DELETE_CATEGORY, new DeleteCategoryHandler(bot));
        registerUpdateHandler(DELETE_PRODUCT, new DeleteProductHandler(bot));
        registerUpdateHandler(RESTORE_MENU, new RestoreMenuHandler(bot));
        registerUpdateHandler(RESTORE_CATEGORY, new RestoreCategoryHandler(bot));
        registerUpdateHandler(RESTORE_PRODUCT, new RestoreProductHandler(bot));
    }
}
