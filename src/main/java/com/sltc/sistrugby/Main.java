package com.sltc.sistrugby;

import com.sltc.sistrugby.persistencia.ConexionBD;
import com.sltc.sistrugby.presentacion.DatosInicialesSeeder;
import com.sltc.sistrugby.presentacion.MenuConsola;

public class Main {
    public static void main(String[] args) {
        try {
            ConexionBD bd = ConexionBD.getInstance();
            if (bd.isModoMemoria()) {
                System.out.println("[INFO] " + bd.getMotivoFallback());
                System.out.println("[INFO] Modo demo (en memoria). Cargando datos de prueba...");
                DatosInicialesSeeder.cargar();
                System.out.println("[INFO] Datos cargados. Credenciales de prueba:");
                System.out.println("       admin/admin2026 | entrenador1/rugby2026 | secretario1/sltc2026");
            } else {
                System.out.println("[INFO] Conectado a MySQL (modo produccion). "
                        + "Los datos provienen de la base sistrugby_sltc.");
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
