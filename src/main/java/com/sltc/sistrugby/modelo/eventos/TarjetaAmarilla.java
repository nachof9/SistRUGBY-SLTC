package com.sltc.sistrugby.modelo.eventos;

/**
 * Tarjeta amarilla: el jugador queda 10 minutos fuera del campo.
 * No aporta puntos al marcador.
 */
public class TarjetaAmarilla extends EventoPartido {

    public TarjetaAmarilla() { super(); }

    public TarjetaAmarilla(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public TarjetaAmarilla(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 0; }

    @Override
    public Tipo getTipo() { return Tipo.TARJETA_AMARILLA; }
}
