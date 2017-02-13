package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.entity.Brand;
import com.sushnaya.telegrambot.Data;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.state.admin.BrandsManagementState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import static com.sushnaya.telegrambot.command.Command.Uri.HELP_URI;

public class SaveBrandCommand extends RevealBrandsManagement {
    public SaveBrandCommand(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        saveBrand(update);

        super.execute(update);
    }

    private void saveBrand(Update update) {
        Brand brand = createBrand(update);
        Data.get().saveBrand(brand);
    }

    private Brand createBrand(Update update) {
        String brandName = update.getMessage().getText();
        return new Brand(brandName);
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(BrandsManagementState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.HTML)
                .setText("Успех! Брэнд создан. " + HELP_URI);
    }
}
