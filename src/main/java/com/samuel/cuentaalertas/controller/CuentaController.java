package com.samuel.cuentaalertas.controller;

import com.samuel.cuentaalertas.model.CuentaCorreo;
import com.samuel.cuentaalertas.model.CuentaRequest;
import com.samuel.cuentaalertas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    // GET /api/cuentas → lista todas
    @GetMapping
    public ResponseEntity<List<CuentaCorreo>> listarTodas() {
        return ResponseEntity.ok(cuentaService.listarTodas());
    }

    // GET /api/cuentas/{id} → busca una
    @GetMapping("/{id}")
    public ResponseEntity<CuentaCorreo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.buscarPorId(id));
    }

    // POST /api/cuentas → crea nueva
    @PostMapping
    public ResponseEntity<CuentaCorreo> crear(@RequestBody CuentaRequest request) {
        CuentaCorreo nueva = cuentaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // PUT /api/cuentas/{id} → edita existente
    @PutMapping("/{id}")
    public ResponseEntity<CuentaCorreo> editar(
            @PathVariable Long id,
            @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(cuentaService.editar(id, request));
    }

    // DELETE /api/cuentas/{id} → elimina
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cuenta eliminada correctamente"));
    }

    // ── Manejo global de errores ───────────────────────────────
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleError(RuntimeException ex) {
        log.error("❌ Error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}