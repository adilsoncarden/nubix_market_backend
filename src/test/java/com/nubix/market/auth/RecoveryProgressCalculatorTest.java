package com.nubix.market.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nubix.market.module.auth.util.RecoveryProgressCalculator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RecoveryProgressCalculatorTest {

    @Test
    void remainingPercent_fourMinutesLeft_isEighty() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 7, 10, 1, 0);
        LocalDateTime expiresAt = LocalDateTime.of(2026, 6, 7, 10, 5, 0);

        assertEquals(80, RecoveryProgressCalculator.remainingPercent(expiresAt, now));
    }

    @Test
    void remainingPercent_oneMinuteLeft_isTwenty() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 7, 10, 4, 0);
        LocalDateTime expiresAt = LocalDateTime.of(2026, 6, 7, 10, 5, 0);

        assertEquals(20, RecoveryProgressCalculator.remainingPercent(expiresAt, now));
    }

    @Test
    void remainingPercent_expired_isZero() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 7, 10, 6, 0);
        LocalDateTime expiresAt = LocalDateTime.of(2026, 6, 7, 10, 5, 0);

        assertEquals(0, RecoveryProgressCalculator.remainingPercent(expiresAt, now));
    }
}
