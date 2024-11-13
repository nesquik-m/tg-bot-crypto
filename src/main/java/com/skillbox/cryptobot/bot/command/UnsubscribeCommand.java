package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.SendMessageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            subscribersService.unsubscribe(message.getChatId());
            answer.setText("Подписка отменена");
            SendMessageUtil.send(absSender, answer);
        } catch (SubscriberNotFoundException e) {
            log.error(e.getMessage());
            answer.setText("Для продолжения нажмите /start");
            SendMessageUtil.send(absSender, answer);
        }
    }

}