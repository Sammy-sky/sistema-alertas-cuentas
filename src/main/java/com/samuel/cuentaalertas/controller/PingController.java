package com.samuel.cuentaalertas.controller;

import com.samuel.cuentaalertas.model.CuentaCorreo;
import com.samuel.cuentaalertas.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PingController {

    private final TelegramService telegramService;

    @GetMapping("/api/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of("status", "alive"));
    }

    @GetMapping("/api/test-telegram")
    public ResponseEntity<Map<String, String>> testTelegram() {
        CuentaCorreo demo = new CuentaCorreo();
        demo.setEmail("prueba@zoho.com");
        demo.setAppPlataforma("Zoho Mail");
        demo.setProyecto("Cliente Restaurante");
        demo.setPerfilNavegador("Chrome Perfil 2");
        demo.setNotas("Tiene 2FA activado");

        telegramService.enviarAlerta(demo);
        return ResponseEntity.ok(Map.of("mensaje", "Telegram enviado, revisa tu celular"));
    }
}