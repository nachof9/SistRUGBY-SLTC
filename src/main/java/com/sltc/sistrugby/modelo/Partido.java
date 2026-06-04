package com.sltc.sistrugby.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.eventos.EventoPartido;

/**
 * Partido jugado por el SLTC. Aglutina plantel y eventos.
 * Mantiene listas internas para uso en memoria durante la sesión activa;
 * la persistencia real se delega al PartidoDAO y EventoDAO.
 */
public class Partido {

    public enum Sede { LOCAL, VISITANTE }
    public enum Estado { BORRADOR, FINALIZADO }

    private int id;
    private LocalDate fecha;
    private int idClubRival;
    private int idCategoria;
    private int idTemporada;
    private Sede sede;
    private int ptsLocal;
    private int ptsVisitante;
    private Estado estado;

    // Composición: plantel + eventos del partido (cargados en demanda)
    private List<PlantelPartido> plantel = new ArrayList<>();
    private List<EventoPartido> eventos = new ArrayList<>();

    public Partido() {
        this.estado = Estado.BORRADOR;
    }

    public Partido(LocalDate fecha, int idClubRival, int idCategoria,
                   int idTemporada, Sede sede) {
        this.fecha = fecha;
        this.idClubRival = idClubRival;
        this.idCategoria = idCategoria;
        this.idTemporada = idTemporada;
        this.sede = sede;
        this.ptsLocal = 0;
        this.ptsVisitante = 0;
        this.estado = Estado.BORRADOR;
    }

    public Partido(int id, LocalDate fecha, int idClubRival, int idCategoria,
                   int idTemporada, Sede sede, int ptsLocal, int ptsVisitante,
                   Estado estado) {
        this.id = id;
        this.fecha = fecha;
        this.idClubRival = idClubRival;
        this.idCategoria = idCategoria;
        this.idTemporada = idTemporada;
        this.sede = sede;
        this.ptsLocal = ptsLocal;
        this.ptsVisitante = ptsVisitante;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public int getIdClubRival() { return idClubRival; }
    public void setIdClubRival(int idClubRival) { this.idClubRival = idClubRival; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public int getIdTemporada() { return idTemporada; }
    public void setIdTemporada(int idTemporada) { this.idTemporada = idTemporada; }
    public Sede getSede() { return sede; }
    public void setSede(Sede sede) { this.sede = sede; }
    public int getPtsLocal() { return ptsLocal; }
    public void setPtsLocal(int ptsLocal) { this.ptsLocal = ptsLocal; }
    public int getPtsVisitante() { return ptsVisitante; }
    public void setPtsVisitante(int ptsVisitante) { this.ptsVisitante = ptsVisitante; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public List<PlantelPartido> getPlantel() { return plantel; }
    public void setPlantel(List<PlantelPartido> plantel) { this.plantel = plantel; }

    public List<EventoPartido> getEventos() { return eventos; }
    public void setEventos(List<EventoPartido> eventos) { this.eventos = eventos; }

    /**
     * Recalcula puntos del SLTC sumando polimórficamente los eventos cargados.
     * Demuestra el beneficio del POLIMORFISMO: el cliente no conoce el tipo
     * concreto del evento, solo llama a calcularPuntos().
     */
    public int recalcularPuntosSLTC() {
        int total = 0;
        for (EventoPartido e : eventos) {
            total += e.calcularPuntos();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Partido #%d - %s - %s - %d vs %d - %s",
                id, fecha, sede, ptsLocal, ptsVisitante, estado);
    }
}
