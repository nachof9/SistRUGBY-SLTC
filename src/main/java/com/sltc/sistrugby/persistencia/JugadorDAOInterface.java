package com.sltc.sistrugby.persistencia;

import java.sql.SQLException;
import java.util.List;

import com.sltc.sistrugby.modelo.Jugador;

/**
 * Contrato de acceso a datos para la entidad {@link Jugador} (patron DAO).
 *
 * <p>Define la interfaz uniforme que la capa de negocio consume sin conocer
 * la tecnologia de persistencia subyacente. Cualquier implementacion
 * (JDBC/MySQL, memoria, o un futuro ORM) puede sustituirse sin tocar los
 * Service, materializando el bajo acoplamiento que exige la arquitectura en
 * capas. Todas las operaciones declaran {@link SQLException} para que el
 * manejo del error tecnico se resuelva en una unica capa.</p>
 */
public interface JugadorDAOInterface {

    /** Inserta un jugador y devuelve la instancia con su id asignado. */
    Jugador insertar(Jugador j) throws SQLException;

    /** Busca un jugador por DNI; devuelve {@code null} si no existe. */
    Jugador findByDni(String dni) throws SQLException;

    /** Lista los jugadores activos de una categoria, ordenados por apellido. */
    List<Jugador> findByCategoria(int idCategoria) throws SQLException;

    /** Devuelve todos los jugadores. */
    List<Jugador> findAll() throws SQLException;

    /** Baja logica: marca el jugador como inactivo preservando el historial. */
    void darDeBaja(int idJugador) throws SQLException;
}
