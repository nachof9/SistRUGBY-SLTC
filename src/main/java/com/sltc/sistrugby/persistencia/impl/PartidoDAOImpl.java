package com.sltc.sistrugby.persistencia.impl;

import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.persistencia.ConexionDB;
import com.sltc.sistrugby.persistencia.dao.PartidoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de PartidoDAO.
 */
public class PartidoDAOImpl implements PartidoDAO {

    @Override
    public int insertar(Partido p) throws Exception {
        final String SQL =
            "INSERT INTO partidos "
            + "(fecha, id_club_rival, id_division, sede, puntos_local, puntos_visitante, temporada, estado) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1,   Date.valueOf(p.getFecha()));
            ps.setInt(2,    p.getIdClubRival());
            ps.setInt(3,    p.getIdDivision());
            ps.setString(4, p.getSede().name());
            ps.setInt(5,    p.getPuntosLocal());
            ps.setInt(6,    p.getPuntosVisitante());
            ps.setInt(7,    p.getTemporada());
            ps.setString(8, p.getEstado().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void actualizar(Partido p) throws Exception {
        final String SQL =
            "UPDATE partidos "
            + "SET fecha = ?, id_club_rival = ?, id_division = ?, sede = ?, "
            + "    puntos_local = ?, puntos_visitante = ?, temporada = ?, estado = ? "
            + "WHERE id_partido = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setDate(1,   Date.valueOf(p.getFecha()));
            ps.setInt(2,    p.getIdClubRival());
            ps.setInt(3,    p.getIdDivision());
            ps.setString(4, p.getSede().name());
            ps.setInt(5,    p.getPuntosLocal());
            ps.setInt(6,    p.getPuntosVisitante());
            ps.setInt(7,    p.getTemporada());
            ps.setString(8, p.getEstado().name());
            ps.setInt(9,    p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Partido buscarPorId(int idPartido) throws Exception {
        final String SQL = "SELECT * FROM partidos WHERE id_partido = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    @Override
    public List<Partido> listarPorDivisionYTemporada(int idDivision, int temporada) throws Exception {
        final String SQL =
            "SELECT * FROM partidos "
            + "WHERE id_division = ? AND temporada = ? "
            + "ORDER BY fecha DESC";

        List<Partido> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idDivision);
            ps.setInt(2, temporada);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Partido> listarFinalizados() throws Exception {
        final String SQL =
            "SELECT * FROM partidos WHERE estado = 'FINALIZADO' ORDER BY fecha DESC";

        List<Partido> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        Partido p = new Partido();
        p.setId(rs.getInt("id_partido"));
        p.setFecha(rs.getDate("fecha").toLocalDate());
        p.setIdClubRival(rs.getInt("id_club_rival"));
        p.setIdDivision(rs.getInt("id_division"));
        p.setSede(rs.getString("sede"));
        p.setPuntosLocal(rs.getInt("puntos_local"));
        p.setPuntosVisitante(rs.getInt("puntos_visitante"));
        p.setTemporada(rs.getInt("temporada"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
