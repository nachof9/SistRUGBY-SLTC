package com.sltc.sistrugby.persistencia.dao;

import com.sltc.sistrugby.modelo.Jugador;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad Jugador.
 * Las implementaciones concretas usan JDBC + PreparedStatement.
 */
public interface JugadorDAO {

    /**
     * Inserta un nuevo jugador en el padrón con estado ACTIVO.
     * @throws Exception si el DNI ya existe (violación de UNIQUE KEY).
     */
    void insertar(Jugador jugador) throws Exception;

    /**
     * Actualiza los datos de un jugador existente.
     */
    void actualizar(Jugador jugador) throws Exception;

    /**
     * Aplica la baja lógica: cambia el estado a INACTIVO.
     * El historial estadístico no se elimina.
     */
    void darDeBaja(int idJugador) throws Exception;

    /**
     * Busca un jugador por su DNI único.
     * @return el Jugador encontrado o {@code null} si no existe.
     */
    Jugador buscarPorDni(String dni) throws Exception;

    /**
     * Busca un jugador por su identificador primario.
     * @return el Jugador encontrado o {@code null} si no existe.
     */
    Jugador buscarPorId(int idJugador) throws Exception;

    /**
     * Retorna todos los jugadores con estado ACTIVO,
     * ordenados por apellido y nombre.
     */
    List<Jugador> listarActivos() throws Exception;

    /**
     * Retorna todos los jugadores de una división específica con estado ACTIVO.
     */
    List<Jugador> listarActivosPorDivision(int idDivision) throws Exception;
}
