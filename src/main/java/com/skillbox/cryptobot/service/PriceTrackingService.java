package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.utils.SendMessageUtil;
import com.skillbox.cryptobot.utils.TextUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceTrackingService {

    private static final ConcurrentHashMap<Long, Long> SENT_NOTIFICATIONS = new ConcurrentHashMap<>();

    @Value("${telegram.bot.notify.delay.value}")
    private int notificationDelayValue;

    @Value("${telegram.bot.notify.delay.unit}")
    private String notificationDelayUnit;

    private final CryptoBot cryptoBot;

    private final SubscribersService subscribersService;

    private final CryptoCurrencyService cryptoCurrencyService;

    @PostConstruct
    @Scheduled(cron = "${telegram.bot.notify.cron}")
    public void startPriceTracking() {
        new Thread(this::trackPrice).start();
    }

    private void trackPrice() {
        log.info("Starting price tracking...");
        try {
            removeOldNotifications();
            double bitcoinPrice = cryptoCurrencyService.getBitcoinPrice();
            List<Long> subscribersToNotify = getSubscriberTelegramIdsForNotifications(bitcoinPrice);
            subscribersToNotify.stream()
                    .filter(subscriber -> !hasNotificationBeenSent(subscriber))
                    .forEach(subscriber -> sendNotification(subscriber, bitcoinPrice));
        } catch (Exception e) {
            log.error("Error during scheduled task execution: trackPrice", e);
        }
    }

    private List<Long> getSubscriberTelegramIdsForNotifications(double bitcoinPrice) {
        return subscribersService.getSubscribersForNotifications(bitcoinPrice);
    }

    private void removeOldNotifications() {
        long delayInMillis = getDelayInMillis();
        SENT_NOTIFICATIONS.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() > delayInMillis);
    }

    private long getDelayInMillis() {
        if (notificationDelayUnit.equalsIgnoreCase("MINUTES")) {
            return notificationDelayValue * 60 * 1000L;
        } else if (notificationDelayUnit.equalsIgnoreCase("SECONDS")) {
            return notificationDelayValue * 1000L;
        } else {
            throw new IllegalArgumentException("Unknown notification delay unit: " + notificationDelayUnit);
        }
    }

    private boolean hasNotificationBeenSent(Long chatId) {
        return SENT_NOTIFICATIONS.containsKey(chatId);
    }

    private void sendNotification(Long chatId, Double bitcoinPrice) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        answer.setText("Пора покупать, стоимость биткоина " + TextUtil.toString(bitcoinPrice) + " USD");
        SendMessageUtil.send(cryptoBot, answer);
        SENT_NOTIFICATIONS.put(chatId, System.currentTimeMillis());
    }

}