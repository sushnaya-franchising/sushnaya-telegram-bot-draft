package com.sushnaya.telegrambot.command.admin;

import com.sushnaya.entity.*;
import com.sushnaya.telegrambot.Data;
import com.sushnaya.telegrambot.SushnayaBot;
import com.sushnaya.telegrambot.command.ReplyCommand;
import com.sushnaya.telegrambot.state.admin.ManagementState;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.sushnaya.telegrambot.command.Command.Uri.*;

public class RevealManagement extends ReplyCommand {
    private static final Data DATA = Data.get();

    public RevealManagement(SushnayaBot bot) {
        super(bot);
    }

    public void execute(Update update) {
        super.execute(update);

        bot.setState(update.getMessage().getFrom().getId(), new ManagementState(bot));
    }

    protected SendMessage createSendMessage(Update update) {
        StringBuilder messageBuilder = new StringBuilder()
                .append("<b>Каковы текущие настройки?</b>")
                .append("\n\n")
                .append(getBrandsString())
                .append(getMenuString())
                .append(getDeliveryZonesString())
                .append(getUsersString());

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(ManagementState.KEYBOARD_MARKUP)
                .setParseMode(ParseMode.HTML)
                .setText(messageBuilder.toString());
    }

    private String getBrandsString() {
        StringBuilder b = new StringBuilder("<b>Брэнды</b>:").append('\n');

        Collection<Brand> brands = DATA.getBrands();

        if (brands.isEmpty()) {
            b.append("-").append("\n\n");

        } else {
            for (Brand brand : brands) {
                b.append(brand.getName())
                        .append(' ')
                        .append(EDIT_BRAND_URI)
                        .append(brand.getIdAsHexString())
                        .append('\n');
            }
            b.append('\n');
        }

        return b.toString();
    }

    private String getMenuString() {
        StringBuilder b = new StringBuilder("<b>Меню</b>:")
                .append('\n');
        Collection<Menu> menuCollection = DATA.getMenu();

        if (menuCollection.isEmpty()) {
            b.append("-").append("\n\n");

        } else {
            for (Menu menu : menuCollection) {
                City city = DATA.getCityId(menu.getCityId());
                b.append(city.getName())
                        .append(' ')
                        .append(EDIT_MENU_URI)
                        .append(menu.getIdAsHexString())
                        .append('\n');
            }
            b.append('\n');
        }

        return b.toString();
    }

    private String getDeliveryZonesString() {
        StringBuilder b = new StringBuilder("<b>Зоны доставки</b>:")
                .append("\n");
        Collection<DeliveryZone> deliveryZones = DATA.getDeliveryZones();

        if (deliveryZones.isEmpty()) {
            b.append("-").append("\n\n");

        } else {
            for (DeliveryZone zone : deliveryZones) {
                City city = DATA.getCityId(zone.getCityId());
                b.append(city.getName())
                        .append(' ')
                        .append(EDIT_DELIVERY_ZONE_URI)
                        .append(zone.getIdAsHexString())
                        .append('\n');
            }
            b.append('\n');
        }

        return b.toString();
    }

    private String getUsersString() {
        StringBuilder b = new StringBuilder("<b>Пользователи</b>:").append("\n");
        List<User> admins = DATA.getAdmins().stream().sorted(
                Comparator.comparing(User::getFullName)).collect(Collectors.toList());

        if (admins.isEmpty()) {
            throw new Error("Admin users collection must be not empty");
        }

        for (User admin : admins) {
            b.append(admin.getFullName())
                    .append(" <i>").append(admin.getRole().getName()).append("</i>")
                    .append(" - ")
                    .append(EDIT_USER_URI)
                    .append(admin.getIdAsHexString())
                    .append('\n');
        }
        b.append('\n');

        return b.toString();
    }
}
