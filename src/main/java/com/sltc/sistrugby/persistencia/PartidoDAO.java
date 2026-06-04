package com.sltc.sistrugby.persistencia;

import java.sql.SQLException;
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
        }
        // Implementación JDBC similar al JugadorDAO (omitida por brevedad).
        return p;
    }

    public Partido findById(int id) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().partidos().get(id);
        return null;
    }

    public List<Partido> findAll() throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) {
            return new ArrayList<>(RepositorioMemoria.get().partidos().values());
        }
        return new ArrayList<>();
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
        }
    }

    public List<PlantelPartido> obtenerPlantel(int idPartido) throws SQLException {
        ConexionBD bd = ConexionBD.getInstance();
        if (bd.isModoMemoria()) return RepositorioMemoria.get().plantelDe(idPartido);
        return new ArrayList<>();
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
        }
    }
}
