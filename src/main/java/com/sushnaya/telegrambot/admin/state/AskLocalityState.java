package com.sushnaya.telegrambot.admin.state;

import com.sushnaya.entity.Locality;
import com.sushnaya.telegrambot.SushnayaBot;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import static com.sushnaya.telegrambot.SushnayaBot.MESSAGES;
import static com.sushnaya.telegrambot.util.UpdateUtil.isTextMessage;

public class AskLocalityState extends AdminBotDialogState<Locality> {
    private static final String YANDEX_GEOCODE_BASE_URL = "https://geocode-maps.yandex.ru/1.x/";
    public static final String UNKNOWN_UPDATE_ERROR_MESSAGE =
            MESSAGES.inquireTextMessageForLocality();
    public static final String RESOLVE_LOCALITY_ERROR_MESSAGE = MESSAGES.localityResolveError();

    public AskLocalityState(SushnayaBot bot) {
        super(bot);
    }

    @Override
    protected void handleUpdate(Update update, BiConsumer<Update, Locality> ok,
                                BiConsumer<Update, String> ko) {
        if (isTextMessage(update)) {
            try {
                ok.accept(update, resolveLocality(update));

            } catch (Exception ex) {
                // todo: log exception
                ko.accept(update, RESOLVE_LOCALITY_ERROR_MESSAGE);
            }

        } else {
            ko.accept(update, UNKNOWN_UPDATE_ERROR_MESSAGE);
        }
    }

    private Locality resolveLocality(Update update) throws Exception {
        String localityName = update.getMessage().getText();

        return geocode(localityName);
    }

    private Locality geocode(String localityName) throws Exception {
        URI uri = createUri(localityName);
        String json = requestJson(uri);

        return Locality.parseYandexGeocodeJson(json);
    }

    private String requestJson(URI uri) throws IOException {
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = bot.getHttpClient().execute(httpget);
        HttpEntity entity = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(entity);

        return EntityUtils.toString(buf, StandardCharsets.UTF_8);
    }

    private URI createUri(String localityName) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(YANDEX_GEOCODE_BASE_URL);
        builder.setParameter("geocode", localityName)
                .setParameter("kind", "locality")
                .setParameter("format", "json")
                .setParameter("results", "1");

        return builder.build();
    }
}
