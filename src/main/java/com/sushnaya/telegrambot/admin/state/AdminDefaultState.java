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
        registerUpdateHandler(ADMIN_DASHBOARD, new AdminDashboardHandler(bot));

        registerUpdateHandler(CREATE_MENU, new CreateMenuHandler(bot));
        registerUpdateHandler(CREATE_CATEGORY, new CreateCategoryHandler(bot));
        registerUpdateHandler(CREATE_PRODUCT, new CreateProductHandler(bot));

        registerUpdateHandler(EDIT_MENU, new EditMenuHandler(bot));
        registerUpdateHandler(EDIT_CATEGORY, new EditCategoryHandler(bot));
        registerUpdateHandler(EDIT_PRODUCT, new EditProductHandler(bot));

        registerUpdateHandler(DELETE_MENU, new DeleteMenuHandler(bot));
        registerUpdateHandler(DELETE_CATEGORY, new DeleteCategoryHandler(bot));
        registerUpdateHandler(DELETE_PRODUCT, new DeleteProductHandler(bot));

        registerUpdateHandler(RECOVER_MENU, new RecoverMenuHandler(bot));
        registerUpdateHandler(RECOVER_CATEGORY, new RecoverCategoryHandler(bot));
        registerUpdateHandler(RECOVER_PRODUCT, new RecoverProductHandler(bot));

        registerUpdateHandler(EDIT_MENU_DELIVERY, new EditMenuDeliveryHandler(bot));
        registerUpdateHandler(EDIT_MENU_AFFILIATES, new EditMenuAffiliatesHandler(bot));
        registerUpdateHandler(CLONE_MENU, new CloneMenuHandler(bot));

        registerUpdateHandler(SET_CATEGORY_NAME, new SetCategoryNameHandler(bot));
        registerUpdateHandler(SET_CATEGORY_SUBHEADING, new SetCategorySubheadingHandler(bot));
        registerUpdateHandler(SET_CATEGORY_PHOTO, new SetCategoryPhotoHandler(bot));

        registerUpdateHandler(SET_PRODUCT_NAME, new SetProductNameHandler(bot));
        registerUpdateHandler(SET_PRODUCT_PRICE, new SetProductPriceHandler(bot));
        registerUpdateHandler(SET_PRODUCT_PHOTO, new SetProductPhotoHandler(bot));
        registerUpdateHandler(SET_PRODUCT_SUBHEADING, new SetProductSubheadingHandler(bot));
        registerUpdateHandler(SET_PRODUCT_DESCRIPTION, new SetProductDescriptionHandler(bot));
        registerUpdateHandler(EDIT_PRODUCT_MODIFICATIONS, new EditProductModificationsHandler(bot));
        registerUpdateHandler(EDIT_PRODUCT_OPTIONS, new EditProductOptionsHandler(bot));
        registerUpdateHandler(HIDE_PRODUCT, new HideProductHandler(bot));
        registerUpdateHandler(PUBLISH_PRODUCT, new PublishProductHandler(bot));
    }
}
