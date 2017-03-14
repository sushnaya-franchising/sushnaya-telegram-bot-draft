package com.sushnaya.telegrambot.admin.updatehandler;

import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.SushnayaBotUpdateHandler;
import org.telegram.telegrambots.api.objects.Update;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.CommandUriParamType.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.*;

public class EditProductHandler extends SushnayaBotUpdateHandler {

    public EditProductHandler(SushnayaBot bot) {
        super(bot);
    }

    @Override
    public void handle(Update update) {
        final ByteBuffer payload = Command.parseCommandUriByteBufferPayload(update);

        if (payload == null || payload.remaining() < 5) {
            askMenuToEditProductIn(update);
            return;
        }

        Byte paramType = payload.get();

        if (paramType == MENU_ID_PARAM) {
            final Menu menu = getMenu(payload);
            askCategoryToEditProductIn(update, menu);

        } else if (paramType == CATEGORY_ID_PARAM) {
            final MenuCategory category = getCategory(payload);
            askProductToEdit(update, category);

        } else if (paramType == PRODUCT_ID_PARAM) {
            final Product product = getProduct(payload);
            editProduct(update, product);

        } else {
            bot.handleUnknownCommand(update);
        }
    }

    private Menu getMenu(ByteBuffer payload) {
        final int menuId = payload.getInt();

        return bot.getDataStorage().getMenu(menuId);
    }

    private MenuCategory getCategory(ByteBuffer payload) {
        final int categoryId = payload.getInt();

        return bot.getDataStorage().getMenuCategory(categoryId);
    }

    private Product getProduct(ByteBuffer payload) {
        final int productId = payload.getInt();

        return bot.getDataStorage().getProduct(productId);
    }

    private void askMenuToEditProductIn(Update update) {
        List<Menu> menusWithProducts = bot.getDataStorage().getMenus()
                .stream().filter(m -> !filterCategoriesWithProducts(m.getCategories()).isEmpty())
                .collect(Collectors.toList());

        if (menusWithProducts == null || menusWithProducts.isEmpty()) {
            bot.say(update, MESSAGES.noProductWasCreatedToEdit(CREATE_PRODUCT), true);

        } else if (menusWithProducts.size() == 1) {
            askCategoryToEditProductIn(update, menusWithProducts.get(0));

        } else {
            bot.answer(update, MESSAGES.selectMenuToEditProductIn(), selectMenuKeyboard(menusWithProducts,
                    m -> buildCommandUri(EDIT_PRODUCT, MENU_ID_PARAM, m.getId())
            ));
        }
    }

    private List<MenuCategory> filterCategoriesWithProducts(List<MenuCategory> categories) {
        return categories.stream().filter(c -> !c.getProducts().isEmpty())
                .collect(Collectors.toList());
    }

    private void askCategoryToEditProductIn(Update update, Menu menu) {
        final List<MenuCategory> categoriesWithProducts =
                filterCategoriesWithProducts(menu.getCategories());

        if (categoriesWithProducts == null || categoriesWithProducts.isEmpty()) {
            bot.say(update, MESSAGES.menuContainsNoCategoryWithProducts(), true);

        } else if (categoriesWithProducts.size() == 1) {
            askProductToEdit(update, categoriesWithProducts.get(0));

        } else {
            bot.answer(update, MESSAGES.selectCategoryToEditProductIn(),
                    selectCategoryKeyboard(categoriesWithProducts,
                            c -> buildCommandUri(EDIT_PRODUCT, CATEGORY_ID_PARAM, c.getId())));
        }
    }

    private void askProductToEdit(Update update, MenuCategory category) {
        final List<Product> products = category.getProducts();

        if (products == null || products.isEmpty()) {
            bot.say(update, MESSAGES.categoryDoesNotContainProducts(), true);

        } else if (products.size() == 1) {
            editProduct(update, products.get(0));

        } else {
            bot.answer(update, MESSAGES.selectProductToEdit(),
                    selectProductKeyboard(products, p -> buildCommandUri(EDIT_PRODUCT,
                            PRODUCT_ID_PARAM, p.getId())));
            // todo: reveal products list as message if products count is greater than 14
        }
    }

    protected void editProduct(Update update, Product product) {
        bot.answer(update, MESSAGES.productSettings(product),
                bot.getAdminKeyboardFactory().editProduct(product));
    }
}
