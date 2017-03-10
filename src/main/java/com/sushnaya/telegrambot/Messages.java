package com.sushnaya.telegrambot;

import com.sushnaya.entity.Locality;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.util.UTF8Control;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    private static Messages DEFAULT_MESSAGES;

    private final ResourceBundle messages;
    private final Locale locale;
    private final MessageFormat createProductInCategoryFormat;
    private final MessageFormat cancelMenuCreationHelpFormat;
    private final MessageFormat askLocalityConfirmationForMenuCreationFormat;
    private final MessageFormat askProductNameForMenuCreationFormat;
    private final MessageFormat valueConfirmationFormat;
    private final MessageFormat skipCategoryPhotoStepHelpFormat;
    private final MessageFormat skipCategorySubheadingStepHelpFormat;
    private final MessageFormat askCategoryPhotoForCategoryCreationFormat;
    private final MessageFormat skipProductPhotoStepHelpFormat;
    private final MessageFormat skipProductSubheadingStepHelpFormat;
    private final MessageFormat skipProductDescriptionStepHelpFormat;
    private final MessageFormat userHelpFormat;
    private final MessageFormat adminHelpFormat;
    private final MessageFormat userUnknownCommandFormat;
    private final MessageFormat dashboardDefaultMessageFormat;
    private final MessageFormat menuCreationIsCancelledFormat;
    private final MessageFormat menuCreationIsInterruptedFormat;
    private final MessageFormat menuCreationIsSuccessfulFormat;
    private final MessageFormat categoryCreationIsSuccessfulFormat;
    private final MessageFormat categoryCreationIsCancelledFormat;
    private final MessageFormat categoryCreationIsInterruptedFormat;
    private final MessageFormat categoryCreationInquireMenuCreationFormat;
    private final MessageFormat productCreationIsCancelledFormat;
    private final MessageFormat productCreationIsInterruptedFormat;
    private final MessageFormat productCreationInquireMenuCreationFormat;
    private final MessageFormat productCreationInquireCategoryCreationFormat;
    private final MessageFormat productCreationIsSuccessfulFormat;
    private final MessageFormat userMenuDefaultMessageFormat;
    private final MessageFormat nextProductFormat;
    private final MessageFormat noCategoryWasCreatedToEditFormat;
    private final MessageFormat noProductWasCreatedToEditFormat;
    private final MessageFormat productSettingsFormat;
    private final MessageFormat categorySettingsFormat;
    private final MessageFormat noMenuWasCreatedToEditFormat;
    private final MessageFormat menuSettingsFormat;

    public static Messages getDefaultMessages() {
        if (DEFAULT_MESSAGES == null) {
            DEFAULT_MESSAGES = create();
        }

        return DEFAULT_MESSAGES;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Messages create() {
        return create(Locale.getDefault());
    }

    public static Messages create(Locale locale) {
        return new Messages(locale);
    }

    private Messages(Locale locale) {
        this.locale = locale;

        this.messages = ResourceBundle.getBundle("MessagesBundle", locale, new UTF8Control());

        createProductInCategoryFormat = new MessageFormat(
                messages.getString("create_product_in_category"), locale);
        cancelMenuCreationHelpFormat = new MessageFormat(
                messages.getString("help.cancel_menu_creation"), locale);
        askLocalityConfirmationForMenuCreationFormat = new MessageFormat(
                messages.getString("menu_creation.ask_locality_confirmation"), locale);
        askProductNameForMenuCreationFormat = new MessageFormat(
                messages.getString("menu_creation.ask_product_name"), locale);
        valueConfirmationFormat = new MessageFormat(messages.getString("value_confirmation"), locale);
        skipCategoryPhotoStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_category_photo_step"), locale);
        skipCategorySubheadingStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_category_subheading_step"), locale);
        askCategoryPhotoForCategoryCreationFormat = new MessageFormat(
                messages.getString("category_creation.ask_category_photo"), locale);
        skipProductPhotoStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_photo_step"), locale);
        skipProductSubheadingStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_subheading_step"), locale);
        skipProductDescriptionStepHelpFormat = new MessageFormat(
                messages.getString("help.skip_product_description_step"), locale);
        userHelpFormat = new MessageFormat(messages.getString("user.help"), locale);
        adminHelpFormat = new MessageFormat(messages.getString("admin.help"), locale);
        userUnknownCommandFormat = new MessageFormat(
                messages.getString("user.unknown_command"), locale);
        dashboardDefaultMessageFormat = new MessageFormat(
                messages.getString("admin.dashboard.default_message"), locale);
        menuCreationIsCancelledFormat = new MessageFormat(
                messages.getString("menu_creation.cancelled"), locale);
        menuCreationIsInterruptedFormat = new MessageFormat(
                messages.getString("menu_creation.interrupted"), locale);
        menuCreationIsSuccessfulFormat = new MessageFormat(
                messages.getString("menu_creation.successful"), locale);
        categoryCreationIsSuccessfulFormat = new MessageFormat(
                messages.getString("category_creation.successful"), locale);
        categoryCreationIsCancelledFormat = new MessageFormat(
                messages.getString("category_creation.cancelled"), locale);
        categoryCreationIsInterruptedFormat = new MessageFormat(
                messages.getString("category_creation.interrupted"), locale);
        categoryCreationInquireMenuCreationFormat = new MessageFormat(
                messages.getString("category_creation.menu_creation_inquiry"), locale);
        productCreationIsCancelledFormat = new MessageFormat(
                messages.getString("product_creation.cancelled"), locale);
        productCreationIsInterruptedFormat = new MessageFormat(
                messages.getString("product_creation.interrupted"), locale);
        productCreationInquireMenuCreationFormat = new MessageFormat(
                messages.getString("product_creation.menu_creation_inquiry"), locale);
        productCreationInquireCategoryCreationFormat = new MessageFormat(
                messages.getString("product_creation.category_creation_inquiry"), locale);
        productCreationIsSuccessfulFormat = new MessageFormat(
                messages.getString("product_creation.successful"), locale);
        userMenuDefaultMessageFormat = new MessageFormat(
                messages.getString("user.menu.default_message"));
        nextProductFormat = new MessageFormat(
                messages.getString("next_product"));
        noCategoryWasCreatedToEditFormat = new MessageFormat(
                messages.getString("no_category_was_created_to_edit"));
        noProductWasCreatedToEditFormat = new MessageFormat(
                messages.getString("no_product_was_created_to_edit"));
        productSettingsFormat = new MessageFormat(
                messages.getString("product_settings"));
        categorySettingsFormat = new MessageFormat(
                messages.getString("category_settings"));
        noMenuWasCreatedToEditFormat = new MessageFormat(
                messages.getString("no_menu_was_created_to_edit"));
        menuSettingsFormat = new MessageFormat(
                messages.getString("menu_settings"));
    }

    public String askProductPriceForProductCreation() {
        return messages.getString("product_creation.ask_product_price");
    }

    public String askProductPhotoForProductCreation() {
        return messages.getString("product_creation.ask_product_photo");
    }

    public String askLocality() {
        return messages.getString("ask_locality");
    }

    public String askLocalityForMenuCreation() {
        return messages.getString("menu_creation.ask_locality");
    }

    public String sendContact() {
        return messages.getString("send_contact");
    }

    public String menu() {
        return messages.getString("menu");
    }

    public String search() {
        return messages.getString("search");
    }

    public String dashboard() {
        return messages.getString("dashboard");
    }

    public String cancel() {
        return messages.getString("cancel");
    }

    public String skip() {
        return messages.getString("skip");
    }

    public String continueMessage() {
        return messages.getString("continue");
    }

    public String backToMenu() {
        return messages.getString("back_to_menu");
    }

    public String backToDashboard() {
        return messages.getString("back_to_dashboard");
    }

    public String settings() {
        return messages.getString("settings");
    }

    public String statistics() {
        return messages.getString("statistics");
    }

    public String notify_() {
        return messages.getString("notify");
    }

    public String promotion() {
        return messages.getString("promotion");
    }

    public String createMenu() {
        return messages.getString("create_menu");
    }

    public String editMenu() {
        return messages.getString("edit_menu");
    }

    public String editLocality() {
        return messages.getString("edit_locality");
    }

    public String deleteMenu() {
        return messages.getString("delete_menu");
    }

    public String editCategories() {
        return messages.getString("edit_categories");
    }

    public String createCategory() {
        return messages.getString("create_category");
    }

    public String deleteCategory() {
        return messages.getString("delete_category");
    }

    public String editProduct() {
        return messages.getString("edit_product");
    }

    public String setProductSubheading() {
        return messages.getString("set_product_subheading");
    }

    public String setProductDescription() {
        return messages.getString("set_product_description");
    }

    public String publishProduct() {
        return messages.getString("publish_product");
    }

    public String skipProductPublication() {
        return messages.getString("skip_product_publication");
    }

    public String createProduct() {
        return messages.getString("create_product");
    }

    public String createProductInCategory(String categoryDisplayName) {
        Object[] args = {categoryDisplayName};
        return createProductInCategoryFormat.format(args);
    }

    public String askLocalityHelp() {
        return messages.getString("help.ask_locality");
    }

    public String cancelMenuCreationHelp(Command cancelCommand) {
        Object[] args = {cancelCommand.getUri()};
        return cancelMenuCreationHelpFormat.format(args);
    }

    public String askLocalityConfirmationForMenuCreation(Locality locality) {
        Object[] args = {locality.getFullName()};
        return askLocalityConfirmationForMenuCreationFormat.format(args);
    }

    public String askProductNameForMenuCreation(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return askProductNameForMenuCreationFormat.format(args);
    }

    public String askCategoryNameForMenuCreation() {
        return messages.getString("menu_creation.ask_category_name");
    }

    public String askCategoryNameForFirstMenuCreation() {
        return messages.getString("first_menu_creation.ask_category_name");
    }

    public String valueConfirmation(String value) {
        Object[] args = {value};
        return valueConfirmationFormat.format(args);
    }

    public String askCategoryName() {
        return messages.getString("ask_category_name");
    }

    public String askCategoryNameHelp() {
        return messages.getString("help.ask_category_name");
    }

    public String askCategoryPhoto() {
        return messages.getString("ask_category_photo");
    }

    public String skipCategoryPhotoStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipCategoryPhotoStepHelpFormat.format(args);
    }

    public String askCategorySubheading() {
        return messages.getString("ask_category_subheading");
    }

    public String skipCategorySubheadingStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipCategorySubheadingStepHelpFormat.format(args);
    }

    public String askCategorySubheadingStepHelp() {
        return messages.getString("help.ask_category_subheading");
    }

    public String askCategorySubheadingForCategoryCreation() {
        return messages.getString("category_creation.ask_category_subheading");
    }

    public String askCategoryPhotoForCategoryCreation(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return askCategoryPhotoForCategoryCreationFormat.format(args);
    }

    public String askProductName() {
        return messages.getString("ask_product_name");
    }


    public String askProductPrice() {
        return messages.getString("ask_product_price");
    }

    public String askProductPriceHelp() {
        return messages.getString("help.ask_product_price");
    }

    public String askProductPhoto() {
        return messages.getString("ask_product_photo");
    }

    public String skipProductPhotoStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductPhotoStepHelpFormat.format(args);
    }

    public String askProductSubheading() {
        return messages.getString("ask_product_subheading");
    }

    public String askProductSubheadingHelp() {
        return messages.getString("help.ask_product_subheading");
    }

    public String skipProductSubheadingStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductSubheadingStepHelpFormat.format(args);
    }

    public String askProductDescription() {
        return messages.getString("ask_product_description");
    }

    public String skipProductDescriptionStepHelp(Command skipCommand) {
        Object[] args = {skipCommand.getUri()};
        return skipProductDescriptionStepHelpFormat.format(args);
    }

    public String proposeSetProductSubheadingDescriptionAndPublish() {
        return messages.getString("propose_set_product_subheading_description_and_publish");
    }

    public String proposePublishProduct() {
        return messages.getString("propose_publish_product");
    }

    public String proposeSetProductSubheadingAndPublish() {
        return messages.getString("propose_set_product_subheading_and_publish");
    }

    public String proposeSetProductDescriptionAndPublish() {
        return messages.getString("propose_set_product_description_and_publish");
    }

    public String greeting() {
        return messages.getString("greeting");
    }

    public String userMenuDefaultMessage(Menu menu) {
        Object[] args = {menu.getLocality().getName()};
        return userMenuDefaultMessageFormat.format(args);
    }

    public String noProductsUserMessage() {
        return messages.getString("user.menu.no_products_message");
    }

    public String userHelp(Command menuCommand, Command helpCommand, Command cancelCommand) {
        Object[] args = {menuCommand.getUri(), helpCommand.getUri(), cancelCommand.getUri()};
        return userHelpFormat.format(args);
    }

    public String userUnknownCommand(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return userUnknownCommandFormat.format(args);
    }

    public String unregisteredUserContactInquiry() {
        return messages.getString("unregistered_user.contact_inquiry");
    }

    public String unregisteredUserHelp() {
        return messages.getString("unregistered_user.help");
    }

    public String askToRegister() {
        return messages.getString("ask_to_register");
    }

    public String registrationSuccessful() {
        return messages.getString("registration_successful");
    }

    // todo: support plural forms
    public String dashboardDefaultMessage(double todayRevenue, int todayOrdersCount,
                                          double yesterdayRevenue, int yesterdayOrdersCount,
                                          double lastSevenDaysRevenue, int lastSevenDaysOrdersCount) {
        Object[] args = {todayRevenue, todayOrdersCount,
                yesterdayRevenue, yesterdayOrdersCount,
                lastSevenDaysRevenue, lastSevenDaysOrdersCount};

        return dashboardDefaultMessageFormat.format(args);
    }

    public String menuCreationIsCancelled(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return menuCreationIsCancelledFormat.format(args);
    }

    public String categoryCreationIsCancelled(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return categoryCreationIsCancelledFormat.format(args);
    }

    public String administrationAllowed() {
        return messages.getString("administration_allowed");
    }

    public String dashboardNoProductsMessage() {
        return messages.getString("admin.dashboard.no_products_message");
    }

    public String dashboardNoPublishedProductsMessage() {
        return messages.getString("admin.dashboard.no_published_products_message");
    }

    public String nothingToCancel() {
        return messages.getString("nothing_to_cancel");
    }

    public String nothingToSkip() {
        return messages.getString("nothing_to_skip");
    }

    public String askCommandUnknownMessage() {
        return messages.getString("ask_command_unknown_message");
    }

    public String inquireTextMessage() {
        return messages.getString("inquire_text_message");
    }

    public String doubleValueFormatError() {
        return messages.getString("ask_double_value_format_error");
    }

    public String inquireTextMessageForLocality() {
        return messages.getString("ask_locality.inquire_text_message");
    }

    public String localityResolveError() {
        return messages.getString("ask_locality.resolve_error");
    }

    public String inquirePhotoMessage() {
        return messages.getString("inquire_photo_message");
    }

    public String gettingPhotoFilepathError() {
        return messages.getString("ask_photo_get_file_path_error");
    }

    public String closeDashboard() {
        return messages.getString("close_dashboard");
    }

    public String menuCreationIsInterrupted(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return menuCreationIsInterruptedFormat.format(args);
    }

    public String categoryCreationIsInterrupted(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return categoryCreationIsInterruptedFormat.format(args);
    }

    public String menuCreationIsSuccessful(Menu menu) {
        Object[] args = {menu.getLocality().getFullName()};
        return menuCreationIsSuccessfulFormat.format(args);
    }

    public String proposeFurtherCommandsForMenuCreation() {
        return messages.getString("menu_creation.propose_further_commands");
    }

    public String proposeFurtherCommandsForCategoryCreation() {
        return proposeFurtherCommandsForMenuCreation();
    }

    public String categoryCreationIsSuccessful(MenuCategory category) {
        Object[] args = {category.getDisplayName()};
        return categoryCreationIsSuccessfulFormat.format(args);
    }

    public String selectMenu() {
        return messages.getString("select_menu");
    }

    public String selectMenuForCategoryCreation() {
        return messages.getString("category_creation.select_menu");
    }

    public String categoryCreationInquireMenuCreation(Command createMenuCommand) {
        Object[] args = {createMenuCommand.getUri()};
        return categoryCreationInquireMenuCreationFormat.format(args);
    }

    public String productCreationIsCancelled(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return productCreationIsCancelledFormat.format(args);
    }

    public String productCreationIsInterrupted(Command helpCommand) {
        Object[] args = {helpCommand.getUri()};
        return productCreationIsInterruptedFormat.format(args);
    }

    public String selectMenuForProductCreation() {
        return messages.getString("product_creation.select_menu");
    }

    public String productCreationInquireCategoryCreation(Command createCategoryCommand) {
        Object[] args = {createCategoryCommand.getUri()};
        return productCreationInquireCategoryCreationFormat.format(args);
    }

    public String selectCategoryForProductCreation() {
        return messages.getString("product_creation.select_category");
    }

    public String productCreationInquireMenuCreation(Command createMenuCommand) {
        Object[] args = {createMenuCommand.getUri()};
        return productCreationInquireMenuCreationFormat.format(args);
    }

    public String productCreationIsSuccessful(Product product) {
        Object[] args = {product.getDisplayNameWithPrice(locale),
                product.getCategory().getDisplayName()};
        return productCreationIsSuccessfulFormat.format(args);
    }

    public String proposeFurtherCommandsForProductCreation() {
        return proposeFurtherCommandsForMenuCreation();
    }

    public String adminHelp(Command dashboardCommand, Command createProductCommand,
                            Command createCategoryCommand, Command createMenuCommand) {
        Object[] args = {dashboardCommand.getUri(), createProductCommand.getUri(),
                createCategoryCommand.getUri(), createMenuCommand.getUri()};
        return adminHelpFormat.format(args);
    }

    public String nextProduct(int nextProductNumber, int productsCount) {
        Object[] args = {nextProductNumber, productsCount};
        return nextProductFormat.format(args);
    }

    public String selectMenuToEditCategoryIn() {
        return messages.getString("select_menu_to_edit_category_in");
    }

    public String noCategoryWasCreatedToEdit(Command createCategoryCommand) {
        Object[] args = {createCategoryCommand.getUri()};
        return noCategoryWasCreatedToEditFormat.format(args);
    }

    public String selectCategoryToEdit() {
        return messages.getString("select_category_to_edit");
    }

    public String backToEditMenu() {
        return messages.getString("back_to_edit_menu");
    }

    public String noProductWasCreatedToEdit(Command createProductCommand) {
        Object[] args = {createProductCommand.getUri()};
        return noProductWasCreatedToEditFormat.format(args);
    }

    public String selectMenuToEditProductIn() {
        return messages.getString("select_menu_to_edit_product_in");
    }

    public String menuContainsNoCategoryWithProducts() {
        return messages.getString("menu_contains_no_category_with_products");
    }

    public String selectCategoryToEditProductIn() {
        return messages.getString("select_category_to_edit_product_in");
    }

    public String categoryDoesNotContainProducts() {
        return messages.getString("category_does_not_contain_products");
    }

    public String editCategory() {
        return messages.getString("edit_category");
    }

    public String deleteProduct() {
        return messages.getString("delete_product");
    }

    public String backToEditCategory() {
        return messages.getString("back_to_edit_category");
    }

    public String selectProductToEdit() {
        return messages.getString("select_product_to_edit");
    }

    public String optionalValueIsProvided() {
        return messages.getString("optional_value_is_provided");
    }

    public String optionalValueIsNotProvided() {
        return messages.getString("optional_value_is_not_provided");
    }

    public String productSettings(Product product) {
        Object[] args = {
                product.getDisplayNameWithPrice(getLocale()),
                product.hasPhoto() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                product.hasSubheading() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                product.hasDescription() ? optionalValueIsProvided() : optionalValueIsNotProvided()
        };

        return productSettingsFormat.format(args);
    }

    public String noMenuWasCreatedToEdit(Command createMenuCommand) {
        Object[] args = {createMenuCommand.getUri()};
        return noMenuWasCreatedToEditFormat.format(args);
    }

    public String selectMenuToEdit() {
        return messages.getString("select_menu_to_edit");
    }

    public String categorySettings(MenuCategory category) {
        Object[] args = {
                category.getDisplayName(),
                category.getProducts().size(),
                category.hasPhoto() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                category.hasSubheading() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
        };

        return categorySettingsFormat.format(args);
    }

    public String menuSettings(Menu menu) {
        int productsCount = menu.getCategories()
                .stream().mapToInt(c -> c.getProducts().size()).sum();
        Object[] args = {menu.getLocalityName(), menu.getCategories().size(), productsCount};

        return menuSettingsFormat.format(args);
    }
}
