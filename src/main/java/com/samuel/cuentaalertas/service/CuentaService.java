package com.samuel.cuentaalertas.service;

import com.samuel.cuentaalertas.model.CuentaCorreo;
import com.samuel.cuentaalertas.model.CuentaRequest;
import com.samuel.cuentaalertas.model.EstadoCuenta;
import com.samuel.cuentaalertas.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private static final ZoneId LIMA = ZoneId.of("America/Lima");

    // ── Listar todas ───────────────────────────────────────────
    public List<CuentaCorreo> listarTodas() {
        return cuentaRepository.findAll();
    }

    // ── Buscar por ID ──────────────────────────────────────────
    public CuentaCorreo buscarPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));
    }

    // ── Crear nueva cuenta ─────────────────────────────────────
    public CuentaCorreo crear(CuentaRequest request) {
        CuentaCorreo cuenta = new CuentaCorreo();
        cuenta.setEmail(request.getEmail());
        cuenta.setAppPlataforma(request.getAppPlataforma());
        cuenta.setProyecto(request.getProyecto());
        cuenta.setPerfilNavegador(request.getPerfilNavegador());
        cuenta.setNotas(request.getNotas());
        cuenta.setEstado(EstadoCuenta.BLOQUEADA);

        // Doble modo de ingreso de fecha
        cuenta.setFechaReactivacion(resolverFecha(request));

        log.info("✅ Cuenta creada: {} - Reactiva: {}", cuenta.getEmail(), cuenta.getFechaReactivacion());
        return cuentaRepository.save(cuenta);
    }

    // ── Editar cuenta existente ────────────────────────────────
    public CuentaCorreo editar(Long id, CuentaRequest request) {
        CuentaCorreo cuenta = buscarPorId(id);
        cuenta.setEmail(request.getEmail());
        cuenta.setAppPlataforma(request.getAppPlataforma());
        cuenta.setProyecto(request.getProyecto());
        cuenta.setPerfilNavegador(request.getPerfilNavegador());
        cuenta.setNotas(request.getNotas());
        cuenta.setFechaReactivacion(resolverFecha(request));

        // Recalcular estado al editar
        cuenta.setEstado(EstadoCuenta.BLOQUEADA);
        cuenta.setNotificadoEn(null);

        log.info("✏️ Cuenta editada: {}", cuenta.getEmail());
        return cuentaRepository.save(cuenta);
    }

    // ── Eliminar cuenta ────────────────────────────────────────
    public void eliminar(Long id) {
        cuentaRepository.deleteById(id);
        log.info("🗑️ Cuenta eliminada ID: {}", id);
    }

    // ── Buscar cuentas listas para notificar ───────────────────
    public List<CuentaCorreo> buscarParaNotificar() {
        return cuentaRepository.findCuentasParaNotificar(
                EstadoCuenta.BLOQUEADA,
                ZonedDateTime.now(LIMA)
        );
    }

    // ── Marcar como notificada ─────────────────────────────────
    public void marcarNotificada(CuentaCorreo cuenta) {
        cuenta.setEstado(EstadoCuenta.NOTIFICADA);
        cuenta.setNotificadoEn(ZonedDateTime.now(LIMA));
        cuentaRepository.save(cuenta);
    }

    // ── Lógica de doble modo de fecha ──────────────────────────
    private ZonedDateTime resolverFecha(CuentaRequest request) {
        // Modo 2: si viene cantidad de horas, calcula desde ahora
        if (request.getHorasBloqueada() != null && request.getHorasBloqueada() > 0) {
            return ZonedDateTime.now(LIMA).plusHours(request.getHorasBloqueada());
        }
        // Modo 1: si viene fecha exacta como texto, la parsea
        if (request.getFechaReactivacion() != null && !request.getFechaReactivacion().isBlank()) {
            LocalDateTime ldt = LocalDateTime.parse(
                    request.getFechaReactivacion(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            );
            return ldt.atZone(LIMA);
        }
        throw new RuntimeException("Debes indicar fecha exacta o cantidad de horas.");
    }
}