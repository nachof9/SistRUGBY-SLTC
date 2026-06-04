package com.sltc.sistrugby.negocio;

import java.sql.SQLException;
import java.util.List;

import com.sltc.sistrugby.excepciones.DatosInvalidosException;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.PlantelPartido;
import com.sltc.sistrugby.modelo.eventos.EventoPartido;
import com.sltc.sistrugby.persistencia.EventoDAO;
import com.sltc.sistrugby.persistencia.PartidoDAO;

/**
 * Servicio de negocio: alta de eventos de partido y recálculo polimórfico
 * del marcador.
 */
public class EventoService {

    private final EventoDAO eventoDao = new EventoDAO();
    private final PartidoDAO partidoDao = new PartidoDAO();

    /**
     * Registra un evento durante un partido. Verifica que el jugador
     * pertenezca al plantel del partido y recalcula el marcador
     * polimórficamente.
     */
    public EventoPartido registrarEvento(EventoPartido.Tipo tipo, int idPartido,
                                         int idJugador, int minuto)
            throws DatosInvalidosException, SQLException {

        if (tipo == null) throw new DatosInvalidosException("Tipo de evento obligatorio");
        if (minuto < 0 || minuto > 90)
            throw new DatosInvalidosException("Minuto inválido (0-90)");

        // Verificación: jugador pertenece al plantel
        List<PlantelPartido> plantel = partidoDao.obtenerPlantel(idPartido);
        boolean perteneceAlPlantel = plantel.stream()
                .anyMatch(pp -> pp.getIdJugador() == idJugador);
        if (!perteneceAlPlantel) {
            throw new DatosInvalidosException(
                    "El jugador #" + idJugador + " no pertenece al plantel del partido");
        }

        EventoPartido evento = EventoPartido.crear(tipo, idPartido, idJugador, minuto);
        eventoDao.insertar(evento);

        // POLIMORFISMO en acción: recalcular marcador
        Partido p = partidoDao.findById(idPartido);
        if (p != null) {
            List<EventoPartido> eventos = eventoDao.findByPartido(idPartido);
            int total = eventos.stream()
                    .mapToInt(EventoPartido::calcularPuntos)
                    .sum();
            if (p.getSede() == Partido.Sede.LOCAL) {
                partidoDao.actualizarPuntos(idPartido, total, p.getPtsVisitante());
            } else {
                partidoDao.actualizarPuntos(idPartido, p.getPtsLocal(), total);
            }
        }
        return evento;
    }

    public List<EventoPartido> obtenerEventos(int idPartido) throws SQLException {
        return eventoDao.findByPartido(idPartido);
    }

    public EventoPartido deshacerUltimo() throws SQLException {
        return eventoDao.deshacerUltimo();
    }
}
