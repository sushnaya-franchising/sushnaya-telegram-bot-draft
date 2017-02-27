package com.sushnaya.telegrambot;

public class SushnayaBotException extends Exception {
    public SushnayaBotException() {
        super();
    }

    public SushnayaBotException(String message) {
        super(message);
    }

    public SushnayaBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public SushnayaBotException(Throwable cause) {
        super(cause);
    }

    protected SushnayaBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}