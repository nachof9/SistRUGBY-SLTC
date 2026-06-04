package com.sltc.sistrugby.modelo.eventos;

/**
 * Penal: patada a los palos como sanción al rival. 3 puntos.
 */
public class Penal extends EventoPartido {

    public Penal() { super(); }

    public Penal(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public Penal(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 3; }

    @Override
    public Tipo getTipo() { return Tipo.PENAL; }
}
