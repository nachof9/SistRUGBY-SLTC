package com.sltc.sistrugby.persistencia.impl;

import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.persistencia.ConexionDB;
import com.sltc.sistrugby.persistencia.dao.JugadorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de JugadorDAO.
 * Usa PreparedStatement en todas las consultas para prevenir SQL Injection.
 */
public class JugadorDAOImpl implements JugadorDAO {

    // ── insertar ──────────────────────────────────────────────────────────────

    @Override
    public void insertar(Jugador j) throws Exception {
        final String SQL =
            "INSERT INTO jugadores "
            + "(nombre, apellido, dni, fecha_nacimiento, posicion, id_division, estado, fecha_alta) "
            + "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVO', CURDATE())";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setString(3, j.getDni());
            ps.setDate(4,   Date.valueOf(j.getFechaNacimiento()));
            ps.setString(5, j.getPosicion());
            ps.setInt(6,    j.getIdDivision());
            ps.executeUpdate();
        }
    }

    // ── actualizar ────────────────────────────────────────────────────────────

    @Override
    public void actualizar(Jugador j) throws Exception {
        final String SQL =
            "UPDATE jugadores "
            + "SET nombre = ?, apellido = ?, posicion = ?, id_division = ? "
            + "WHERE id_jugador = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setString(3, j.getPosicion());
            ps.setInt(4,    j.getIdDivision());
            ps.setInt(5,    j.getId());
            ps.executeUpdate();
        }
    }

    // ── darDeBaja ─────────────────────────────────────────────────────────────

    @Override
    public void darDeBaja(int idJugador) throws Exception {
        final String SQL =
            "UPDATE jugadores SET estado = 'INACTIVO' WHERE id_jugador = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idJugador);
            ps.executeUpdate();
        }
    }

    // ── buscarPorDni ──────────────────────────────────────────────────────────

    @Override
    public Jugador buscarPorDni(String dni) throws Exception {
        final String SQL = "SELECT * FROM jugadores WHERE dni = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Override
    public Jugador buscarPorId(int idJugador) throws Exception {
        final String SQL = "SELECT * FROM jugadores WHERE id_jugador = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── listarActivos ─────────────────────────────────────────────────────────

    @Override
    public List<Jugador> listarActivos() throws Exception {
        final String SQL =
            "SELECT * FROM jugadores WHERE estado = 'ACTIVO' ORDER BY apellido, nombre";

        List<Jugador> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── listarActivosPorDivision ──────────────────────────────────────────────

    @Override
    public List<Jugador> listarActivosPorDivision(int idDivision) throws Exception {
        final String SQL =
            "SELECT * FROM jugadores "
            + "WHERE estado = 'ACTIVO' AND id_division = ? "
            + "ORDER BY apellido, nombre";

        List<Jugador> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idDivision);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── mapear ────────────────────────────────────────────────────────────────

    private Jugador mapear(ResultSet rs) throws SQLException {
        Jugador j = new Jugador();
        j.setId(rs.getInt("id_jugador"));
        j.setNombre(rs.getString("nombre"));
        j.setApellido(rs.getString("apellido"));
        j.setDni(rs.getString("dni"));
        j.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        j.setPosicion(rs.getString("posicion"));
        j.setIdDivision(rs.getInt("id_division"));
        j.setEstado(rs.getString("estado"));
        Date fa = rs.getDate("fecha_alta");
        if (fa != null) j.setFechaAlta(fa.toLocalDate());
        return j;
    }
}
