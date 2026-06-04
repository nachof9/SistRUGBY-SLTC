package com.sltc.sistrugby.modelo;

import java.time.LocalDate;

/**
 * Entidad del dominio: jugador de rugby del SLTC.
 * Aplica el pilar de HERENCIA al extender {@link Persona} y reutilizar
 * sus atributos comunes. Aplica ENCAPSULAMIENTO con atributos privados
 * y acceso controlado por getters/setters.
 */
public class Jugador extends Persona {

    public enum Estado { ACTIVO, INACTIVO }

    private String posicion;
    private int idCategoria;
    private Estado estado;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    /** Constructor por defecto. */
    public Jugador() {
        super();
        this.estado = Estado.ACTIVO;
    }

    /** Constructor de alta (sin id, asignado por la BD). */
    public Jugador(String nombre, String apellido, String dni,
                   LocalDate fechaNacimiento, String posicion, int idCategoria) {
        super(0, nombre, apellido, dni, fechaNacimiento);
        this.posicion = posicion;
        this.idCategoria = idCategoria;
        this.estado = Estado.ACTIVO;
        this.fechaAlta = LocalDate.now();
    }

    /** Constructor completo (típicamente usado por el DAO al mapear filas). */
    public Jugador(int id, String nombre, String apellido, String dni,
                   LocalDate fechaNacimiento, String posicion,
                   int idCategoria, Estado estado,
                   LocalDate fechaAlta, LocalDate fechaBaja) {
        super(id, nombre, apellido, dni, fechaNacimiento);
        this.posicion = posicion;
        this.idCategoria = idCategoria;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    // ----- Getters / Setters -----

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }

    public boolean estaActivo() {
        return estado == Estado.ACTIVO;
    }

    @Override
    public String descripcionCorta() {
        return String.format("Jugador #%d - %s [%s] - DNI %s",
                id, getNombreCompleto(), posicion, dni);
    }
}
