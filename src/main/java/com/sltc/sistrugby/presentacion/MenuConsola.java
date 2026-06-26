package com.sltc.sistrugby.presentacion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sltc.sistrugby.excepciones.CredencialesInvalidasException;
import com.sltc.sistrugby.excepciones.DatosInvalidosException;
import com.sltc.sistrugby.excepciones.DniDuplicadoException;
import com.sltc.sistrugby.excepciones.JugadorNoEncontradoException;
import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.PlantelPartido;
import com.sltc.sistrugby.modelo.Usuario;
import com.sltc.sistrugby.modelo.eventos.EventoPartido;
import com.sltc.sistrugby.negocio.EstadisticaService;
import com.sltc.sistrugby.negocio.EventoService;
import com.sltc.sistrugby.negocio.JugadorService;
import com.sltc.sistrugby.negocio.PartidoService;
import com.sltc.sistrugby.negocio.ReporteService;
import com.sltc.sistrugby.negocio.UsuarioService;

public class MenuConsola {

    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UsuarioService     usuarioSvc   = new UsuarioService();
    private final JugadorService     jugadorSvc   = new JugadorService();
    private final PartidoService     partidoSvc   = new PartidoService();
    private final EventoService      eventoSvc    = new EventoService();
    private final EstadisticaService estadSvc     = new EstadisticaService();
    private final ReporteService     reporteSvc   = new ReporteService();

    private Usuario usuarioActual;
    private int intentosFallidos = 0;

