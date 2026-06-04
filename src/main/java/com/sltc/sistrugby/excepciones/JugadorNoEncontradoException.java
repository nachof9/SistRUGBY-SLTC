package com.sltc.sistrugby.excepciones;

/**
 * Excepción de dominio: se lanza cuando una operación referencia
 * un jugador inexistente en el sistema.
 */
public class JugadorNoEncontradoException extends Exception {

    public JugadorNoEncontradoException(String criterio) {
        super("No se encontró un jugador con el criterio: " + criterio);
    }
}
