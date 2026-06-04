package com.sltc.sistrugby.modelo.eventos;

/**
 * Clase abstracta raíz de la jerarquía de eventos deportivos que ocurren
 * durante un partido. Aplica los pilares de:
 *
 * - ABSTRACCIÓN: define el contrato {@link #calcularPuntos()} y
 *   {@link #getTipo()} sin proveer una implementación concreta, dejando
 *   que cada subclase determine su comportamiento específico.
 *
 * - POLIMORFISMO: el código cliente puede iterar una colección de
 *   EventoPartido y obtener el comportamiento correcto en tiempo de
 *   ejecución según la subclase real (Try, Conversion, Penal, etc.).
 *
 * - HERENCIA: cada tipo concreto de evento extiende esta clase y reutiliza
 *   los atributos comunes (id, partido, jugador, minuto).
 */
public abstract class EventoPartido {

    public enum Tipo {
        TRY, CONVERSION, PENAL, DROP,
        TARJETA_AMARILLA, TARJETA_ROJA, SUSTITUCION
    }

    protected int id;
    protected int idPartido;
    protected int idJugador;
    protected int minuto;
    protected String descripcion;

    protected EventoPartido() { }

    protected EventoPartido(int idPartido, int idJugador, int minuto) {
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.minuto = minuto;
    }

    protected EventoPartido(int id, int idPartido, int idJugador,
                            int minuto, String descripcion) {
        this.id = id;
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.minuto = minuto;
        this.descripcion = descripcion;
    }

    /**
     * Contrato polimórfico: cada subclase devuelve los puntos que aporta
     * el evento al marcador del partido.
     */
    public abstract int calcularPuntos();

    /**
     * Contrato polimórfico: cada subclase declara su tipo concreto del
     * enum {@link Tipo}, lo que evita instanceof en el código cliente.
     */
    public abstract Tipo getTipo();

    // ----- Encapsulamiento -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }

    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }

    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return String.format("%s en minuto %d (jugador #%d) [%d pts]",
                getTipo(), minuto, idJugador, calcularPuntos());
    }

    /**
     * Factory polimórfica: instancia la subclase correcta a partir del
     * enum Tipo, asegurando que cada DAO/Service no acople con las
     * clases concretas.
     */
    public static EventoPartido crear(Tipo tipo, int idPartido,
                                      int idJugador, int minuto) {
        switch (tipo) {
            case TRY:              return new Try(idPartido, idJugador, minuto);
            case CONVERSION:       return new Conversion(idPartido, idJugador, minuto);
            case PENAL:            return new Penal(idPartido, idJugador, minuto);
            case DROP:             return new Drop(idPartido, idJugador, minuto);
            case TARJETA_AMARILLA: return new TarjetaAmarilla(idPartido, idJugador, minuto);
            case TARJETA_ROJA:     return new TarjetaRoja(idPartido, idJugador, minuto);
            case SUSTITUCION:      return new Sustitucion(idPartido, idJugador, minuto);
            default:
                throw new IllegalArgumentException("Tipo de evento desconocido: " + tipo);
        }
    }
}
