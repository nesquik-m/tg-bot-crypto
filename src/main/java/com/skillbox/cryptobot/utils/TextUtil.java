package com.skillbox.cryptobot.utils;

import com.skillbox.cryptobot.exception.NegativePriceException;
import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;

@UtilityClass
public class TextUtil {

    public static String toString(double value) {
        if (value < 0) {
            throw new NegativePriceException("Price cannot be negative.");
        }
        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        return formatter.format(value);
    }

}