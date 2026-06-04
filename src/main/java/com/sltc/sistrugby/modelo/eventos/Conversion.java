package com.sltc.sistrugby.modelo.eventos;

/**
 * Conversión: patada al palo posterior a un try. 2 puntos.
 */
public class Conversion extends EventoPartido {

    public Conversion() { super(); }

    public Conversion(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public Conversion(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 2; }

    @Override
    public Tipo getTipo() { return Tipo.CONVERSION; }
}
