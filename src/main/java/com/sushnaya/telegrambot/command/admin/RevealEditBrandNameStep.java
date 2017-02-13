package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import com.sushnaya.telegrambot.state.admin.EditBrandNameStep;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;

public class RevealEditBrandNameStep extends ReplyCommand{
    public RevealEditBrandNameStep(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        super.execute(update);

        bot.setState(update.getMessage().getFrom().getId(),
                new EditBrandNameStep(bot, getSentMessage()));
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(new ForceReplyKeyboard())
                .setParseMode(ParseMode.HTML)
                .setText("Как называется ваш новый брэнд?");
    }
}
