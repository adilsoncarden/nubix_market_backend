package com.nubix.market.module.auth.util;

import com.nubix.market.module.auth.service.RecuperaciónContraseñaService;
import java.time.Duration;
import java.time.LocalDateTime;

public final class RecoveryProgressCalculator {

    private RecoveryProgressCalculator() {
    }

    /**
     * Porcentaje de vigencia restante (0–100).
     * Ej.: 4 min restantes de 5 → 80 %; 1 min restante → 20 %.
     */
    public static int remainingPercent(LocalDateTime expiresAt, LocalDateTime now) {
        long totalSeconds = RecuperaciónContraseñaService.CODE_VALIDITY_MINUTES * 60L;
        long remainingSeconds = Duration.between(now, expiresAt).getSeconds();

        if (remainingSeconds <= 0) {
            return 0;
        }

        return (int) Math.min(100, (remainingSeconds * 100) / totalSeconds);
    }
}
