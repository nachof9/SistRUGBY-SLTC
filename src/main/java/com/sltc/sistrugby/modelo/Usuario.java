package com.sltc.sistrugby.modelo;

/**
 * Representa un usuario del sistema con su rol de acceso.
 * La contraseña nunca se almacena en texto plano;
 * se usa el hash bcrypt almacenado en la base de datos.
 */
public class Usuario {

    public enum Rol {
        ADMINISTRADOR, ENTRENADOR, SECRETARIO
    }

    private int    id;
    private String nombreUsuario;
    private String contrasenaHash;
    private Rol    rol;
    private boolean activo;

    public Usuario() {}

    public Usuario(String nombreUsuario, String contrasenaHash, Rol rol) {
        this.nombreUsuario  = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.rol            = rol;
        this.activo         = true;
    }

    public boolean tieneRol(Rol r) {
        return this.rol == r;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getNombreUsuario()                      { return nombreUsuario; }
    public void   setNombreUsuario(String nombreUsuario)  { this.nombreUsuario = nombreUsuario; }

    public String getContrasenaHash()                        { return contrasenaHash; }
    public void   setContrasenaHash(String contrasenaHash)   { this.contrasenaHash = contrasenaHash; }

    public Rol  getRol()          { return rol; }
    public void setRol(Rol rol)   { this.rol = rol; }

    public boolean isActivo()              { return activo; }
    public void    setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombreUsuario='" + nombreUsuario + "', rol=" + rol + '}';
    }
}
