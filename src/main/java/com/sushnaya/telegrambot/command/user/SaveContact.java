package com.sushnaya.telegrambot.command.user;

import com.sushnaya.entity.User;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import com.sushnaya.telegrambot.state.user.DefaultState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;

public class SaveContact extends ReplyCommand {
    public SaveContact(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        super.execute(update);

        User user = registerUser(update);
        bot.setState(user.getTelegramId(), new DefaultState(bot));
    }

    protected SendMessage createSendMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(DefaultState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.MARKDOWN)
                .setText("Отлично! Я успешно обновил ваши контактные данные. " +
                        "Теперь вы зарегистрированы.");
    }

    private User registerUser(Update update) {
        User user = User.fromTelegramUser(update.getMessage().getFrom());
        Contact contact = update.getMessage().getContact();
        user.setPhoneNumber(contact.getPhoneNumber());

        bot.registerUser(user);

        return user;
    }

//    public void sendAppPromotionMessage(Update update) {
//        try {
//            bot.sendMessage(createPromotionTextMessage(update));
//            bot.sendMessage(createIPhoneAppLinkMessage(update));
//            bot.sendMessage(createAndroidAppLinkMessage(update));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private SendMessage createPromotionTextMessage(Update update) {
//        return new SendMessage()
//                .setChatId(update.getMessage().getChatId())
//                .setReplyMarkup(DefaultState.KEYBOARD_MARKUP)
//                .setParseMode(ParseMode.MARKDOWN)
//                .setText("Отлично! Если вы захотите сделать заказ, то вы можете " +
//                        "сделать это через мобильное приложение.");
//    }
//
//    private SendMessage createIPhoneAppLinkMessage(Update update) {
//        return new SendMessage()
//                .setChatId(update.getMessage().getChatId())
//                .setParseMode(ParseMode.MARKDOWN)
//                .setText("Скачать для iPhone можно [здесь](https://itunes.apple.com/us/app/complete-anatomy/id1054948424?mt=8).");
//    }
//
//    private SendMessage createAndroidAppLinkMessage(Update update) {
//        return new SendMessage()
//                .setChatId(update.getMessage().getChatId())
//                .setParseMode(ParseMode.MARKDOWN)
//                .setText("A для Android [тут](https://play.google.com/store/apps/dev?id=9133452689932095671).");
//    }
}
