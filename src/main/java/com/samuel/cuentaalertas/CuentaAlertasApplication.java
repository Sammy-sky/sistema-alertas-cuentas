package com.samuel.cuentaalertas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CuentaAlertasApplication {
    public static void main(String[] args) {
        SpringApplication.run(CuentaAlertasApplication.class, args);
    }
}