package com.skillbox.cryptobot.exception;

public class NegativePriceException extends RuntimeException {

    public NegativePriceException(String message) {
        super(message);
    }

}
