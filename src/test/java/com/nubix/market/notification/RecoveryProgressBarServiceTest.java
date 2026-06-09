package com.nubix.market.notification;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.auth.util.RecoveryBarSignature;
import com.nubix.market.module.notification.service.RecoveryProgressBarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecoveryProgressBarServiceTest {

    @Mock
    private ReseteoContraseñaRepository reseteoContraseñaRepository;

    private RecoveryProgressBarService service;

    @BeforeEach
    void setUp() {
        RecoveryBarSignature signature = new RecoveryBarSignature("test-secret-key-for-hmac-signing");
        service = new RecoveryProgressBarService(
                reseteoContraseñaRepository, signature, "https://api.nubix.test");
    }

    @Test
    void buildTableProgressBarHtml_eightyPercent_usesTableCellWidths() {
        String html = service.buildTableProgressBarHtml(80);

        assertTrue(html.contains("width=\"80%\""));
        assertTrue(html.contains("width=\"20%\""));
        assertTrue(html.contains("bgcolor=\"#006634\""));
    }

    @Test
    void buildTableProgressBarHtml_twentyPercent_usesTableCellWidths() {
        String html = service.buildTableProgressBarHtml(20);

        assertTrue(html.contains("width=\"20%\""));
        assertTrue(html.contains("width=\"80%\""));
    }
}
