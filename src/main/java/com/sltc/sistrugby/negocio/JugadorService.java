package com.sltc.sistrugby.negocio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.sltc.sistrugby.excepciones.DatosInvalidosException;
import com.sltc.sistrugby.excepciones.DniDuplicadoException;
import com.sltc.sistrugby.excepciones.JugadorNoEncontradoException;
import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.persistencia.JugadorDAO;
import com.sltc.sistrugby.persistencia.JugadorDAOInterface;
import com.sltc.sistrugby.util.BuscadorJugadores;
import com.sltc.sistrugby.util.OrdenadorJugadores;
import com.sltc.sistrugby.util.ValidadorDNI;

/**
 * Servicio de negocio: alta, baja, modificación y consulta de jugadores.
 * Centraliza las validaciones y delega la persistencia en {@link JugadorDAO}.
 */
public class JugadorService {

    private final JugadorDAOInterface dao = new JugadorDAO();

    public Jugador registrar(String nombre, String apellido, String dni,
                             LocalDate fechaNacimiento, String posicion,
                             int idCategoria)
            throws DatosInvalidosException, DniDuplicadoException, SQLException {

        // Validación de datos obligatorios
        if (nombre == null || nombre.isBlank())
            throw new DatosInvalidosException("Nombre obligatorio");
        if (apellido == null || apellido.isBlank())
            throw new DatosInvalidosException("Apellido obligatorio");
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now()))
            throw new DatosInvalidosException("Fecha de nacimiento inválida");
        if (idCategoria <= 0)
            throw new DatosInvalidosException("Categoría inválida");
        ValidadorDNI.validar(dni);

        // Regla de negocio: DNI único
        if (dao.findByDni(dni) != null) throw new DniDuplicadoException(dni);

        Jugador j = new Jugador(nombre.trim(), apellido.trim(), dni.trim(),
                fechaNacimiento, posicion, idCategoria);
        return dao.insertar(j);
    }

    public void darDeBaja(int idJugador)
            throws JugadorNoEncontradoException, SQLException {
        Jugador j = buscarPorId(idJugador);
        if (j == null) throw new JugadorNoEncontradoException("id=" + idJugador);
        dao.darDeBaja(idJugador);
    }

    public Jugador buscarPorDni(String dni) throws SQLException {
        return dao.findByDni(dni);
    }

    public Jugador buscarPorId(int id) throws SQLException {
        for (Jugador j : dao.findAll()) if (j.getId() == id) return j;
        return null;
    }

    public List<Jugador> listarTodos() throws SQLException {
        return dao.findAll();
    }

    public List<Jugador> listarPorCategoria(int idCategoria) throws SQLException {
        return dao.findByCategoria(idCategoria);
    }

    /**
     * Devuelve los jugadores ordenados alfabéticamente por apellido + nombre.
     * Usa el {@link OrdenadorJugadores} (QuickSort manual).
     */
    public List<Jugador> listarOrdenadoPorApellido() throws SQLException {
        List<Jugador> lista = dao.findAll();
        OrdenadorJugadores.ordenarPorApellido(lista);
        return lista;
    }

    /**
     * Búsqueda binaria por DNI: O(log n) tras ordenamiento previo.
     */
    public Jugador buscarRapidoPorDni(String dni) throws SQLException {
        List<Jugador> lista = dao.findAll();
        BuscadorJugadores.ordenarPorDni(lista);
        return BuscadorJugadores.buscarPorDni(lista, dni);
    }
}
