package com.sltc.sistrugby.persistencia;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.sltc.sistrugby.modelo.eventos.EventoPartido;

public class EventoDAO {

    public EventoPartido insertar(EventoPartido evento) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            evento.setId(RepositorioMemoria.get().nextEventoId());
            // Lista enlazada: inserción al final, mantiene orden cronológico
            RepositorioMemoria.get().eventosDe(evento.getIdPartido()).addLast(evento);
            // Empuja en la pila de UNDO
            RepositorioMemoria.get().pilaUndo().push(evento);
            return evento;
        }
        // Implementación JDBC con INSERT y polimorfismo de tipo (omitida).
        return evento;
    }

    public List<EventoPartido> findByPartido(int idPartido) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new LinkedList<>(RepositorioMemoria.get().eventosDe(idPartido));
        }
        return new LinkedList<>();
    }

    /**
     * Deshace la última carga (operación LIFO sobre la pila de undo).
     * @return el evento removido o null si la pila está vacía.
     */
    public EventoPartido deshacerUltimo() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (RepositorioMemoria.get().pilaUndo().isEmpty()) return null;
            EventoPartido tope = RepositorioMemoria.get().pilaUndo().pop();
            RepositorioMemoria.get().eventosDe(tope.getIdPartido()).remove(tope);
            return tope;
        }
        return null;
    }
}
