package com.sltc.sistrugby.util;

import com.sltc.sistrugby.excepciones.DatosInvalidosException;

/**
 * Utilitario de validación de datos personales del dominio rugby argentino.
 */
public final class ValidadorDNI {

    private ValidadorDNI() { }

    /**
     * Valida el formato de un DNI argentino: cadena numérica de 7 u 8 dígitos.
     * @throws DatosInvalidosException si el DNI no cumple el formato.
     */
    public static void validar(String dni) throws DatosInvalidosException {
        if (dni == null || dni.isBlank()) {
            throw new DatosInvalidosException("El DNI es obligatorio.");
        }
        if (!dni.matches("\\d{7,8}")) {
            throw new DatosInvalidosException(
                    "El DNI debe contener únicamente 7 u 8 dígitos: '" + dni + "'");
        }
    }
}
