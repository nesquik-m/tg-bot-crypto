package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.SendMessageUtil;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetPriceCommand implements IBotCommand {

    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "get_price";
    }

    @Override
    public String getDescription() {
        return "Возвращает цену биткоина в USD";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        try {
            answer.setText("Текущая цена биткоина " + TextUtil.toString(cryptoCurrencyService.getBitcoinPrice()) + " USD");
            SendMessageUtil.send(absSender, answer);

        } catch (IOException e) {
            log.error(e.getMessage());
            answer.setText("Что-то пошло не так :(\nПовторите запрос");
            SendMessageUtil.send(absSender, answer);
        }
    }

}