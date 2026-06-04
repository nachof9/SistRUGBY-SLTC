package com.sltc.sistrugby.modelo.eventos;

/**
 * Drop: patada a los palos en juego con bote previo. 3 puntos.
 */
public class Drop extends EventoPartido {

    public Drop() { super(); }

    public Drop(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public Drop(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 3; }

    @Override
    public Tipo getTipo() { return Tipo.DROP; }
}
