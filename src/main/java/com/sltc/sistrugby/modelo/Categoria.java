package com.sltc.sistrugby.modelo;

/**
 * División deportiva del SLTC.
 * Tipos: JUVENIL (M15..M19) o PLANTEL_SUPERIOR (Pre-Intermedia, Intermedia, Primera).
 */
public class Categoria {

    public enum Tipo { JUVENIL, PLANTEL_SUPERIOR }

    private int id;
    private String nombre;
    private Tipo tipo;
    private boolean activo;

    public Categoria() { this.activo = true; }

    public Categoria(int id, String nombre, Tipo tipo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%s [%s]", nombre, tipo);
    }
}
