package com.sltc.sistrugby.modelo;

public class Temporada {

    private int id;
    private int anio;
    private String descripcion;

    public Temporada() { }

    public Temporada(int id, int anio, String descripcion) {
        this.id = id;
        this.anio = anio;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() { return "Temporada " + anio; }
}
