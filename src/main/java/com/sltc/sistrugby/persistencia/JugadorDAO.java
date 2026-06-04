package com.sltc.sistrugby.persistencia;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Jugador;

/**
 * DAO de la entidad Jugador.
 */
public class JugadorDAO {

    public Jugador insertar(Jugador j) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            RepositorioMemoria repo = RepositorioMemoria.get();
            j.setId(repo.nextJugadorId());
            repo.jugadores().put(j.getId(), j);
            return j;
        }
        String sql = "INSERT INTO jugadores (nombre, apellido, dni, fecha_nacimiento, "
                + "posicion, id_categoria, estado, fecha_alta) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'activo', CURDATE())";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setString(3, j.getDni());
            ps.setDate(4, Date.valueOf(j.getFechaNacimiento()));
            ps.setString(5, j.getPosicion());
            ps.setInt(6, j.getIdCategoria());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) j.setId(rs.getInt(1));
            }
        }
        return j;
    }

    public Jugador findByDni(String dni) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            for (Jugador j : RepositorioMemoria.get().jugadores().values()) {
                if (dni.equals(j.getDni())) return j;
            }
            return null;
        }
        String sql = "SELECT * FROM jugadores WHERE dni = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Jugador> findByCategoria(int idCategoria) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        List<Jugador> resultado = new ArrayList<>();
        if (bd.isModoMemoria()) {
            for (Jugador j : RepositorioMemoria.get().jugadores().values()) {
                if (j.getIdCategoria() == idCategoria && j.estaActivo()) resultado.add(j);
            }
            return resultado;
        }
        String sql = "SELECT * FROM jugadores WHERE id_categoria = ? AND estado = 'activo' "
                + "ORDER BY apellido, nombre";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    public List<Jugador> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().jugadores().values());
        }
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY apellido, nombre";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void darDeBaja(int idJugador) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            Jugador j = RepositorioMemoria.get().jugadores().get(idJugador);
            if (j != null) {
                j.setEstado(Jugador.Estado.INACTIVO);
                j.setFechaBaja(java.time.LocalDate.now());
            }
            return;
        }
        String sql = "UPDATE jugadores SET estado='inactivo', fecha_baja=CURDATE() "
                + "WHERE id_jugador = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            ps.executeUpdate();
        }
    }

    private Jugador mapear(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt("id_jugador"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                rs.getString("posicion"),
                rs.getInt("id_categoria"),
                Jugador.Estado.valueOf(rs.getString("estado").toUpperCase()),
                rs.getDate("fecha_alta").toLocalDate(),
                rs.getDate("fecha_baja") != null ? rs.getDate("fecha_baja").toLocalDate() : null);
    }
}
