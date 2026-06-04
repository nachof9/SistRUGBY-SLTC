package com.sltc.sistrugby.modelo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Clase abstracta del dominio que captura los atributos comunes a toda persona
 * vinculada al sistema (jugadores, usuarios). Aplica el pilar de POO de
 * ABSTRACCIÓN — define la esencia conceptual sin proveer una implementación
 * concreta — y sirve como raíz de la jerarquía de HERENCIA.
 *
 * Atributos encapsulados con visibilidad protegida para que las subclases
 * accedan a ellos directamente cuando sea apropiado.
 */
public abstract class Persona {

    protected int id;
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected LocalDate fechaNacimiento;

    /** Constructor por defecto requerido por algunos frameworks de persistencia. */
    protected Persona() { }

    /** Constructor completo. */
    protected Persona(int id, String nombre, String apellido,
                      String dni, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }

    // ----- Encapsulamiento: getters y setters -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** Devuelve el nombre completo en formato "Apellido, Nombre". */
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    /** Devuelve la edad actual calculada a partir de la fecha de nacimiento. */
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Método abstracto: cada subclase debe definir su representación textual
     * para el menú/log. Demuestra el contrato de la abstracción.
     */
    public abstract String descripcionCorta();

    @Override
    public String toString() {
        return descripcionCorta();
    }
}
