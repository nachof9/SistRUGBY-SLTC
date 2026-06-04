package com.sltc.sistrugby.excepciones;

/**
 * Excepción de dominio: se lanza cuando se intenta registrar un jugador
 * cuyo DNI ya existe en el padrón. Se trata como una excepción verificada
 * (checked) para forzar al cliente a manejarla.
 */
public class DniDuplicadoException extends Exception {

    public DniDuplicadoException(String dni) {
        super("El DNI " + dni + " ya se encuentra registrado en el padrón.");
    }
}
