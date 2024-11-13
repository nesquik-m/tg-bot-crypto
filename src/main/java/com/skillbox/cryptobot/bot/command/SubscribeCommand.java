package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.NegativePriceException;
import com.skillbox.cryptobot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.ReplaceUtil;
import com.skillbox.cryptobot.utils.SendMessageUtil;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;

/**
 * Обработка команды подписки на курс валюты
 * SubscribeCommand реализует интерфейс IBotCommand, что означает, что он представляет собой команду бота.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final CryptoCurrencyService cryptoCurrencyService;

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            Double price = ReplaceUtil.replace(getCommandIdentifier(), message.getText());
            subscribersService.updateSubscribe(message.getChatId(), price);
            answer.setText("Текущая цена биткоина " + TextUtil.toString(cryptoCurrencyService.getBitcoinPrice()) + " USD");
            SendMessageUtil.send(absSender, answer);
            answer.setText("Новая подписка создана на стоимость " + TextUtil.toString(price) + " USD");
            SendMessageUtil.send(absSender, answer);

        } catch (SubscriberNotFoundException e) {
            log.error(e.getMessage());
            answer.setText("Для продолжения нажмите /start");
            SendMessageUtil.send(absSender, answer);

        } catch (IOException e) {
            log.error(e.getMessage());
            answer.setText("Что-то пошло не так :(\nПовторите попытку");
            SendMessageUtil.send(absSender, answer);

        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            answer.setText("Недопустимый формат числа. Повторите попытку");
            SendMessageUtil.send(absSender, answer);

        } catch (NegativePriceException e) {
            log.error(e.getMessage());
            answer.setText("Число не может быть отрицательным. Повторите попытку");
            SendMessageUtil.send(absSender, answer);
        }
    }

}