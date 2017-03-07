package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.state.AskCommandState;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectCategoryKeyboard;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.selectMenuKeyboard;


public class CreateProductInMenuHandler extends CreateProductInCategoryHandler {
    private AskCommandState menuStep;
    private AskCommandState categoryStep;

    public CreateProductInMenuHandler(SushnayaBot bot) {
        super(bot);
    }

    private AskCommandState ensureMenuStep() {
        return menuStep == null ? menuStep = (AskCommandState) new AskCommandState(bot)
                .setDefaultMessage(MESSAGES.selectMenuForProductCreation())
                .onCancel(this::cancelProductCreation) : menuStep;
    }

    private AskCommandState ensureCategoryStep() {
        return categoryStep == null ? categoryStep = (AskCommandState) new AskCommandState(bot)
                .setDefaultMessage(MESSAGES.selectCategoryForProductCreation())
                .onCancel(this::cancelProductCreation) : categoryStep;
    }

    @Override
    public void handle(Update update) {
        createProductInMenu(update, getMenu(update));
    }

    private Menu getMenu(Update update) {
        final Integer menuId = Command.parseCommandUriIntPayload(update);

        return bot.getDataStorage().getMenu(menuId);
    }

    private void createProductInMenu(Update update, Menu menu) {
        askCategoryToCreateProductIn(update, menu);
    }

    private void askCategoryToCreateProductIn(Update update, Menu menu) {
        if (menu == null) {
            askMenuToCreateProductIn(update);
            return;
        }

        final List<MenuCategory> categories = menu.getMenuCategories();

        if (categories == null || categories.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireCategoryCreation(CREATE_CATEGORY),
                    true);

        } else if (categories.size() == 1) {
            createProductInCategory(update, categories.get(0));

        } else {
            ensureCategoryStep().ask(update, selectCategoryKeyboard(categories,
                    c -> buildCommandUri(CREATE_PRODUCT_IN_CATEGORY, c.getId())
            ));
        }
    }

    private void askMenuToCreateProductIn(Update update) {
        final List<Menu> menus = bot.getDataStorage().getMenus();

        if (menus == null || menus.isEmpty()) {
            bot.say(update, MESSAGES.productCreationInquireMenuCreation(CREATE_MENU),
                    true);

        } else if (menus.size() == 1) {
            createProductInMenu(update, menus.get(0));

        } else {
            ensureMenuStep().ask(update, selectMenuKeyboard(menus,
                    m -> buildCommandUri(CREATE_PRODUCT, m.getId())
            ));
        }
    }
}
