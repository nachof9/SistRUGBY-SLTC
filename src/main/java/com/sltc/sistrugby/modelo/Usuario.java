package com.sltc.sistrugby.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad del dominio: usuario del sistema (Administrador, Entrenador, Secretario).
 * Aplica HERENCIA al extender {@link Persona}.
 *
 * Las contraseñas se almacenan exclusivamente como hash PBKDF2 con salt
 * (ver {@link com.sltc.sistrugby.util.HashUtil}). Jamás se persiste la
 * contraseña en texto plano.
 */
public class Usuario extends Persona {

    public enum Rol { ADMINISTRADOR, ENTRENADOR, SECRETARIO }

    private String nombreUsuario;
    private String contrasenaHash;
    private Rol rol;
    private boolean activo;
    private LocalDateTime creadoEn;

    public Usuario() {
        super();
        this.activo = true;
    }

    public Usuario(String nombreUsuario, String contrasenaHash, Rol rol) {
        super();
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.rol = rol;
        this.activo = true;
        this.creadoEn = LocalDateTime.now();
    }

    public Usuario(int id, String nombre, String apellido, String dni,
                   LocalDate fechaNacimiento, String nombreUsuario,
                   String contrasenaHash, Rol rol, boolean activo,
                   LocalDateTime creadoEn) {
        super(id, nombre, apellido, dni, fechaNacimiento);
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.rol = rol;
        this.activo = activo;
        this.creadoEn = creadoEn;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String descripcionCorta() {
        return String.format("Usuario '%s' - Rol: %s", nombreUsuario, rol);
    }
}
