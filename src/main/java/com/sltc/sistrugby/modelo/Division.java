package com.sltc.sistrugby.modelo;

/**
 * Representa una categoría deportiva del SLTC.
 * Tipos: JUVENIL (M15–M19) o PLANTEL_SUPERIOR (Pre-Intermedia, Intermedia, Primera).
 */
public class Division {

    public enum Tipo {
        JUVENIL, PLANTEL_SUPERIOR
    }

    private int    id;
    private String nombre;
    private Tipo   tipo;

    public Division() {}

    public Division(String nombre, Tipo tipo) {
        this.nombre = nombre;
        this.tipo   = tipo;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int    getId()             { return id; }
    public void   setId(int id)       { this.id = id; }

    public String getNombre()                 { return nombre; }
    public void   setNombre(String nombre)    { this.nombre = nombre; }

    public Tipo getTipo()             { return tipo; }
    public void setTipo(Tipo tipo)    { this.tipo = tipo; }

    @Override
    public String toString() { return nombre; }
}
