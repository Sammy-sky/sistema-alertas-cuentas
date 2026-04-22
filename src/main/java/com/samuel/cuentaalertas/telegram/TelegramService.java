package com.samuel.cuentaalertas.telegram;

import com.samuel.cuentaalertas.model.CuentaCorreo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class TelegramService {

    private final WebClient webClient;
    private final String chatId;

    public TelegramService(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.chatId}") String chatId) {
        this.chatId = chatId;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token)
                .build();
    }

    public void enviarAlerta(CuentaCorreo cuenta) {
        String mensaje = construirMensaje(cuenta);

        try {
            webClient.post()
                    .uri("/sendMessage")
                    .bodyValue(Map.of(
                            "chat_id", chatId,
                            "text", mensaje,
                            "parse_mode", "HTML"
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(r -> log.info("📨 Telegram enviado para: {}", cuenta.getEmail()))
                    .doOnError(e -> log.error("❌ Error enviando Telegram: {}", e.getMessage()))
                    .subscribe();
        } catch (Exception e) {
            log.error("❌ Excepción en TelegramService: {}", e.getMessage());
        }
    }

    private String construirMensaje(CuentaCorreo cuenta) {
        return """
                🔓 <b>Cuenta disponible</b>

                📧 <b>Email:</b> %s
                🚀 <b>App:</b> %s
                📁 <b>Proyecto:</b> %s
                🌐 <b>Perfil:</b> %s
                📝 <b>Notas:</b> %s

                ✅ Ya puedes usarla
                """.formatted(
                cuenta.getEmail(),
                cuenta.getAppPlataforma(),
                cuenta.getProyecto(),
                cuenta.getPerfilNavegador(),
                cuenta.getNotas() != null ? cuenta.getNotas() : "Sin notas"
        );
    }
}