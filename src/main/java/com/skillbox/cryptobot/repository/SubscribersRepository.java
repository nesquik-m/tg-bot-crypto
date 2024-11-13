package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscribers, UUID> {

    Boolean existsByTelegramId(Long telegramId);

    Optional<Subscribers> findByTelegramId(Long telegramId);

    @Query("SELECT s.telegramId FROM Subscribers s WHERE s.price >= :price")
    List<Long> findTelegramIdsByPriceGreaterThanEqual(Double price);

}
