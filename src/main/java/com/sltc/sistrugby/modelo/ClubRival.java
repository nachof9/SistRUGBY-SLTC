package com.sltc.sistrugby.modelo;

/**
 * Representa un club rival de la USR u otra unión.
 */
public class ClubRival {

    private int    id;
    private String nombre;
    private String unionPertenencia;
    private String contacto;

    public ClubRival() {}

    public ClubRival(String nombre, String unionPertenencia) {
        this.nombre           = nombre;
        this.unionPertenencia = unionPertenencia;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int    getId()                   { return id; }
    public void   setId(int id)             { this.id = id; }

    public String getNombre()                       { return nombre; }
    public void   setNombre(String nombre)          { this.nombre = nombre; }

    public String getUnionPertenencia()                            { return unionPertenencia; }
    public void   setUnionPertenencia(String unionPertenencia)     { this.unionPertenencia = unionPertenencia; }

    public String getContacto()                     { return contacto; }
    public void   setContacto(String contacto)      { this.contacto = contacto; }

    @Override
    public String toString() { return nombre; }
}
