package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.entity.Subscribers;
import com.skillbox.cryptobot.exception.SubscriberNotFoundException;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribersService {

    private final SubscribersRepository subscribersRepository;

    private Subscribers getSubscriber(Long telegramId) {
        return subscribersRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new SubscriberNotFoundException(
                        String.format("Subscriber not found for telegram ID: %d", telegramId)));
    }

    public void createSubscriber(Long telegramId) {
        if (!subscribersRepository.existsByTelegramId(telegramId)) {
            Subscribers subscribers = Subscribers.builder()
                    .telegramId(telegramId)
                    .build();
            subscribersRepository.save(subscribers);
        }
    }

    public Double getSubscribePrice(Long telegramId) {
        return getSubscriber(telegramId).getPrice();
    }

    public void updateSubscribe(Long telegramId, Double price) {
        updateSubscriberPrice(telegramId, price);
    }

    public void unsubscribe(Long telegramId) {
        updateSubscriberPrice(telegramId, null);
    }

    private void updateSubscriberPrice(Long telegramId, Double price) {
        Subscribers subscriber = getSubscriber(telegramId);
        subscriber.setPrice(price);
        subscribersRepository.save(subscriber);
    }

    public List<Long> getSubscribersForNotifications(Double price) {
        return subscribersRepository.findTelegramIdsByPriceGreaterThanEqual(price);
    }

}
