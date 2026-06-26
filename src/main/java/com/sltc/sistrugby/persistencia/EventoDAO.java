package com.sltc.sistrugby.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.sltc.sistrugby.modelo.eventos.EventoPartido;

public class EventoDAO {

    public EventoPartido insertar(EventoPartido evento) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            evento.setId(RepositorioMemoria.get().nextEventoId());
            RepositorioMemoria.get().eventosDe(evento.getIdPartido()).addLast(evento);
            RepositorioMemoria.get().pilaUndo().push(evento);
            return evento;
        }
        String sql = "INSERT INTO eventos_partido "
                + "(id_partido, id_jugador, tipo_evento, minuto, descripcion) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, evento.getIdPartido());
            ps.setInt(2, evento.getIdJugador());
            ps.setString(3, evento.getTipo().name());
            ps.setInt(4, evento.getMinuto());
            ps.setString(5, evento.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) evento.setId(rs.getInt(1));
            }
        }
        return evento;
    }

    public List<EventoPartido> findByPartido(int idPartido) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new LinkedList<>(RepositorioMemoria.get().eventosDe(idPartido));
        }
        List<EventoPartido> lista = new LinkedList<>();
        String sql = "SELECT * FROM eventos_partido WHERE id_partido = ? ORDER BY minuto, id_evento";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public EventoPartido deshacerUltimo() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (RepositorioMemoria.get().pilaUndo().isEmpty()) return null;
            EventoPartido tope = RepositorioMemoria.get().pilaUndo().pop();
            RepositorioMemoria.get().eventosDe(tope.getIdPartido()).remove(tope);
            return tope;
        }
        String sel = "SELECT * FROM eventos_partido ORDER BY id_evento DESC LIMIT 1";
        EventoPartido ultimo = null;
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sel);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) ultimo = mapear(rs);
        }
        if (ultimo == null) return null;
        try (PreparedStatement del = bd.getConnection().prepareStatement(
                "DELETE FROM eventos_partido WHERE id_evento = ?")) {
            del.setInt(1, ultimo.getId());
            del.executeUpdate();
        }
        return ultimo;
    }

    private EventoPartido mapear(ResultSet rs) throws SQLException {
        EventoPartido.Tipo tipo = EventoPartido.Tipo.valueOf(rs.getString("tipo_evento"));
        EventoPartido e = EventoPartido.crear(
                tipo,
                rs.getInt("id_partido"),
                rs.getInt("id_jugador"),
                rs.getInt("minuto"));
        e.setId(rs.getInt("id_evento"));
        e.setDescripcion(rs.getString("descripcion"));
        return e;
    }
}
