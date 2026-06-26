package com.sltc.sistrugby.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Club;

public class ClubDAO {

    public Club insertar(Club c) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (c.getId() == 0) c.setId(RepositorioMemoria.get().nextClubId());
            RepositorioMemoria.get().clubes().put(c.getId(), c);
            return c;
        }
        String sql = "INSERT INTO clubes (nombre, union_pertenencia, contacto, activo) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getUnionPertenencia());
            ps.setString(3, c.getContacto());
            ps.setBoolean(4, c.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
        return c;
    }

    public List<Club> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().clubes().values());
        }
        List<Club> lista = new ArrayList<>();
        String sql = "SELECT * FROM clubes ORDER BY nombre";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Club findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().clubes().get(id);
        String sql = "SELECT * FROM clubes WHERE id_club = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Club mapear(ResultSet rs) throws SQLException {
        return new Club(
                rs.getInt("id_club"),
                rs.getString("nombre"),
                rs.getString("union_pertenencia"),
                rs.getString("contacto"),
                rs.getBoolean("activo"));
    }
}
