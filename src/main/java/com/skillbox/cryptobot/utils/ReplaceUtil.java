package com.skillbox.cryptobot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReplaceUtil {

    public static Double replace(String command, String text) {
        return Double.valueOf(text.replace("/" + command, "").trim());
    }

}
