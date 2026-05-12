package com.sltc.sistrugby.modelo;

/**
 * Representa un evento ocurrido durante un partido:
 * try, conversión, penal, drop, tarjeta o sustitución.
 *
 * Puntuación rugby (según reglamento World Rugby):
 *   TRY = 5 pts | CONVERSIÓN = 2 pts | PENAL = 3 pts | DROP = 3 pts
 */
public class EventoPartido {

    public enum TipoEvento {
        TRY, CONVERSION, PENAL, DROP,
        TARJETA_AMARILLA, TARJETA_ROJA, SUSTITUCION
    }

    private int        id;
    private int        idPartido;
    private int        idJugador;
    private TipoEvento tipoEvento;
    private Integer    minuto;

    public EventoPartido() {}

    public EventoPartido(int idPartido, int idJugador, TipoEvento tipoEvento, Integer minuto) {
        this.idPartido  = idPartido;
        this.idJugador  = idJugador;
        this.tipoEvento = tipoEvento;
        this.minuto     = minuto;
    }

    /**
     * Devuelve los puntos aportados por este evento.
     * Las tarjetas y sustituciones no suman puntos.
     */
    public int getPuntosAportados() {
        if (tipoEvento == null) return 0;
        return switch (tipoEvento) {
            case TRY        -> 5;
            case CONVERSION -> 2;
            case PENAL      -> 3;
            case DROP       -> 3;
            default         -> 0;
        };
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int        getId()                        { return id; }
    public void       setId(int id)                  { this.id = id; }

    public int        getIdPartido()                 { return idPartido; }
    public void       setIdPartido(int idPartido)    { this.idPartido = idPartido; }

    public int        getIdJugador()                 { return idJugador; }
    public void       setIdJugador(int idJugador)    { this.idJugador = idJugador; }

    public TipoEvento getTipoEvento()                          { return tipoEvento; }
    public void       setTipoEvento(TipoEvento tipoEvento)     { this.tipoEvento = tipoEvento; }
    public void       setTipoEvento(String tipoEvento)         { this.tipoEvento = TipoEvento.valueOf(tipoEvento); }

    public Integer    getMinuto()                    { return minuto; }
    public void       setMinuto(Integer minuto)      { this.minuto = minuto; }
}
