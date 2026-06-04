package com.sltc.sistrugby.modelo.eventos;

/**
 * Tarjeta roja: el jugador queda expulsado del partido sin reemplazo.
 * No aporta puntos al marcador.
 */
public class TarjetaRoja extends EventoPartido {

    public TarjetaRoja() { super(); }

    public TarjetaRoja(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public TarjetaRoja(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 0; }

    @Override
    public Tipo getTipo() { return Tipo.TARJETA_ROJA; }
}
