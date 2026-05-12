package com.sltc.sistrugby.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Proveedor de conexiones JDBC a MySQL.
 *
 * Patrón: Factory Method.
 * La contraseña se lee de la variable de entorno DB_PASS
 * para evitar credenciales en el código fuente.
 *
 * Uso:
 *   try (Connection conn = ConexionDB.obtenerConexion()) { ... }
 */
public class ConexionDB {

    private static final String URL =
        "jdbc:mysql://localhost:3306/sistrugby_sltc"
        + "?useSSL=false"
        + "&serverTimezone=UTC"
        + "&characterEncoding=UTF-8"
        + "&allowPublicKeyRetrieval=true";

    private static final String USUARIO  = "sistrugby_user";
    private static final String PASSWORD = System.getenv("DB_PASS");

    /** Impide la instanciación directa. */
    private ConexionDB() {}

    /**
     * Retorna una nueva conexión JDBC al esquema sistrugby_sltc.
     *
     * @throws SQLException si la conexión falla (credenciales,
     *                       servidor no disponible, etc.)
     */
    public static Connection obtenerConexion() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                "Variable de entorno DB_PASS no definida. "
                + "Definila antes de ejecutar la aplicación.");
        }
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
