package com.sltc.sistrugby.modelo.eventos;

/**
 * Try: anotación de mayor valor en rugby. 5 puntos.
 * Aplica POLIMORFISMO sobreescribiendo {@link EventoPartido#calcularPuntos()}.
 */
public class Try extends EventoPartido {

    public Try() { super(); }

    public Try(int idPartido, int idJugador, int minuto) {
        super(idPartido, idJugador, minuto);
    }

    public Try(int id, int idPartido, int idJugador, int minuto, String descripcion) {
        super(id, idPartido, idJugador, minuto, descripcion);
    }

    @Override
    public int calcularPuntos() { return 5; }

    @Override
    public Tipo getTipo() { return Tipo.TRY; }
}