    public void iniciar() {
        System.out.println("===========================================================");
        System.out.println(" SistRUGBY-SLTC | Santiago Lawn Tennis Club | Prototipo AP4 ");
        System.out.println("===========================================================");
        while (usuarioActual == null) {
            if (intentosFallidos >= 3) {
                System.out.println("\n[BLOQUEO] Tres intentos fallidos. Saliendo.");
                return;
            }
            iniciarSesion();
        }
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: registrarJugador();      break;
                case 2: listarJugadores();       break;
                case 3: buscarJugadorPorDni();   break;
                case 4: registrarPartido();      break;
                case 5: registrarPlantel();      break;
                case 6: registrarEvento();       break;
                case 7: deshacerUltimoEvento();  break;
                case 8: mostrarRanking();        break;
                case 9: darDeBajaJugador();      break;
                case 10: exportarReporte();      break;
                case 0: salir = true;            break;
                default: System.out.println("Opción inválida.");
            }
        }
        System.out.println("Sesión finalizada. ¡Hasta la próxima!");
    }

    private void iniciarSesion() {
        System.out.println("\n--- Iniciar sesión ---");
        System.out.print("Usuario: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        try {
            usuarioActual = usuarioSvc.autenticar(user, pass);
            System.out.println("Bienvenido, " + usuarioActual.getNombreUsuario()
                    + " [" + usuarioActual.getRol() + "]");
            intentosFallidos = 0;
        } catch (CredencialesInvalidasException e) {
            intentosFallidos++;
            System.out.println("Credenciales inválidas. Intentos restantes: "
                    + (3 - intentosFallidos));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("[1] Registrar jugador (CU02)");
        System.out.println("[2] Listar jugadores (ordenado por apellido)");
        System.out.println("[3] Buscar jugador por DNI (búsqueda binaria)");
        System.out.println("[4] Registrar partido (CU05)");
        System.out.println("[5] Registrar plantel de partido (CU06)");
        System.out.println("[6] Registrar evento de partido (CU07)");
        System.out.println("[7] Deshacer último evento (PILA undo)");
        System.out.println("[8] Ranking de jugadores por puntos (CU08)");
        System.out.println("[9] Dar de baja jugador (CU04)");
        System.out.println("[10] Exportar reporte a archivo (CU09)");
        System.out.println("[0] Salir");
    }

    private void registrarJugador() {
        System.out.println("\n--- CU02 Registrar jugador ---");
        try {
            System.out.print("Nombre: ");      String nom = sc.nextLine();
            System.out.print("Apellido: ");    String ape = sc.nextLine();
            System.out.print("DNI: ");         String dni = sc.nextLine();
            System.out.print("Fecha nac. (YYYY-MM-DD): ");
            LocalDate fn = LocalDate.parse(sc.nextLine(), FMT);
            System.out.print("Posición: ");    String pos = sc.nextLine();
            System.out.print("ID Categoría (1..8): ");
            int cat = Integer.parseInt(sc.nextLine());
            Jugador j = jugadorSvc.registrar(nom, ape, dni, fn, pos, cat);
            System.out.println("OK. Jugador creado: " + j.descripcionCorta());
        } catch (DatosInvalidosException | DniDuplicadoException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido (esperado YYYY-MM-DD).");
        } catch (NumberFormatException e) {
            System.out.println("ID de categoría debe ser numérico.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private void listarJugadores() {
        System.out.println("\n--- Padrón de jugadores (ordenado QuickSort) ---");
        try {
            List<Jugador> lista = jugadorSvc.listarOrdenadoPorApellido();
            if (lista.isEmpty()) { System.out.println("(Padrón vacío)"); return; }
            for (Jugador j : lista) System.out.println("  " + j);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarJugadorPorDni() {
        System.out.println("\n--- Búsqueda binaria por DNI ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        try {
            Jugador j = jugadorSvc.buscarRapidoPorDni(dni);
            System.out.println(j == null ? "No encontrado." : "Encontrado: " + j);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarPartido() {
        System.out.println("\n--- CU05 Registrar partido ---");
        try {
            System.out.print("Fecha (YYYY-MM-DD): ");
            LocalDate f = LocalDate.parse(sc.nextLine(), FMT);
            System.out.print("ID Club rival (1..5): ");
            int rival = Integer.parseInt(sc.nextLine());
            System.out.print("ID Categoría (1..8): ");
            int cat = Integer.parseInt(sc.nextLine());
            System.out.print("ID Temporada (1..3): ");
            int temp = Integer.parseInt(sc.nextLine());
            System.out.print("Sede (LOCAL/VISITANTE): ");
            Partido.Sede sede = Partido.Sede.valueOf(sc.nextLine().toUpperCase());
            Partido p = partidoSvc.registrar(f, rival, cat, temp, sede);
            System.out.println("OK. Partido creado: " + p);
        } catch (DatosInvalidosException e) {
            System.out.println("Validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarPlantel() {
        System.out.println("\n--- CU06 Registrar plantel ---");
        try {
            System.out.print("ID Partido: ");
            int idP = Integer.parseInt(sc.nextLine());
            System.out.print("IDs Jugadores titulares (separados por coma): ");
            String[] titIds = sc.nextLine().split(",");
            System.out.print("IDs Jugadores suplentes (separados por coma, vacío si no hay): ");
            String[] supIds = sc.nextLine().split(",");
            List<PlantelPartido> plantel = new ArrayList<>();
            for (String s : titIds) {
                if (!s.isBlank())
                    plantel.add(new PlantelPartido(idP, Integer.parseInt(s.trim()),
                            PlantelPartido.Condicion.TITULAR));
            }
            for (String s : supIds) {
                if (!s.isBlank())
                    plantel.add(new PlantelPartido(idP, Integer.parseInt(s.trim()),
                            PlantelPartido.Condicion.SUPLENTE));
            }
            partidoSvc.registrarPlantel(idP, plantel);
            System.out.println("OK. Plantel cargado con " + plantel.size() + " jugadores.");
        } catch (DatosInvalidosException e) {
            System.out.println("Validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarEvento() {
        System.out.println("\n--- CU07 Registrar evento (polimórfico) ---");
        try {
            System.out.print("ID Partido: ");
            int idP = Integer.parseInt(sc.nextLine());
            System.out.print("ID Jugador: ");
            int idJ = Integer.parseInt(sc.nextLine());
            System.out.println("Tipos: TRY, CONVERSION, PENAL, DROP, "
                    + "TARJETA_AMARILLA, TARJETA_ROJA, SUSTITUCION");
            System.out.print("Tipo: ");
            EventoPartido.Tipo t = EventoPartido.Tipo.valueOf(sc.nextLine().toUpperCase());
            System.out.print("Minuto (0-90): ");
            int min = Integer.parseInt(sc.nextLine());
            EventoPartido e = eventoSvc.registrarEvento(t, idP, idJ, min);
            System.out.println("OK. Evento registrado: " + e);
        } catch (DatosInvalidosException e) {
            System.out.println("Validación: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deshacerUltimoEvento() {
        System.out.println("\n--- Deshacer último evento (PILA undo LIFO) ---");
        try {
            EventoPartido e = eventoSvc.deshacerUltimo();
            System.out.println(e == null ? "Nada para deshacer." : "Deshecho: " + e);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void mostrarRanking() {
        System.out.println("\n--- Ranking por puntos (POLIMORFISMO en calcularPuntos) ---");
        try {
            List<EstadisticaService.FilaRanking> ranking = estadSvc.rankingPorPuntos();
            if (ranking.isEmpty()) { System.out.println("Sin eventos aún."); return; }
            System.out.printf("%-25s | %-5s | %-5s | %-5s | %-5s | %s%n",
                    "JUGADOR", "TRY", "CONV", "PEN", "DROP", "PUNTOS");
            System.out.println("---------------------------------------------------------------");
            for (EstadisticaService.FilaRanking f : ranking) {
                String nom = f.jugador != null && f.jugador.getNombreCompleto() != null
                        ? f.jugador.getNombreCompleto() : "#" + f.jugador.getId();
                System.out.printf("%-25s | %-5d | %-5d | %-5d | %-5d | %d%n",
                        nom, f.tries, f.conversiones, f.penales, f.drops, f.puntosTotal);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void darDeBajaJugador() {
        System.out.println("\n--- CU04 Dar de baja jugador ---");
        try {
            System.out.print("ID Jugador: ");
            int id = Integer.parseInt(sc.nextLine());
            jugadorSvc.darDeBaja(id);
            System.out.println("OK. Jugador #" + id + " ahora está INACTIVO.");
        } catch (JugadorNoEncontradoException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void exportarReporte() {
        System.out.println("\n--- CU09 Exportar reporte a archivo ---");
        System.out.print("Nombre de archivo (enter = reporte_sltc.txt): ");
        String ruta = sc.nextLine().trim();
        if (ruta.isEmpty()) ruta = "reporte_sltc.txt";
        try {
            String absoluta = reporteSvc.exportarReporte(ruta);
            System.out.println("OK. Reporte escrito en: " + absoluta);
        } catch (java.io.IOException e) {
            System.out.println("Error de escritura del archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) {
                System.out.println("Ingresá un número entero válido.");
            }
        }
    }
}
