package com.sltc.sistrugby.modelo;

/**
 * Asocia un jugador a un partido con su rol (titular/suplente).
 * Resuelve la relación N:M entre JUGADORES y PARTIDOS.
 */
public class PlantelPartido {

    public enum Rol { TITULAR, SUPLENTE }

    private int id;
    private int idPartido;
    private int idJugador;
    private Rol rol;

    public PlantelPartido() {}

    public PlantelPartido(int idPartido, int idJugador, Rol rol) {
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.rol       = rol;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public int  getIdPartido()                   { return idPartido; }
    public void setIdPartido(int idPartido)      { this.idPartido = idPartido; }

    public int  getIdJugador()                   { return idJugador; }
    public void setIdJugador(int idJugador)      { this.idJugador = idJugador; }

    public Rol  getRol()                 { return rol; }
    public void setRol(Rol rol)          { this.rol = rol; }
    public void setRol(String rol)       { this.rol = Rol.valueOf(rol); }
}
