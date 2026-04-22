package com.samuel.cuentaalertas.repository;

import com.samuel.cuentaalertas.model.CuentaCorreo;
import com.samuel.cuentaalertas.model.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaCorreo, Long> {

    // Busca cuentas que ya cumplieron su tiempo y aún no fueron notificadas
    @Query("SELECT c FROM CuentaCorreo c WHERE c.estado = :estado AND c.fechaReactivacion <= :ahora")
    List<CuentaCorreo> findCuentasParaNotificar(EstadoCuenta estado, ZonedDateTime ahora);

    // Lista todas las cuentas por estado
    List<CuentaCorreo> findByEstado(EstadoCuenta estado);
}