package com.sltc.sistrugby.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Categoria;

public class CategoriaDAO {

    public Categoria insertar(Categoria c) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (c.getId() == 0) c.setId(RepositorioMemoria.get().nextCategoriaId());
            RepositorioMemoria.get().categorias().put(c.getId(), c);
            return c;
        }
        String sql = "INSERT INTO categorias (nombre, tipo, activo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTipo().name());
            ps.setBoolean(3, c.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
        return c;
    }

    public List<Categoria> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            List<Categoria> l = new ArrayList<>(RepositorioMemoria.get().categorias().values());
            l.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));
            return l;
        }
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY id_categoria";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Categoria findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().categorias().get(id);
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Categoria findByNombre(String nombre) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            for (Categoria c : RepositorioMemoria.get().categorias().values()) {
                if (c.getNombre().equalsIgnoreCase(nombre)) return c;
            }
            return null;
        }
        String sql = "SELECT * FROM categorias WHERE nombre = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("nombre"),
                Categoria.Tipo.valueOf(rs.getString("tipo")),
                rs.getBoolean("activo"));
    }
}
