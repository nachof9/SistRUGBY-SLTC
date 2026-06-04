package com.sltc.sistrugby.excepciones;

/**
 * Excepción de dominio: se lanza cuando las credenciales presentadas
 * en el inicio de sesión no coinciden con ningún usuario activo.
 */
public class CredencialesInvalidasException extends Exception {

    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos.");
    }
}
