package com.skillbox.cryptobot.utils;

import com.skillbox.cryptobot.bot.CryptoBot;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@UtilityClass
@Slf4j
public class SendMessageUtil {

    public static void send(AbsSender absSender, SendMessage answer) {
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка при отправке сообщения", e);
        }
    }

    public static void send(CryptoBot cryptoBot, SendMessage answer) {
        try {
            cryptoBot.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка при отправке сообщения", e);
        }
    }

}
