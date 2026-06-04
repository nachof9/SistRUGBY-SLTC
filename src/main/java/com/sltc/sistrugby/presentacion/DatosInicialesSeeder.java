package com.sltc.sistrugby.presentacion;

import java.sql.SQLException;
import java.time.LocalDate;

import com.sltc.sistrugby.modelo.Categoria;
import com.sltc.sistrugby.modelo.Club;
import com.sltc.sistrugby.modelo.Temporada;
import com.sltc.sistrugby.modelo.Usuario;
import com.sltc.sistrugby.negocio.JugadorService;
import com.sltc.sistrugby.negocio.UsuarioService;
import com.sltc.sistrugby.persistencia.CategoriaDAO;
import com.sltc.sistrugby.persistencia.ClubDAO;
import com.sltc.sistrugby.persistencia.RepositorioMemoria;

/**
 * Carga datos iniciales en modo memoria para que el menú interactivo
 * tenga categorías, clubes, usuarios y jugadores de prueba.
 */
public final class DatosInicialesSeeder {

    private DatosInicialesSeeder() { }

    public static void cargar() throws Exception {
        CategoriaDAO catDao = new CategoriaDAO();
        ClubDAO clubDao = new ClubDAO();
        UsuarioService usuarioSvc = new UsuarioService();
        JugadorService jugadorSvc = new JugadorService();

        // ----- Categorías -----
        catDao.insertar(new Categoria(1, "M15",  Categoria.Tipo.JUVENIL, true));
        catDao.insertar(new Categoria(2, "M16",  Categoria.Tipo.JUVENIL, true));
        catDao.insertar(new Categoria(3, "M17",  Categoria.Tipo.JUVENIL, true));
        catDao.insertar(new Categoria(4, "M18",  Categoria.Tipo.JUVENIL, true));
        catDao.insertar(new Categoria(5, "M19",  Categoria.Tipo.JUVENIL, true));
        catDao.insertar(new Categoria(6, "Pre-Intermedia",
                Categoria.Tipo.PLANTEL_SUPERIOR, true));
        catDao.insertar(new Categoria(7, "Intermedia",
                Categoria.Tipo.PLANTEL_SUPERIOR, true));
        catDao.insertar(new Categoria(8, "Primera",
                Categoria.Tipo.PLANTEL_SUPERIOR, true));

        // ----- Clubes -----
        clubDao.insertar(new Club(1, "Lince Rugby Club",          "USR", null, true));
        clubDao.insertar(new Club(2, "Santiago Rugby Club",       "USR", null, true));
        clubDao.insertar(new Club(3, "Catamarca Rugby Club",      "URC", null, true));
        clubDao.insertar(new Club(4, "Tucumán Rugby Club",        "URT", null, true));
        clubDao.insertar(new Club(5, "Club Atlético del Norte",   "USR", null, true));

        // ----- Temporadas -----
        RepositorioMemoria.get().temporadas().put(1, new Temporada(1, 2024, "Temporada 2024"));
        RepositorioMemoria.get().temporadas().put(2, new Temporada(2, 2025, "Temporada 2025"));
        RepositorioMemoria.get().temporadas().put(3, new Temporada(3, 2026, "Temporada 2026"));

        // ----- Usuarios -----
        usuarioSvc.registrar("admin",        "admin2026", Usuario.Rol.ADMINISTRADOR);
        usuarioSvc.registrar("entrenador1",  "rugby2026", Usuario.Rol.ENTRENADOR);
        usuarioSvc.registrar("secretario1",  "sltc2026",  Usuario.Rol.SECRETARIO);

        // ----- Jugadores (5 de Primera) -----
        try {
            jugadorSvc.registrar("Rodrigo",   "Pereyra",  "35211001",
                    LocalDate.of(2000, 3, 14),  "Pilar", 8);
            jugadorSvc.registrar("Matías",    "Villalba", "36089452",
                    LocalDate.of(2001, 7, 22),  "Hooker", 8);
            jugadorSvc.registrar("Federico",  "Casas",    "34567890",
                    LocalDate.of(1999, 11, 5),  "Apertura", 8);
            jugadorSvc.registrar("Gonzalo",   "Herrera",  "37124500",
                    LocalDate.of(2002, 2, 18),  "Ala derecho", 8);
            jugadorSvc.registrar("Tomás",     "Ruiz",     "35980012",
                    LocalDate.of(2000, 9, 30),  "Zaguero", 8);
        } catch (SQLException e) {
            throw new RuntimeException("Error cargando jugadores seed", e);
        }
    }
}
