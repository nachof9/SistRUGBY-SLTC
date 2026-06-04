package com.sltc.sistrugby.persistencia;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.sltc.sistrugby.modelo.Categoria;
import com.sltc.sistrugby.modelo.Club;
import com.sltc.sistrugby.modelo.Jugador;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.PlantelPartido;
import com.sltc.sistrugby.modelo.Temporada;
import com.sltc.sistrugby.modelo.Usuario;
import com.sltc.sistrugby.modelo.eventos.EventoPartido;

/**
 * Repositorio en memoria para el modo demo (sin MySQL conectado).
 *
 * Demuestra el uso de las ESTRUCTURAS DE DATOS pedidas por la consigna:
 *
 *   - LISTA ENLAZADA ({@code LinkedList<EventoPartido>}): mantiene el
 *     orden cronológico de los eventos registrados durante un partido,
 *     con inserción O(1) al final y recorrido secuencial natural.
 *
 *   - PILA ({@code Deque<EventoPartido>}): permite deshacer (undo) la
 *     última carga de evento. LIFO.
 *
 *   - COLA ({@code Queue<Integer>}): registra el orden FIFO de
 *     sustituciones planificadas (jugador que entra a la cancha cuando
 *     el cuerpo técnico lo decida).
 *
 *   - HASHMAP: almacenamiento principal indexado por id para acceso O(1)
 *     a entidades (jugadores, partidos, etc.).
 */
public final class RepositorioMemoria {

    private static final RepositorioMemoria INSTANCIA = new RepositorioMemoria();
    public static RepositorioMemoria get() { return INSTANCIA; }

    // Almacenes principales (HashMap, acceso O(1))
    private final Map<Integer, Usuario>   usuarios   = new HashMap<>();
    private final Map<Integer, Categoria> categorias = new HashMap<>();
    private final Map<Integer, Club>      clubes     = new HashMap<>();
    private final Map<Integer, Temporada> temporadas = new HashMap<>();
    private final Map<Integer, Jugador>   jugadores  = new HashMap<>();
    private final Map<Integer, Partido>   partidos   = new HashMap<>();

    // Lista enlazada de eventos POR PARTIDO (orden cronológico)
    private final Map<Integer, LinkedList<EventoPartido>> eventosPorPartido = new HashMap<>();

    // Plantel por partido
    private final Map<Integer, List<PlantelPartido>> plantelPorPartido = new HashMap<>();

    // Pila de eventos para deshacer (alcance: sesión)
    private final Deque<EventoPartido> pilaUndo = new ArrayDeque<>();

    // Cola de sustituciones pendientes (FIFO)
    private final Queue<Integer> colaSustituciones = new LinkedList<>();

    // Generadores de id autoincremental
    private int seqUsuario = 0, seqCategoria = 0, seqClub = 0, seqTemporada = 0,
                seqJugador = 0, seqPartido = 0, seqEvento = 0, seqPlantel = 0;

    private RepositorioMemoria() { }

    // ----- Generadores -----
    public synchronized int nextUsuarioId()   { return ++seqUsuario; }
    public synchronized int nextCategoriaId() { return ++seqCategoria; }
    public synchronized int nextClubId()      { return ++seqClub; }
    public synchronized int nextTemporadaId() { return ++seqTemporada; }
    public synchronized int nextJugadorId()   { return ++seqJugador; }
    public synchronized int nextPartidoId()   { return ++seqPartido; }
    public synchronized int nextEventoId()    { return ++seqEvento; }
    public synchronized int nextPlantelId()   { return ++seqPlantel; }

    // ----- Accesores a los almacenes -----
    public Map<Integer, Usuario>   usuarios()   { return usuarios; }
    public Map<Integer, Categoria> categorias() { return categorias; }
    public Map<Integer, Club>      clubes()     { return clubes; }
    public Map<Integer, Temporada> temporadas() { return temporadas; }
    public Map<Integer, Jugador>   jugadores()  { return jugadores; }
    public Map<Integer, Partido>   partidos()   { return partidos; }

    public LinkedList<EventoPartido> eventosDe(int idPartido) {
        return eventosPorPartido.computeIfAbsent(idPartido, k -> new LinkedList<>());
    }

    public List<PlantelPartido> plantelDe(int idPartido) {
        return plantelPorPartido.computeIfAbsent(idPartido, k -> new ArrayList<>());
    }

    public Deque<EventoPartido> pilaUndo()       { return pilaUndo; }
    public Queue<Integer>       colaSustituciones() { return colaSustituciones; }

    /**
     * Devuelve copia ordenada de todos los jugadores (no expone el HashMap interno).
     */
    public List<Jugador> listaJugadores() {
        return new ArrayList<>(jugadores.values());
    }
}
