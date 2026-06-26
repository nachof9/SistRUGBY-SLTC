package com.sltc.sistrugby.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.PlantelPartido;

public class PartidoDAO {

    public Partido insertar(Partido p) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            if (p.getId() == 0) p.setId(RepositorioMemoria.get().nextPartidoId());
            RepositorioMemoria.get().partidos().put(p.getId(), p);
            return p;
        }
        String sql = "INSERT INTO partidos (fecha, id_club_rival, id_categoria, "
                + "id_temporada, sede, pts_local, pts_visitante, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(p.getFecha()));
            ps.setInt(2, p.getIdClubRival());
            ps.setInt(3, p.getIdCategoria());
            ps.setInt(4, p.getIdTemporada());
            ps.setString(5, p.getSede().name());
            ps.setInt(6, p.getPtsLocal());
            ps.setInt(7, p.getPtsVisitante());
            ps.setString(8, p.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    public Partido findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().partidos().get(id);
        String sql = "SELECT * FROM partidos WHERE id_partido = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Partido> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().partidos().values());
        }
        List<Partido> lista = new ArrayList<>();
        String sql = "SELECT * FROM partidos ORDER BY fecha";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void guardarPlantel(int idPartido, List<PlantelPartido> plantel)
            throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            List<PlantelPartido> almacen = RepositorioMemoria.get().plantelDe(idPartido);
            for (PlantelPartido pp : plantel) {
                if (pp.getId() == 0) pp.setId(RepositorioMemoria.get().nextPlantelId());
                pp.setIdPartido(idPartido);
                almacen.add(pp);
            }
            return;
        }
        Connection con = bd.getConnection();
        boolean autoCommitPrevio = con.getAutoCommit();
        String sql = "INSERT INTO partido_plantel (id_partido, id_jugador, condicion) "
                + "VALUES (?, ?, ?)";
        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (PlantelPartido pp : plantel) {
                    ps.setInt(1, idPartido);
                    ps.setInt(2, pp.getIdJugador());
                    ps.setString(3, pp.getCondicion().name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommitPrevio);
        }
    }

    public List<PlantelPartido> obtenerPlantel(int idPartido) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().plantelDe(idPartido);
        List<PlantelPartido> lista = new ArrayList<>();
        String sql = "SELECT * FROM partido_plantel WHERE id_partido = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new PlantelPartido(
                            rs.getInt("id_plantel"),
                            rs.getInt("id_partido"),
                            rs.getInt("id_jugador"),
                            PlantelPartido.Condicion.valueOf(rs.getString("condicion"))));
                }
            }
        }
        return lista;
    }

    public void actualizarPuntos(int idPartido, int ptsLocal, int ptsVisitante)
            throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            Partido p = RepositorioMemoria.get().partidos().get(idPartido);
            if (p != null) {
                p.setPtsLocal(ptsLocal);
                p.setPtsVisitante(ptsVisitante);
            }
            return;
        }
        String sql = "UPDATE partidos SET pts_local = ?, pts_visitante = ? WHERE id_partido = ?";
        try (PreparedStatement ps = bd.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ptsLocal);
            ps.setInt(2, ptsVisitante);
            ps.setInt(3, idPartido);
            ps.executeUpdate();
        }
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        return new Partido(
                rs.getInt("id_partido"),
                rs.getDate("fecha").toLocalDate(),
                rs.getInt("id_club_rival"),
                rs.getInt("id_categoria"),
                rs.getInt("id_temporada"),
                Partido.Sede.valueOf(rs.getString("sede")),
                rs.getInt("pts_local"),
                rs.getInt("pts_visitante"),
                Partido.Estado.valueOf(rs.getString("estado")));
    }
}
