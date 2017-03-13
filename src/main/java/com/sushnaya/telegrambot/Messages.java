package com.sushnaya.telegrambot;

import com.google.common.collect.Maps;
import com.sushnaya.entity.Locality;
import com.sushnaya.entity.Menu;
import com.sushnaya.entity.MenuCategory;
import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.util.UTF8Control;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static com.sushnaya.telegrambot.Command.*;

public class Messages {
    private static Messages DEFAULT_MESSAGES;
    private final Map<String, MessageFormat> formats = Maps.newHashMap();
    private final ResourceBundle messages;
    private final Locale locale;

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
    }

    private MessageFormat getMessageFormat(String key) {
        final MessageFormat messageFormat = formats.get(key);
        if (messageFormat == null) {
            formats.put(key, new MessageFormat(messages.getString(key), locale));
        }

        return formats.get(key);
    }

    private String format(String key, Object... args) {
        return getMessageFormat(key).format(args);
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
        return format("create_product_in_category", categoryDisplayName);
    }

    public String askLocalityHelp() {
        return messages.getString("help.ask_locality");
    }

    public String cancelMenuCreationHelp(Command cancelCommand) {
        return format("help.cancel_menu_creation", cancelCommand.getUri());
    }

    public String askLocalityConfirmationForMenuCreation(Locality locality) {
        return format("menu_creation.ask_locality_confirmation", locality.getFullName());
    }

    public String askProductNameForMenuCreation(MenuCategory category) {
        return format("menu_creation.ask_product_name", category.getDisplayName());
    }

    public String askCategoryNameForMenuCreation() {
        return messages.getString("menu_creation.ask_category_name");
    }

    public String askCategoryNameForFirstMenuCreation() {
        return messages.getString("first_menu_creation.ask_category_name");
    }

    public String valueConfirmation(String value) {
        return format("value_confirmation", value);
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

    public String skipCategoryPhotoStepHelp() {
        return format("help.skip_category_photo_step", SKIP.getUri());
    }

    public String askCategorySubheading() {
        return messages.getString("ask_category_subheading");
    }

    public String skipCategorySubheadingStepHelp() {
        return format("help.skip_category_subheading_step", SKIP.getUri());
    }

    public String askCategorySubheadingStepHelp() {
        return messages.getString("help.ask_category_subheading");
    }

    public String askCategorySubheadingForCategoryCreation() {
        return messages.getString("category_creation.ask_category_subheading");
    }

    public String askCategoryPhotoForCategoryCreation(MenuCategory category) {
        return format("category_creation.ask_category_photo", category.getDisplayName());
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

    public String skipProductPhotoStepHelp() {
        return format("help.skip_product_photo_step", SKIP.getUri());
    }


    public String askProductSubheading() {
        return messages.getString("ask_product_subheading");
    }

    public String askProductSubheadingHelp() {
        return messages.getString("help.ask_product_subheading");
    }

    public String skipProductSubheadingStepHelp() {
        return format("help.skip_product_subheading_step", SKIP.getUri());
    }

    public String askProductDescription() {
        return messages.getString("ask_product_description");
    }

    public String skipProductDescriptionStepHelp() {
        return format("help.skip_product_description_step", SKIP.getUri());
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
        return format("user.menu.default_message", menu.getDisplayName());
    }

    public String noProductsUserMessage() {
        return messages.getString("user.menu.no_products_message");
    }

    public String userHelp() {
        return format("user.help", MENU.getUri(), HELP.getUri(), CANCEL.getUri());
    }

    public String userUnknownCommand() {
        return format("user.unknown_command", HELP.getUri());
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
        return format("admin.dashboard.default_message", todayRevenue, todayOrdersCount,
                yesterdayRevenue, yesterdayOrdersCount, lastSevenDaysRevenue,
                lastSevenDaysOrdersCount);
    }

    public String menuCreationIsCancelled() {
        return format("menu_creation.cancelled", HELP.getUri());
    }

    public String categoryCreationIsCancelled() {
        return format("category_creation.cancelled", HELP.getUri());
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
        return format("menu_creation.interrupted", HELP.getUri());
    }

    public String categoryCreationIsInterrupted() {
        return format("category_creation.interrupted", HELP.getUri());
    }

    public String menuCreationIsSuccessful(Menu menu) {
        return format("menu_creation.successful", menu.getDisplayName());
    }

    public String proposeFurtherCommandsForMenuCreation() {
        return messages.getString("menu_creation.propose_further_commands");
    }

    public String proposeFurtherCommandsForCategoryCreation() {
        return proposeFurtherCommandsForMenuCreation();
    }

    public String categoryCreationIsSuccessful(MenuCategory category) {
        return format("category_creation.successful", category.getDisplayName());
    }

    public String selectMenu() {
        return messages.getString("select_menu");
    }

    public String selectMenuForCategoryCreation() {
        return messages.getString("category_creation.select_menu");
    }

    public String categoryCreationInquireMenuCreation() {
        return format("category_creation.menu_creation_inquiry", CREATE_MENU.getUri());
    }

    public String productCreationIsCancelled() {
        return format("product_creation.cancelled", HELP.getUri());
    }

    public String productCreationIsInterrupted() {
        return format("product_creation.interrupted", HELP.getUri());
    }

    public String selectMenuForProductCreation() {
        return messages.getString("product_creation.select_menu");
    }

    public String productCreationInquireCategoryCreation() {
        return format("product_creation.category_creation_inquiry", CREATE_CATEGORY.getUri());
    }

    public String selectCategoryForProductCreation() {
        return messages.getString("product_creation.select_category");
    }

    public String productCreationInquireMenuCreation() {
        return format("product_creation.menu_creation_inquiry", CREATE_MENU.getUri());
    }

    public String productCreationIsSuccessful(Product product) {
        return format("product_creation.successful", product.getDisplayNameWithPrice(locale),
                product.getCategory().getDisplayName());
    }

    public String proposeFurtherCommandsForProductCreation() {
        return proposeFurtherCommandsForMenuCreation();
    }

    public String adminHelp() {
        return format("admin.help", ADMIN_DASHBOARD.getUri(), CREATE_PRODUCT.getUri(),
                CREATE_CATEGORY.getUri(), CREATE_MENU.getUri());
    }

    public String nextProduct() {
        return messages.getString("next_product");
    }

    public String selectMenuToEditCategoryIn() {
        return messages.getString("select_menu_to_edit_category_in");
    }

    public String noCategoryWasCreatedToEdit(Command createCategoryCommand) {
        return format("no_category_was_created_to_edit", CREATE_CATEGORY.getUri());
    }

    public String selectCategoryToEdit() {
        return messages.getString("select_category_to_edit");
    }

    public String backToEditMenu() {
        return messages.getString("back_to_edit_menu");
    }

    public String noProductWasCreatedToEdit(Command createProductCommand) {
        return format("no_product_was_created_to_edit", CREATE_PRODUCT.getUri());
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
        return format("product_settings",
                product.getDisplayNameWithPrice(getLocale()),
                product.hasPhoto() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                product.hasSubheading() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                product.hasDescription() ? optionalValueIsProvided() : optionalValueIsNotProvided()
        );
    }

    public String noMenuWasCreatedToEdit() {
        return format("no_menu_was_created_to_edit", CREATE_MENU.getUri());
    }

    public String selectMenuToEdit() {
        return messages.getString("select_menu_to_edit");
    }

    public String categorySettings(MenuCategory category) {
        return format("category_settings",
                category.getDisplayName(),
                category.getProducts().size(),
                category.hasPhoto() ? optionalValueIsProvided() : optionalValueIsNotProvided(),
                category.hasSubheading() ? optionalValueIsProvided() : optionalValueIsNotProvided()
        );
    }

    public String menuSettings(Menu menu) {
        int productsCount = menu.getCategories()
                .stream().mapToInt(c -> c.getProducts().size()).sum();

        return format("menu_settings", menu.getDisplayName(), menu.getCategories().size(),
                productsCount);
    }

    public String more() {
        return messages.getString("more");
    }

    public String menuWasDeleted(Menu menu) {
        return format("menu_was_deleted", menu.getDisplayName());
    }

    public String recoverMenu(Menu menu) {
        return format("recover_menu", menu.getDisplayName());
    }

    public String menuWasRecovered(Menu menu) {
        return format("menu_was_recovered", menu.getDisplayName());
    }

    public String categoryWasDeleted(MenuCategory category) {
        return format("category_was_deleted", category.getDisplayName());
    }

    public String recoverCategory(MenuCategory category) {
        return format("recover_category", category.getDisplayName());
    }

    public String categoryWasRecovered(MenuCategory category) {
        return format("category_was_recovered", category.getDisplayName());
    }

    public String productWasDeleted(Product product) {
        return format("product_was_deleted", product.getDisplayName());
    }

    public String menuRecoveryFailed() {
        return messages.getString("menu_recovery_failed");
    }

    public String categoryRecoveryFailed() {
        return messages.getString("category_recovery_failed");
    }

    public String productRecoveryFailed() {
        return messages.getString("product_recovery_failed");
    }

    public String productWasRecovered(Product product) {
        return format("product_was_recovered", product.getDisplayName());
    }

    public String recoverProduct(Product product) {
        return format("recover_product", product.getDisplayName());
    }
}
