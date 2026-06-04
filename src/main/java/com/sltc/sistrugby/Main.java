package com.sltc.sistrugby;

import com.sltc.sistrugby.persistencia.ConexionBD;
import com.sltc.sistrugby.presentacion.DatosInicialesSeeder;
import com.sltc.sistrugby.presentacion.MenuConsola;

/**
 * Punto de entrada del prototipo SistRUGBY-SLTC.
 *
 * Estrategia:
 *   - Si la variable de entorno DB_PASS está definida, intenta abrir
 *     conexión real con MySQL (modo producción).
 *   - Si no está definida, activa el modo memoria y precarga datos de
 *     demo para que el menú interactivo sea autosuficiente.
 *
 * Para correr con MySQL real:
 *   $ export DB_PASS='tu_password'
 *   $ java -cp out:lib/mysql-connector-j-8.3.0.jar com.sltc.sistrugby.Main
 *
 * Para correr en modo demo:
 *   $ java -cp out com.sltc.sistrugby.Main
 */
public class Main {

    public static void main(String[] args) {
        try {
            String dbPass = System.getenv("DB_PASS");
            if (dbPass == null || dbPass.isEmpty()) {
                ConexionBD.activarModoMemoria();
                System.out.println("[INFO] Modo demo (sin MySQL). Cargando datos de prueba...");
                DatosInicialesSeeder.cargar();
                System.out.println("[INFO] Datos cargados. Credenciales de prueba:");
                System.out.println("       admin/admin2026 | entrenador1/rugby2026 | secretario1/sltc2026");
            } else {
                ConexionBD.getInstance();
                System.out.println("[INFO] Conectado a MySQL.");
            }

            new MenuConsola().iniciar();

        } catch (Exception e) {
            System.err.println("[FATAL] " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { ConexionBD.getInstance().cerrar(); }
            catch (Exception ignored) { }
        }
    }
}
