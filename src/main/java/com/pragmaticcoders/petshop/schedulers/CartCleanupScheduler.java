package com.pragmaticcoders.petshop.schedulers;

import com.pragmaticcoders.petshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartCleanupScheduler {

    @Autowired
    private CartRepository cartRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void removeOldCarts() {
        LocalDateTime date = LocalDateTime.now().minusDays(7);
        log.info("Removing carts not updated since {}", date);
        cartRepository.deleteByUpdatedAtBefore(date);
    }

}
