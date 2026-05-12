package com.sltc.sistrugby.negocio;

import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.persistencia.dao.JugadorDAO;
import com.sltc.sistrugby.persistencia.impl.JugadorDAOImpl;

import java.util.List;

/**
 * Lógica de negocio para la gestión del padrón de jugadores.
 * Valida reglas de negocio antes de delegar al DAO.
 */
public class JugadorService {

    private final JugadorDAO jugadorDAO;

    public JugadorService() {
        this.jugadorDAO = new JugadorDAOImpl();
    }

    // Inyección de dependencias para testing
    public JugadorService(JugadorDAO jugadorDAO) {
        this.jugadorDAO = jugadorDAO;
    }

    /**
     * Registra un nuevo jugador en el padrón.
     * Valida campos obligatorios y unicidad de DNI.
     *
     * @throws IllegalArgumentException si faltan datos obligatorios.
     * @throws IllegalStateException    si el DNI ya está registrado.
     */
    public void registrar(Jugador jugador) throws Exception {
        validarCamposObligatorios(jugador);

        Jugador existente = jugadorDAO.buscarPorDni(jugador.getDni());
        if (existente != null) {
            throw new IllegalStateException(
                "El DNI " + jugador.getDni() + " ya está registrado en el sistema.");
        }

        jugadorDAO.insertar(jugador);
    }

    /**
     * Modifica los datos de un jugador existente.
     *
     * @throws IllegalArgumentException si el jugador no existe.
     */
    public void modificar(Jugador jugador) throws Exception {
        validarCamposObligatorios(jugador);

        Jugador existente = jugadorDAO.buscarPorId(jugador.getId());
        if (existente == null) {
            throw new IllegalArgumentException(
                "No existe un jugador con id=" + jugador.getId());
        }

        jugadorDAO.actualizar(jugador);
    }

    /**
     * Aplica la baja lógica a un jugador activo.
     *
     * @throws IllegalStateException si el jugador ya está inactivo.
     */
    public void darDeBaja(int idJugador) throws Exception {
        Jugador jugador = jugadorDAO.buscarPorId(idJugador);
        if (jugador == null) {
            throw new IllegalArgumentException(
                "No existe un jugador con id=" + idJugador);
        }
        if (!jugador.estaActivo()) {
            throw new IllegalStateException(
                "El jugador " + jugador.getNombreCompleto() + " ya está inactivo.");
        }

        jugadorDAO.darDeBaja(idJugador);
    }

    /** Busca jugador por DNI. Retorna null si no existe. */
    public Jugador buscarPorDni(String dni) throws Exception {
        return jugadorDAO.buscarPorDni(dni);
    }

    /** Retorna la lista de jugadores activos ordenados por apellido. */
    public List<Jugador> listarActivos() throws Exception {
        return jugadorDAO.listarActivos();
    }

    /** Retorna los jugadores activos de una división específica. */
    public List<Jugador> listarActivosPorDivision(int idDivision) throws Exception {
        return jugadorDAO.listarActivosPorDivision(idDivision);
    }

    // ── validaciones privadas ─────────────────────────────────────────────────

    private void validarCamposObligatorios(Jugador j) {
        if (j.getNombre()          == null || j.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del jugador es obligatorio.");
        if (j.getApellido()        == null || j.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido del jugador es obligatorio.");
        if (j.getDni()             == null || j.getDni().isBlank())
            throw new IllegalArgumentException("El DNI del jugador es obligatorio.");
        if (j.getFechaNacimiento() == null)
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        if (j.getIdDivision()      <= 0)
            throw new IllegalArgumentException("La división del jugador es obligatoria.");
    }
}
