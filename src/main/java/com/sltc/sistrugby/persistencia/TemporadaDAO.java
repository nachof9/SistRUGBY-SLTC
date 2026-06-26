package com.sltc.sistrugby.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Temporada;

/**
 * DAO de la entidad Temporada (anio deportivo). Patron DAO con
 * implementacion dual memoria / JDBC. Incorporado en AP4 para dar soporte
 * de persistencia real a la FK partidos.id_temporada.
 */
public class TemporadaDAO {

    public Temporada insertar(Temporada t) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (t.getId() == 0) t.setId(RepositorioMemoria.get().nextTemporadaId());
            RepositorioMemoria.get().temporadas().put(t.getId(), t);
            return t;
        }
        String sql = "INSERT INTO temporadas (anio, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getAnio());
            ps.setString(2, t.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
        }
        return t;
    }

    public List<Temporada> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().temporadas().values());
        }
        List<Temporada> lista = new ArrayList<>();
        String sql = "SELECT * FROM temporadas ORDER BY anio";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Temporada findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().temporadas().get(id);
        String sql = "SELECT * FROM temporadas WHERE id_temporada = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Temporada mapear(ResultSet rs) throws SQLException {
        return new Temporada(
                rs.getInt("id_temporada"),
                rs.getInt("anio"),
                rs.getString("descripcion"));
    }
}
