package com.sltc.sistrugby.negocio;

import com.sltc.sistrugby.modelo.EventoPartido;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.persistencia.ConexionDB;
import com.sltc.sistrugby.persistencia.dao.PartidoDAO;
import com.sltc.sistrugby.persistencia.impl.PartidoDAOImpl;

import java.sql.*;
import java.util.List;

/**
 * Lógica de negocio para la gestión de partidos y estadísticas.
 */
public class PartidoService {

    private final PartidoDAO partidoDAO;

    public PartidoService() {
        this.partidoDAO = new PartidoDAOImpl();
    }

    /**
     * Registra un nuevo partido. Retorna el id generado por MySQL.
     *
     * @throws IllegalArgumentException si faltan datos obligatorios.
     */
    public int registrarPartido(Partido partido) throws Exception {
        if (partido.getFecha()       == null) throw new IllegalArgumentException("La fecha del partido es obligatoria.");
        if (partido.getIdClubRival() <= 0)    throw new IllegalArgumentException("El club rival es obligatorio.");
        if (partido.getIdDivision()  <= 0)    throw new IllegalArgumentException("La división es obligatoria.");
        if (partido.getSede()        == null) throw new IllegalArgumentException("La sede del partido es obligatoria.");

        return partidoDAO.insertar(partido);
    }

    /**
     * Registra un evento en un partido usando una transacción ACID.
     * Si la inserción del evento falla, se realiza rollback completo.
     */
    public void registrarEvento(EventoPartido evento) throws Exception {
        final String SQL =
            "INSERT INTO eventos_partido (id_partido, id_jugador, tipo_evento, minuto) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                ps.setInt(1,    evento.getIdPartido());
                ps.setInt(2,    evento.getIdJugador());
                ps.setString(3, evento.getTipoEvento().name());
                if (evento.getMinuto() != null) {
                    ps.setInt(4, evento.getMinuto());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.executeUpdate();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    /** Retorna todos los partidos finalizados. */
    public List<Partido> listarFinalizados() throws Exception {
        return partidoDAO.listarFinalizados();
    }

    /** Retorna los partidos de una división y temporada específicas. */
    public List<Partido> listarPorDivisionYTemporada(int idDivision, int temporada) throws Exception {
        return partidoDAO.listarPorDivisionYTemporada(idDivision, temporada);
    }
}
