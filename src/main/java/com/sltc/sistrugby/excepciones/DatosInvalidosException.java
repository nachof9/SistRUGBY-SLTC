package com.sltc.sistrugby.excepciones;

/**
 * Excepción de dominio: se lanza cuando los datos suministrados
 * no cumplen las reglas de validación del negocio (DNI con formato
 * incorrecto, fecha de nacimiento inválida, plantel sin titulares, etc.).
 */
public class DatosInvalidosException extends Exception {

    public DatosInvalidosException(String mensaje) {
        super("Datos inválidos: " + mensaje);
    }
}
