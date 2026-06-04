package com.sltc.sistrugby.negocio;

import java.sql.SQLException;

import com.sltc.sistrugby.excepciones.CredencialesInvalidasException;
import com.sltc.sistrugby.modelo.Usuario;
import com.sltc.sistrugby.persistencia.UsuarioDAO;
import com.sltc.sistrugby.util.HashUtil;

/**
 * Servicio de negocio: autenticación y alta de usuarios.
 * Encapsula las reglas de negocio relacionadas con credenciales
 * (verificación de hash, control de cuenta activa).
 */
public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Autentica un usuario verificando su hash PBKDF2.
     * @throws CredencialesInvalidasException si las credenciales no son válidas
     *         o el usuario está deshabilitado.
     */
    public Usuario autenticar(String nombreUsuario, String contrasenaPlana)
            throws CredencialesInvalidasException, SQLException {
        Usuario u = dao.findByNombreUsuario(nombreUsuario);
        if (u == null || !u.isActivo()) {
            throw new CredencialesInvalidasException();
        }
        if (!HashUtil.verificar(contrasenaPlana, u.getContrasenaHash())) {
            throw new CredencialesInvalidasException();
        }
        return u;
    }

    /**
     * Registra un nuevo usuario hasheando la contraseña antes de persistirla.
     */
    public Usuario registrar(String nombreUsuario, String contrasenaPlana,
                             Usuario.Rol rol) throws SQLException {
        Usuario u = new Usuario(nombreUsuario,
                HashUtil.hashear(contrasenaPlana), rol);
        return dao.insertar(u);
    }
}
