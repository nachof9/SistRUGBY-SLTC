package com.sltc.sistrugby.modelo.eventos;

/**
 * Sustitución: ingreso de un jugador suplente. No aporta puntos.
 */
public class Sustitucion extends EventoPartido {

    public Sustitucion() { super(); }

    public Sustitucion(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public Sustitucion(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 0; }

    @Override
    public Tipo getTipo() { return Tipo.SUSTITUCION; }
}
