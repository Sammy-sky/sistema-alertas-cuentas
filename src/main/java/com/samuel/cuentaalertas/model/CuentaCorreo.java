package com.samuel.cuentaalertas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "cuentas_correo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "app_plataforma", nullable = false)
    private String appPlataforma;

    @Column(nullable = false)
    private String proyecto;

    @Column(name = "perfil_navegador", nullable = false)
    private String perfilNavegador;

    @Column(name = "fecha_reactivacion", nullable = false)
    private ZonedDateTime fechaReactivacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado = EstadoCuenta.BLOQUEADA;

    @Column(name = "notificado_en")
    private ZonedDateTime notificadoEn;

    @Column(columnDefinition = "TEXT")
    private String notas;
}
