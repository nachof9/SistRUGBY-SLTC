package com.sltc.sistrugby.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidad de hashing de contraseñas con PBKDF2-HMAC-SHA256.
 *
 * Se prefirió PBKDF2 (disponible en el JDK estándar, paquete javax.crypto)
 * sobre bcrypt para mantener el prototipo libre de dependencias externas
 * sin sacrificar la robustez criptográfica. PBKDF2 es el algoritmo
 * recomendado por NIST SP 800-132 para almacenamiento de contraseñas
 * cuando bcrypt o Argon2 no están disponibles.
 *
 * Formato de hash almacenado: "ITER:SALT_BASE64:HASH_BASE64"
 */
public final class HashUtil {

    private static final int ITERACIONES = 100_000;
    private static final int LONGITUD_HASH = 256;  // bits
    private static final int LONGITUD_SALT = 16;   // bytes
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";

    private HashUtil() { /* utilitaria */ }

    /**
     * Genera el hash PBKDF2 de la contraseña con un salt aleatorio.
     * @param contrasenaPlana contraseña en texto plano (jamás se almacena).
     * @return cadena en formato "iter:saltBase64:hashBase64".
     */
    public static String hashear(String contrasenaPlana) {
        try {
            byte[] salt = new byte[LONGITUD_SALT];
            new SecureRandom().nextBytes(salt);

            byte[] hash = pbkdf2(contrasenaPlana.toCharArray(), salt,
                    ITERACIONES, LONGITUD_HASH);

            return ITERACIONES + ":" + Base64.getEncoder().encodeToString(salt)
                    + ":" + Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Error de hashing: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica una contraseña en texto plano contra un hash almacenado.
     * @return true si coinciden, false en caso contrario.
     */
    public static boolean verificar(String contrasenaPlana, String hashAlmacenado) {
        if (hashAlmacenado == null || hashAlmacenado.isEmpty()) return false;
        try {
            String[] partes = hashAlmacenado.split(":");
            if (partes.length != 3) return false;

            int iter = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);

            byte[] candidato = pbkdf2(contrasenaPlana.toCharArray(), salt,
                    iter, esperado.length * 8);

            return constantesIguales(esperado, candidato);

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] contrasena, byte[] salt,
                                 int iteraciones, int longitudBits)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(contrasena, salt, iteraciones, longitudBits);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITMO);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Comparación en tiempo constante para evitar timing attacks.
     */
    private static boolean constantesIguales(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
