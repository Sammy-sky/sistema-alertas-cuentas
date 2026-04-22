package com.samuel.cuentaalertas.model;

import lombok.Data;

@Data
public class CuentaRequest {

    private String email;
    private String appPlataforma;
    private String proyecto;
    private String perfilNavegador;
    private String notas;

    // Modo 1: fecha y hora exacta (viene como String del formulario)
    private String fechaReactivacion;   // formato: "2026-04-22T18:00"

    // Modo 2: duración en horas desde ahora
    private Integer horasBloqueada;     // ej: 24
}