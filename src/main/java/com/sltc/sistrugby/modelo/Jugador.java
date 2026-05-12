package com.sltc.sistrugby.modelo;

import java.time.LocalDate;

/**
 * Representa un jugador del padrón deportivo del SLTC.
 * La baja es lógica: el campo estado cambia a INACTIVO;
 * el historial estadístico se conserva íntegro.
 */
public class Jugador {

    public enum Estado {
        ACTIVO, INACTIVO
    }

    private int       id;
    private String    nombre;
    private String    apellido;
    private String    dni;
    private LocalDate fechaNacimiento;
    private String    posicion;
    private int       idDivision;
    private Estado    estado;
    private LocalDate fechaAlta;

    public Jugador() {}

    public Jugador(String nombre, String apellido, String dni,
                   LocalDate fechaNacimiento, String posicion, int idDivision) {
        this.nombre          = nombre;
        this.apellido        = apellido;
        this.dni             = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.posicion        = posicion;
        this.idDivision      = idDivision;
        this.estado          = Estado.ACTIVO;
        this.fechaAlta       = LocalDate.now();
    }

    /** Devuelve el nombre completo en formato "Apellido, Nombre". */
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    public boolean estaActivo() {
        return Estado.ACTIVO.equals(estado);
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int       getId()                       { return id; }
    public void      setId(int id)                 { this.id = id; }

    public String    getNombre()                   { return nombre; }
    public void      setNombre(String nombre)      { this.nombre = nombre; }

    public String    getApellido()                 { return apellido; }
    public void      setApellido(String apellido)  { this.apellido = apellido; }

    public String    getDni()                      { return dni; }
    public void      setDni(String dni)            { this.dni = dni; }

    public LocalDate getFechaNacimiento()                          { return fechaNacimiento; }
    public void      setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String    getPosicion()                     { return posicion; }
    public void      setPosicion(String posicion)      { this.posicion = posicion; }

    public int       getIdDivision()                   { return idDivision; }
    public void      setIdDivision(int idDivision)     { this.idDivision = idDivision; }

    public Estado    getEstado()                       { return estado; }
    public void      setEstado(Estado estado)          { this.estado = estado; }

    public void      setEstado(String estado)          { this.estado = Estado.valueOf(estado); }

    public LocalDate getFechaAlta()                    { return fechaAlta; }
    public void      setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public String toString() {
        return "Jugador{id=" + id + ", nombre='" + getNombreCompleto() + "', division=" + idDivision + '}';
    }
}
