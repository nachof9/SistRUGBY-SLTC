package com.sltc.sistrugby.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión a la base de datos MySQL implementada bajo el patrón SINGLETON.
 *
 * Una única instancia compartida por todos los DAOs evita el costo de
 * apertura/cierre repetitivo de conexiones JDBC.
 *
 * Modo dual:
 *   - PRODUCCIÓN: lee URL/usuario/contraseña de variables de entorno.
 *   - DEMO sin MySQL instalado: se puede invocar
 *     {@link #activarModoMemoria()} para que los Service/DAO trabajen
 *     contra un repositorio en memoria (clase {@link RepositorioMemoria}).
 *
 * El cambio de modo es transparente para el resto del sistema gracias
 * al patrón DAO: las interfaces se mantienen, cambian solo las
 * implementaciones concretas.
 */
public final class ConexionBD {

    private static ConexionBD instancia;

    private static final String URL_DEFAULT =
            "jdbc:mysql://localhost:3306/sistrugby_sltc"
                    + "?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER_DEFAULT = "sistrugby_user";

    private Connection connection;
    private boolean modoMemoria = false;

    private ConexionBD() throws SQLException {
        String url  = System.getenv().getOrDefault("DB_URL", URL_DEFAULT);
        String user = System.getenv().getOrDefault("DB_USER", USER_DEFAULT);
        String pass = System.getenv().getOrDefault("DB_PASS", "");

        if (pass.isEmpty()) {
            // Modo demo: sin MySQL disponible — no se abre conexión real.
            this.modoMemoria = true;
            return;
        }
        this.connection = DriverManager.getConnection(url, user, pass);
    }

    public static synchronized ConexionBD getInstance() throws SQLException {
        if (instancia == null) {
            instancia = new ConexionBD();
        } else if (!instancia.modoMemoria
                && (instancia.connection == null || instancia.connection.isClosed())) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Fuerza el modo memoria para entornos de demo / pruebas.
     * Una vez activado, los DAOs usarán {@link RepositorioMemoria}.
     */
    public static synchronized void activarModoMemoria() {
        try {
            if (instancia == null) instancia = new ConexionBD();
            instancia.modoMemoria = true;
            if (instancia.connection != null && !instancia.connection.isClosed()) {
                instancia.connection.close();
            }
            instancia.connection = null;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo activar modo memoria", e);
        }
    }

    public Connection getConnection() { return connection; }

    public boolean isModoMemoria() { return modoMemoria; }

    public void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
