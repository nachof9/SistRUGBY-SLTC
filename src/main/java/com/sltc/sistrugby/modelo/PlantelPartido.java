package com.sltc.sistrugby.modelo;

/**
 * Relación ternaria jugador-partido-condición:
 * indica si un jugador fue titular o suplente en un partido determinado.
 */
public class PlantelPartido {

    public enum Condicion { TITULAR, SUPLENTE }

    private int id;
    private int idPartido;
    private int idJugador;
    private Condicion condicion;

    public PlantelPartido() { }

    public PlantelPartido(int idPartido, int idJugador, Condicion condicion) {
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.condicion = condicion;
    }

    public PlantelPartido(int id, int idPartido, int idJugador, Condicion condicion) {
        this.id = id;
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.condicion = condicion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }
    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }
    public Condicion getCondicion() { return condicion; }
    public void setCondicion(Condicion condicion) { this.condicion = condicion; }
}
