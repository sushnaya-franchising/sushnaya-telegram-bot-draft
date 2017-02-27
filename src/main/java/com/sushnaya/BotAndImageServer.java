package com.sushnaya;

import com.sushnaya.imageserver.ImageHttpServer;
import com.sushnaya.telegrambot.Command;
import com.sushnaya.telegrambot.SushnayaBot;
import io.netty.channel.ChannelFuture;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.net.InetSocketAddress;

public class BotAndImageServer {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String token = getBotToken(args);
        startTelegramBot(token);

        int port = getImageHttpServerPort(args);
        startImageHttpServer(port);
    }

    private static String getBotToken(String[] args) {
        return args[1];
    }

    private static void printUsage() {
        System.out.println(BotAndImageServer.class.getSimpleName() + " <port> <bot_token>");
    }

    private static void startTelegramBot(String token) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new SushnayaBot(token));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        System.out.println("Telegram bot started...");
    }

    private static int getImageHttpServerPort(String[] args) {
        return Integer.parseInt(args[0]);
    }

    private static void startImageHttpServer(int port) {
        final ImageHttpServer imageServer = new ImageHttpServer();
        ChannelFuture future = imageServer.start(new InetSocketAddress(port));

        System.out.println("Image server is listening port " + port + "...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                imageServer.shutdown();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
