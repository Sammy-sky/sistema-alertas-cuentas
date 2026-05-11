package com.samuel.cuentaalertas.controller;

import com.samuel.cuentaalertas.scheduler.AlertaScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AlertJobController {

    private final AlertaScheduler alertaScheduler;

    @Value("${alert.check.token:}")
    private String alertCheckToken;

    @PostMapping("/api/jobs/check-alerts")
    public ResponseEntity<Map<String, Object>> checkAlerts(
            @RequestHeader(value = "X-Alert-Token", required = false) String token) {
        if (alertCheckToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", "missing_token"));
        }

        if (!alertCheckToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "unauthorized"));
        }

        int processed = alertaScheduler.procesarAlertas();
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "processed", processed
        ));
    }
}
