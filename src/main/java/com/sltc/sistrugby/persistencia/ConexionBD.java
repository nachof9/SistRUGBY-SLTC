package com.sltc.sistrugby.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {
    private static ConexionBD instancia;
    private static final String URL_DEFAULT =
            "jdbc:mysql://localhost:3306/sistrugby_sltc"
                    + "?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER_DEFAULT = "sistrugby_user";

    private Connection connection;
    private boolean modoMemoria = false;
    private String motivoFallback;

    private ConexionBD() {
        String url  = System.getenv().getOrDefault("DB_URL", URL_DEFAULT);
        String user = System.getenv().getOrDefault("DB_USER", USER_DEFAULT);
        String pass = System.getenv().getOrDefault("DB_PASS", "");
        if (pass.isEmpty()) {
            this.modoMemoria = true;
            this.motivoFallback = "DB_PASS no definida (modo demo en memoria).";
            return;
        }
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
            this.modoMemoria = false;
        } catch (SQLException e) {
            this.modoMemoria = true;
            this.motivoFallback = "No se pudo conectar a MySQL (" + e.getMessage()
                    + "). Se activa modo memoria.";
        }
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

    public static synchronized void activarModoMemoria() {
        if (instancia == null) instancia = new ConexionBD();
        try {
            if (instancia.connection != null && !instancia.connection.isClosed()) {
                instancia.connection.close();
            }
        } catch (SQLException ignored) { }
        instancia.connection = null;
        instancia.modoMemoria = true;
        if (instancia.motivoFallback == null) {
            instancia.motivoFallback = "Modo memoria activado explicitamente.";
        }
    }

    public Connection getConnection() { return connection; }
    public boolean isModoMemoria() { return modoMemoria; }
    public String getMotivoFallback() { return motivoFallback; }
    public void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
