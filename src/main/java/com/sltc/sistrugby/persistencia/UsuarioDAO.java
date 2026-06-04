package com.sltc.sistrugby.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sltc.sistrugby.modelo.Usuario;

/**
 * DAO de la entidad Usuario. Aplica el patrón Data Access Object:
 * separa la lógica de acceso a la base de datos del resto del sistema.
 *
 * Implementación dual: cuando la conexión está en modo memoria delega en
 * {@link RepositorioMemoria}; en modo MySQL real ejecuta SQL JDBC con
 * PreparedStatement.
 */
public class UsuarioDAO {

    public Usuario insertar(Usuario u) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            RepositorioMemoria repo = RepositorioMemoria.get();
            u.setId(repo.nextUsuarioId());
            repo.usuarios().put(u.getId(), u);
            return u;
        }
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena_hash, rol, activo) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, u.getContrasenaHash());
            ps.setString(3, u.getRol().name());
            ps.setBoolean(4, u.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
        }
        return u;
    }

    public Usuario findByNombreUsuario(String nombreUsuario) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            for (Usuario u : RepositorioMemoria.get().usuarios().values()) {
                if (u.getNombreUsuario().equals(nombreUsuario)) return u;
            }
            return null;
        }
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(
                rs.getString("nombre_usuario"),
                rs.getString("contrasena_hash"),
                Usuario.Rol.valueOf(rs.getString("rol")));
        u.setId(rs.getInt("id_usuario"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
