package com.sltc.sistrugby.persistencia.dao;

import com.sltc.sistrugby.modelo.EventoPartido;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad EventoPartido.
 */
public interface EventoPartidoDAO {

    void insertar(EventoPartido evento) throws Exception;

    List<EventoPartido> listarPorPartido(int idPartido) throws Exception;

    /**
     * Elimina físicamente un evento registrado por error.
     * Solo permitido antes de que el partido sea finalizado.
     */
    void eliminar(int idEvento) throws Exception;
}
