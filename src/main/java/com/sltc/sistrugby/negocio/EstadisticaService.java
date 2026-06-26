package com.sltc.sistrugby.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.eventos.EventoPartido;
import com.sltc.sistrugby.persistencia.EventoDAO;
import com.sltc.sistrugby.persistencia.JugadorDAO;
import com.sltc.sistrugby.persistencia.PartidoDAO;

public class EstadisticaService {

    private final EventoDAO eventoDao = new EventoDAO();
    private final PartidoDAO partidoDao = new PartidoDAO();
    private final JugadorDAO jugadorDao = new JugadorDAO();

    public static class FilaRanking {
        public final Jugador jugador;
        public int tries;
        public int conversiones;
        public int penales;
        public int drops;
        public int tarjetas;
        public int puntosTotal;
        public FilaRanking(Jugador jugador) { this.jugador = jugador; }
    }

    public List<FilaRanking> rankingPorPuntos() throws SQLException {
        Map<Integer, FilaRanking> mapa = new HashMap<>();
        for (Partido p : partidoDao.findAll()) {
            for (EventoPartido e : eventoDao.findByPartido(p.getId())) {
                FilaRanking fila = mapa.computeIfAbsent(e.getIdJugador(), id -> {
                    Jugador j = buscarJugador(id);
                    return new FilaRanking(j != null ? j : new Jugador());
                });
                switch (e.getTipo()) {
                    case TRY:              fila.tries++; break;
                    case CONVERSION:       fila.conversiones++; break;
                    case PENAL:            fila.penales++; break;
                    case DROP:             fila.drops++; break;
                    case TARJETA_AMARILLA:
                    case TARJETA_ROJA:     fila.tarjetas++; break;
                    default: break;
                }
                fila.puntosTotal += e.calcularPuntos();
            }
        }
        List<FilaRanking> resultado = new ArrayList<>(mapa.values());
        resultado.sort(Comparator.comparingInt((FilaRanking f) -> f.puntosTotal).reversed());
        return resultado;
    }

    /**
     * Resumen global: cantidad de eventos por tipo. Usa un ARREGLO int[] de
     * tamano fijo (cantidad de valores del enum Tipo), complementario al
     * ArrayList del ranking (cuyo tamano es dinamico).
     */
    public int[] conteoGlobalPorTipo() throws SQLException {
        int[] conteo = new int[EventoPartido.Tipo.values().length];
        for (Partido p : partidoDao.findAll()) {
            for (EventoPartido e : eventoDao.findByPartido(p.getId())) {
                conteo[e.getTipo().ordinal()]++;
            }
        }
        return conteo;
    }

    private Jugador buscarJugador(int id) {
        try {
            for (Jugador j : jugadorDao.findAll()) if (j.getId() == id) return j;
        } catch (SQLException e) { }
        return null;
    }
}
