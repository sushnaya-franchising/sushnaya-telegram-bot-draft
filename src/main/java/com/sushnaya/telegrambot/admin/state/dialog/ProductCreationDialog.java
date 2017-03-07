package com.sushnaya.telegrambot.admin.state.dialog;

import com.sushnaya.entity.Product;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.admin.keyboard.AdminKeyboardMarkupFactory;
import com.sushnaya.telegrambot.admin.state.*;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.sushnaya.telegrambot.Command.*;
import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.KeyboardMarkupUtil.REPLY_KEYBOARD_REMOVE;

public class ProductCreationDialog extends AdminBotDialogState<Product> {
    private static final Function<Update, Command> COMPLETION_STEP_EXTRA_COMMAND_PARSER = u -> {
        if (u.hasMessage() && u.getMessage().hasText()) {
            final String text = u.getMessage().getText();

            if (text.equalsIgnoreCase(MESSAGES.setProductSubheading())) {
                return SET_PRODUCT_SUBHEADING;
            }

            if (text.equalsIgnoreCase(MESSAGES.setProductDescription())) {
                return SET_PRODUCT_DESCRIPTION;
            }

            if (text.equalsIgnoreCase(MESSAGES.skipProductPublication())) {
                return SKIP;
            }

            if (text.equalsIgnoreCase(MESSAGES.publishProduct())) {
                return PUBLISH_PRODUCT;
            }
        }

        return NOP;
    };

    private final AdminKeyboardMarkupFactory keyboardMarkupFactory;
    private final AdminBotDialogState[] steps;
    private final AskTextState nameStep;
    private final AskDoubleValueState priceStep;
    private final AskPhotoState photoStep;
    private final AskTextState subheadingStep;
    private final AskTextState descriptionStep;
    private final AskCommandState completionStep;

    public ProductCreationDialog(SushnayaBot bot) {
        super(bot);

        this.keyboardMarkupFactory = bot.getAdminKeyboardFactory();

        steps = new AdminBotDialogState[]{
                nameStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductName())
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(REPLY_KEYBOARD_REMOVE),

                priceStep = (AskDoubleValueState) new AskDoubleValueState(bot)
                        .setDefaultMessage(MESSAGES.askProductPrice())
                        .setHelpMessage(MESSAGES.askProductPriceHelp() + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)),

                photoStep = (AskPhotoState) new AskPhotoState(bot)
                        .setDefaultMessage(MESSAGES.askProductPhoto())
                        .setHelpMessage(MESSAGES.skipProductPhotoStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductPhotoStepMarkup())
                        .setSkippable(true),

                subheadingStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductSubheading())
                        .setHelpMessage((MESSAGES.askProductSubheadingHelp() + "\n\n" +
                                MESSAGES.skipProductSubheadingStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL)))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductSubheadingStepMarkup())
                        .setSkippable(true),

                descriptionStep = (AskTextState) new AskTextState(bot)
                        .setDefaultMessage(MESSAGES.askProductDescription())
                        .setHelpMessage(MESSAGES.skipProductDescriptionStepHelp(SKIP) + "\n\n" +
                                MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setDefaultKeyboard(this.keyboardMarkupFactory.skipProductDescriptionStepMarkup())
                        .setSkippable(true),

                completionStep = (AskCommandState) new AskCommandState(bot)
                        .setDefaultMessage(MESSAGES.proposeSetProductSubheadingDescriptionAndPublish())
                        .setHelpMessage(MESSAGES.cancelMenuCreationHelp(CANCEL))
                        .setSkippable(true)
                        .setExtraCommandParser(COMPLETION_STEP_EXTRA_COMMAND_PARSER)
        };
    }

    @Override
    public AdminBotDialogState<Product> onCancel(Consumer<Update> onCancel) {
        for (AdminBotDialogState step : steps) {
            step.onCancel(onCancel);
        }

        return super.onCancel(onCancel);
    }

    @Override
    public AdminBotDialogState<Product> ask(Update update, String message, ReplyKeyboard keyboard) {
        Product product = new Product();

        nameStep.ask(update, message != null ? message : MESSAGES.askProductName())
                .then((u, productName) -> {
                    product.setName(productName);

                    final String productDisplayName = product.getDisplayName(MESSAGES.getLocale());

                    priceStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                            MESSAGES.askProductPriceForProductCreation());
                });

        priceStep.then((u, productPrice) -> {
            product.setPrice(productPrice);

            final String productDisplayName = product.getDisplayName(MESSAGES.getLocale());

            photoStep.ask(u, MESSAGES.valueConfirmation(productDisplayName) + " " +
                    MESSAGES.askProductPhotoForProductCreation());
        });

        photoStep.then((u, productPhotoFileId) -> {
            product.setTelegramFileId(productPhotoFileId);

            completionStep.ask(u);
        });

        subheadingStep.then((u, productSubheading) -> {
            product.setSubheading(productSubheading);

            askProductCreationCompletionAgain(u, product);
        });

        descriptionStep.then((u, productDescription) -> {
            product.setDescription(productDescription);
            // todo: support description.md file

            askProductCreationCompletionAgain(u, product);
        });

        // reset default keyboard to propose all optional actions
        completionStep.setDefaultKeyboard(this.keyboardMarkupFactory.productCreationCompletion(
                        true, true));

        completionStep.then((u, v) -> getThen().accept(u, product))
                .ifThen(SET_PRODUCT_SUBHEADING, subheadingStep::ask)
                .ifThen(SET_PRODUCT_DESCRIPTION, descriptionStep::ask)
                .ifThen(SKIP_PRODUCT_PUBLICATION, (u) -> getThen().accept(u, product))
                .ifThen(PUBLISH_PRODUCT, (u) -> {
                    product.setPublished(true);
                    getThen().accept(u, product);
                });

        return this;
    }

    private void askProductCreationCompletionAgain(Update u, Product product) {
        final ReplyKeyboard replyKeyboard = keyboardMarkupFactory.productCreationCompletion(
                !product.hasSubheading(), !product.hasDescription());
        completionStep.setDefaultKeyboard(replyKeyboard);

        if (product.hasSubheading() && product.hasDescription()) {
            completionStep.setDefaultMessage(MESSAGES.proposePublishProduct());

        } else if (!product.hasSubheading() && product.hasDescription()) {
            completionStep.setDefaultMessage(MESSAGES.proposeSetProductSubheadingAndPublish());

        } else if (product.hasSubheading() && !product.hasDescription()) {
            completionStep.setDefaultMessage(MESSAGES.proposeSetProductDescriptionAndPublish());
        }

        completionStep.ask(u);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Product> ok, BiConsumer<Update, String> ko) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
