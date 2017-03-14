package com.sushnaya.telegrambot.admin.keyboard;

import com.google.common.collect.Lists;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.KeyboardFactoryProvider;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.CommandUriParamType.CATEGORY_ID_PARAM;
import static com.sushnaya.telegrambot.CommandUriParamType.MENU_ID_PARAM;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.singleButtonOneTimeKeyboard;

class StartupAdminKeyboardMarkupFactory implements AdminKeyboardMarkupFactory {

    public InlineKeyboardMarkup dashboardMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createMenu())
                        .setCallbackData(CREATE_MENU.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.statistics())
                        .setCallbackData(STATISTICS.getUri()),
                new InlineKeyboardButton().setText(MESSAGES.notify_())
                        .setCallbackData(NOTIFY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.promotion())
                        .setCallbackData(PROMOTION.getUri()),
                new InlineKeyboardButton().setText(MESSAGES.settings())
                        .setCallbackData(SETTINGS.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public ReplyKeyboard editMenuLocalityMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.editLocality());
    }

    public ReplyKeyboard skipCategoryPhotoStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public ReplyKeyboard skipCategorySubheadingStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public ReplyKeyboard skipProductPhotoStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    @Override
    public ReplyKeyboard skipProductSubheadingStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    @Override
    public ReplyKeyboard skipProductDescriptionStepMarkup() {
        return singleButtonOneTimeKeyboard(MESSAGES.skip());
    }

    public InlineKeyboardMarkup localityAlreadyBoundToMenuMarkup() {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editLocality())
                        .setCallbackData(EDIT_LOCALITY.getUri())
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.continueMessage())
                        .setCallbackData(CONTINUE.getUri())
        ));
        appendBackToDashboardButton(keyboard);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup menuCreationFurtherCommands(
            Menu menu, MenuCategory categoryToCreateProductIn) {
        final List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        final int categoriesCount = menu.getCategories().size();
        final MenuCategory onlyCategory = categoriesCount == 1 ?
                menu.getFirstCategory() : categoryToCreateProductIn;

        if (onlyCategory != null) appendCreateProductInCategoryButton(keyboard, onlyCategory);
        if (categoriesCount > 1) appendCreateProductInMenuButton(keyboard, menu);
        appendCreateCategoryButton(keyboard, menu);
        appendBackToDashboardButton(keyboard);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    protected void appendBackToDashboardButton(List<List<InlineKeyboardButton>> keyboard) {
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.backToDashboard())
                        .setCallbackData(ADMIN_DASHBOARD.getUri())
        ));
    }

    protected void appendCreateCategoryButton(
            List<List<InlineKeyboardButton>> keyboard, Menu menu) {
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createCategory())
                        .setCallbackData(buildCommandUri(CREATE_CATEGORY, menu.getId()))
        ));
    }

    protected void appendCreateProductInMenuButton(
            List<List<InlineKeyboardButton>> keyboard, Menu menu) {
        keyboard.add(Lists.newArrayList(new InlineKeyboardButton()
                .setText(MESSAGES.createProduct())
                .setCallbackData(buildCommandUri(CREATE_PRODUCT, menu.getId()))
        ));
    }

    protected void appendCreateProductInCategoryButton(
            List<List<InlineKeyboardButton>> keyboard, MenuCategory onlyCategory) {
        final String uri = buildCommandUri(CREATE_PRODUCT, CATEGORY_ID_PARAM, onlyCategory.getId());

        keyboard.add(Lists.newArrayList(new InlineKeyboardButton()
                .setText(MESSAGES.createProductInCategory(onlyCategory.getDisplayName()))
                .setCallbackData(uri)));
    }

    @Override
    public ReplyKeyboard productCreationCompletion(
            boolean suggestToAddSubheading, boolean suggestToAddDescription) {
        final List<KeyboardRow> keyboard = Lists.newArrayList();
        KeyboardRow row = new KeyboardRow();

        if (suggestToAddSubheading) {
            row.add(MESSAGES.setProductSubheading());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        if (suggestToAddDescription) {
            row.add(MESSAGES.setProductDescription());
            keyboard.add(row);
            row = new KeyboardRow();
        }

        row.add(MESSAGES.skipProductPublication());
        row.add(MESSAGES.publishProduct());
        keyboard.add(row);

        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setResizeKeyboard(true)
                .setOneTimeKeyboad(true);
    }

    @Override
    public InlineKeyboardMarkup editMenu(Menu menu) {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editCategory())
                        .setCallbackData(buildCommandUri(EDIT_CATEGORY, MENU_ID_PARAM, menu.getId()))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createCategory())
                        .setCallbackData(buildCommandUri(CREATE_CATEGORY, menu.getId()))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editMenuAffiliates())
                        .setCallbackData(buildCommandUri(EDIT_MENU_AFFILIATES, menu.getId())),
                new InlineKeyboardButton().setText(MESSAGES.editMenuTermsOfDelivery())
                        .setCallbackData(buildCommandUri(EDIT_MENU_DELIVERY, menu.getId()))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.cloneMenu())
                        .setCallbackData(buildCommandUri(CLONE_MENU, menu.getId())),
                new InlineKeyboardButton().setText(MESSAGES.deleteMenu())
                        .setCallbackData(buildCommandUri(DELETE_MENU, menu.getId()))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.backToDashboard())
                        .setCallbackData(ADMIN_DASHBOARD.getUri())
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    public InlineKeyboardMarkup editCategory(MenuCategory category) {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        final int categoryId = category.getId();

        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editProduct())
                        .setCallbackData(buildCommandUri(EDIT_PRODUCT, CATEGORY_ID_PARAM, categoryId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.createProduct())
                        .setCallbackData(buildCommandUri(CREATE_PRODUCT, CATEGORY_ID_PARAM, categoryId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editCategoryName())
                        .setCallbackData(buildCommandUri(EDIT_CATEGORY_NAME, categoryId)),
                new InlineKeyboardButton().setText(MESSAGES.editCategoryPhoto())
                        .setCallbackData(buildCommandUri(EDIT_CATEGORY_PHOTO, categoryId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editCategorySubheading())
                        .setCallbackData(buildCommandUri(EDIT_CATEGORY_SUBHEADING, categoryId)),
                new InlineKeyboardButton().setText(MESSAGES.deleteCategory())
                        .setCallbackData(buildCommandUri(DELETE_CATEGORY, categoryId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.backToEditMenu())
                        .setCallbackData(buildCommandUri(EDIT_MENU, category.getMenu().getId()))
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    @Override
    public InlineKeyboardMarkup editProduct(Product product) {
        List<List<InlineKeyboardButton>> keyboard = Lists.newArrayList();
        final int productId = product.getId();

        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editProductName())
                        .setCallbackData(buildCommandUri(EDIT_PRODUCT_NAME, productId)),
                new InlineKeyboardButton().setText(MESSAGES.editProductPhoto())
                        .setCallbackData(buildCommandUri(EDIT_PRODUCT_PHOTO, productId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.editProductSubheading())
                        .setCallbackData(buildCommandUri(EDIT_PRODUCT_SUBHEADING, productId)),
                new InlineKeyboardButton().setText(MESSAGES.deleteProduct())
                        .setCallbackData(buildCommandUri(DELETE_PRODUCT, productId))
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboardButton().setText(MESSAGES.backToEditCategory())
                        .setCallbackData(buildCommandUri(EDIT_CATEGORY, CATEGORY_ID_PARAM,
                                product.getCategory().getId()))
        ));

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    @Override
    public InlineKeyboardMarkup menuCategoriesKeyboard(List<MenuCategory> categories) {
        return KeyboardFactoryProvider.getUserKeyboardFactory().menuCategoriesKeyboard(categories);
    }

    @Override
    public InlineKeyboardMarkup menusKeyboard(List<Menu> menus) {
        return KeyboardFactoryProvider.getUserKeyboardFactory().menusKeyboard(menus);
    }
}
