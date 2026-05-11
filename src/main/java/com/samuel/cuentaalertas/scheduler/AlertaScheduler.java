package com.samuel.cuentaalertas.scheduler;

import com.samuel.cuentaalertas.model.CuentaCorreo;
import com.samuel.cuentaalertas.service.CuentaService;
import com.samuel.cuentaalertas.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaScheduler {

    private final CuentaService cuentaService;
    private final TelegramService telegramService;

    @Scheduled(fixedDelay = 60000) // cada 60 segundos
    public void verificarCuentas() {
        procesarAlertas();
    }

    public int procesarAlertas() {
        log.info("🔍 Verificando cuentas bloqueadas...");

        try {
            List<CuentaCorreo> cuentas = cuentaService.buscarParaNotificar();

            if (cuentas.isEmpty()) {
                log.info("😴 Sin cuentas para notificar.");
                return 0;
            }

            log.info("🚨 {} cuenta(s) listas para notificar.", cuentas.size());
            int procesadas = 0;

            for (CuentaCorreo cuenta : cuentas) {
                try {
                    telegramService.enviarAlerta(cuenta);
                    cuentaService.marcarNotificada(cuenta);
                    procesadas++;
                    log.info("✅ Notificada y marcada: {}", cuenta.getEmail());
                } catch (Exception e) {
                    // Un error en una cuenta no detiene las demás
                    log.error("❌ Error procesando {}: {}", cuenta.getEmail(), e.getMessage());
                }
            }

            return procesadas;
        } catch (Exception e) {
            log.error("❌ Error general en scheduler: {}", e.getMessage());
            return 0;
        }
    }
}
