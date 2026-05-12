package com.sltc.sistrugby.persistencia.dao;

import com.sltc.sistrugby.modelo.Partido;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad Partido.
 */
public interface PartidoDAO {

    /**
     * Inserta un nuevo partido. Retorna el id generado.
     */
    int insertar(Partido partido) throws Exception;

    /**
     * Actualiza los datos de un partido existente.
     */
    void actualizar(Partido partido) throws Exception;

    /**
     * Busca un partido por su identificador primario.
     */
    Partido buscarPorId(int idPartido) throws Exception;

    /**
     * Retorna todos los partidos de una división y temporada,
     * ordenados por fecha descendente.
     */
    List<Partido> listarPorDivisionYTemporada(int idDivision, int temporada) throws Exception;

    /**
     * Retorna todos los partidos finalizados, ordenados por fecha descendente.
     */
    List<Partido> listarFinalizados() throws Exception;
}
