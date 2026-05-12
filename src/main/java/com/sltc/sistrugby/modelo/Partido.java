package com.sltc.sistrugby.modelo;

import java.time.LocalDate;

/**
 * Representa un encuentro deportivo de la sección rugby del SLTC.
 */
public class Partido {

    public enum Sede    { LOCAL, VISITANTE }
    public enum Estado  { PENDIENTE, FINALIZADO }

    private int       id;
    private LocalDate fecha;
    private int       idClubRival;
    private int       idDivision;
    private Sede      sede;
    private int       puntosLocal;
    private int       puntosVisitante;
    private int       temporada;
    private Estado    estado;

    public Partido() {}

    public Partido(LocalDate fecha, int idClubRival, int idDivision,
                   Sede sede, int temporada) {
        this.fecha        = fecha;
        this.idClubRival  = idClubRival;
        this.idDivision   = idDivision;
        this.sede         = sede;
        this.temporada    = temporada;
        this.estado       = Estado.PENDIENTE;
    }

    /** Devuelve 'VICTORIA', 'DERROTA' o 'EMPATE' desde la perspectiva del SLTC. */
    public String getResultado() {
        if (puntosLocal > puntosVisitante) return "VICTORIA";
        if (puntosLocal < puntosVisitante) return "DERROTA";
        return "EMPATE";
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int       getId()                          { return id; }
    public void      setId(int id)                    { this.id = id; }

    public LocalDate getFecha()                       { return fecha; }
    public void      setFecha(LocalDate fecha)        { this.fecha = fecha; }

    public int       getIdClubRival()                 { return idClubRival; }
    public void      setIdClubRival(int idClubRival)  { this.idClubRival = idClubRival; }

    public int       getIdDivision()                  { return idDivision; }
    public void      setIdDivision(int idDivision)    { this.idDivision = idDivision; }

    public Sede      getSede()                        { return sede; }
    public void      setSede(Sede sede)               { this.sede = sede; }
    public void      setSede(String sede)             { this.sede = Sede.valueOf(sede); }

    public int       getPuntosLocal()                     { return puntosLocal; }
    public void      setPuntosLocal(int puntosLocal)      { this.puntosLocal = puntosLocal; }

    public int       getPuntosVisitante()                       { return puntosVisitante; }
    public void      setPuntosVisitante(int puntosVisitante)    { this.puntosVisitante = puntosVisitante; }

    public int       getTemporada()                   { return temporada; }
    public void      setTemporada(int temporada)      { this.temporada = temporada; }

    public Estado    getEstado()                      { return estado; }
    public void      setEstado(Estado estado)         { this.estado = estado; }
    public void      setEstado(String estado)         { this.estado = Estado.valueOf(estado); }
}
