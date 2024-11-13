package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.SendMessageUtil;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            Double subscribePrice = subscribersService.getSubscribePrice(message.getChatId());
            String answerText = subscribePrice == null
                    ? "Активные подписки отсутствуют"
                    : "Вы подписаны на стоимость биткоина " + TextUtil.toString(subscribePrice) + " USD";
            answer.setText(answerText);
            SendMessageUtil.send(absSender, answer);
        } catch (SubscriberNotFoundException e) {
            log.error(e.getMessage());
            answer.setText("Для продолжения нажмите /start");
            SendMessageUtil.send(absSender, answer);
        }
    }

}