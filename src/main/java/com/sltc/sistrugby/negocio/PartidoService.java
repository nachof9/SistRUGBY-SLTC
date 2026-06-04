package com.sltc.sistrugby.negocio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.sltc.sistrugby.excepciones.DatosInvalidosException;
import com.sltc.sistrugby.modelo.Partido;
import com.sltc.sistrugby.modelo.PlantelPartido;
import com.sltc.sistrugby.persistencia.PartidoDAO;

public class PartidoService {

    private final PartidoDAO dao = new PartidoDAO();

    public Partido registrar(LocalDate fecha, int idClubRival, int idCategoria,
                             int idTemporada, Partido.Sede sede)
            throws DatosInvalidosException, SQLException {

        if (fecha == null || fecha.isAfter(LocalDate.now()))
            throw new DatosInvalidosException("Fecha del partido inválida");
        if (idClubRival <= 0) throw new DatosInvalidosException("Club rival inválido");
        if (idCategoria <= 0) throw new DatosInvalidosException("Categoría inválida");
        if (idTemporada <= 0) throw new DatosInvalidosException("Temporada inválida");
        if (sede == null) throw new DatosInvalidosException("Sede inválida");

        Partido p = new Partido(fecha, idClubRival, idCategoria, idTemporada, sede);
        return dao.insertar(p);
    }

    public void registrarPlantel(int idPartido, List<PlantelPartido> plantel)
            throws DatosInvalidosException, SQLException {
        if (plantel == null || plantel.isEmpty())
            throw new DatosInvalidosException("Plantel vacío");
        long titulares = plantel.stream()
                .filter(pp -> pp.getCondicion() == PlantelPartido.Condicion.TITULAR)
                .count();
        if (titulares < 1)
            throw new DatosInvalidosException(
                    "El plantel debe contener al menos un titular");
        dao.guardarPlantel(idPartido, plantel);
    }

    public Partido buscarPorId(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<Partido> listarTodos() throws SQLException {
        return dao.findAll();
    }

    public List<PlantelPartido> obtenerPlantel(int idPartido) throws SQLException {
        return dao.obtenerPlantel(idPartido);
    }

    public void actualizarMarcador(int idPartido, int ptsLocal, int ptsVisitante)
            throws SQLException {
        dao.actualizarPuntos(idPartido, ptsLocal, ptsVisitante);
    }
}
