package com.sltc.sistrugby.modelo;

public class Club {

    private int id;
    private String nombre;
    private String unionPertenencia;
    private String contacto;
    private boolean activo;

    public Club() { this.activo = true; }

    public Club(int id, String nombre, String unionPertenencia, String contacto, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.unionPertenencia = unionPertenencia;
        this.contacto = contacto;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUnionPertenencia() { return unionPertenencia; }
    public void setUnionPertenencia(String unionPertenencia) { this.unionPertenencia = unionPertenencia; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%s (%s)", nombre,
                unionPertenencia == null ? "sin unión" : unionPertenencia);
    }
}
