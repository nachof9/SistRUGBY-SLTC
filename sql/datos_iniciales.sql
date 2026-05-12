-- ============================================================
-- SistRUGBY-SLTC  |  datos_iniciales.sql  |  MySQL 8.0
-- Datos de prueba para el prototipo (Sprint 1 y 2)
-- Autor: Fonzo, Ignacio — Seminario de Práctica 2026
-- ============================================================

USE sistrugby_sltc;

-- ============================================================
-- INSERT — Divisiones
-- ============================================================
INSERT INTO divisiones (nombre, tipo) VALUES
    ('M15',             'JUVENIL'),
    ('M16',             'JUVENIL'),
    ('M17',             'JUVENIL'),
    ('M18',             'JUVENIL'),
    ('M19',             'JUVENIL'),
    ('Pre-Intermedia',  'PLANTEL_SUPERIOR'),
    ('Intermedia',      'PLANTEL_SUPERIOR'),
    ('Primera',         'PLANTEL_SUPERIOR');

-- ============================================================
-- INSERT — Clubes rivales (USR)
-- ============================================================
INSERT INTO clubes_rivales (nombre, union_pertenencia, contacto) VALUES
    ('Inti Rugby Club',    'USR', NULL),
    ('Quimilí Rugby Club', 'USR', NULL),
    ('Estudiantes Rugby',  'USR', NULL),
    ('Los Tarcos RC',      'USR', NULL);

-- ============================================================
-- INSERT — Usuarios del sistema
-- Contraseñas generadas con BCrypt (cost=12):
--   admin2026    → $2a$12$...
--   rugby2026    → $2a$12$...
--   sltc2026     → $2a$12$...
-- ============================================================
INSERT INTO usuarios (nombre_usuario, contrasena_hash, rol, activo) VALUES
    ('admin',
     '$2a$12$K8Hv0zXpL5mR3TqN7uYwOuWvBcDeFgHiJkLmNoPqRsTuVwXyZaB',
     'ADMINISTRADOR', 1),
    ('entrenador1',
     '$2a$12$A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6',
     'ENTRENADOR', 1),
    ('secretario1',
     '$2a$12$Z6Y5X4W3V2U1T0S9R8Q7P6O5N4M3L2K1J0I9H8G7F6E5D4C3B2A1',
     'SECRETARIO', 1);

-- ============================================================
-- INSERT — Jugadores de prueba
-- ============================================================
INSERT INTO jugadores (nombre, apellido, dni, fecha_nacimiento, posicion, id_division, estado, fecha_alta) VALUES
    ('Matías',    'González',  '42100001', '2009-03-15', 'Pilar',           1, 'ACTIVO',   '2024-03-01'),
    ('Luciano',   'Herrera',   '42100002', '2008-07-22', 'Hooker',          2, 'ACTIVO',   '2024-03-01'),
    ('Sebastián', 'Martínez',  '41500003', '2007-11-08', 'Talonador',       3, 'ACTIVO',   '2024-03-01'),
    ('Facundo',   'López',     '40800004', '2006-05-30', 'Apertura',        4, 'ACTIVO',   '2024-03-01'),
    ('Tomás',     'Rodríguez', '40200005', '2005-02-14', 'Fullback',        5, 'ACTIVO',   '2024-03-01'),
    ('Ezequiel',  'Pérez',     '39500006', '2003-09-19', 'Pilar',           6, 'ACTIVO',   '2024-03-01'),
    ('Ramiro',    'Díaz',      '38900007', '2001-12-03', 'Número 8',        7, 'ACTIVO',   '2024-03-01'),
    ('Nicolás',   'Fernández', '37800008', '1999-06-25', 'Centro',          8, 'ACTIVO',   '2024-03-01'),
    ('Agustín',   'Torres',    '36700009', '1997-04-11', 'Ala',             8, 'INACTIVO', '2023-03-01'),
    ('Joaquín',   'Sánchez',   '43200010', '2009-08-07', 'Medio Scrum',     1, 'ACTIVO',   '2024-03-01');

-- ============================================================
-- INSERT — Partido de ejemplo
-- ============================================================
INSERT INTO partidos (fecha, id_club_rival, id_division, sede, puntos_local, puntos_visitante, temporada, estado) VALUES
    ('2024-05-18', 1, 8, 'LOCAL', 28, 14, 2024, 'FINALIZADO');

-- ============================================================
-- INSERT — Plantel del partido
-- ============================================================
INSERT INTO plantel_partido (id_partido, id_jugador, rol) VALUES
    (1, 6, 'TITULAR'),
    (1, 7, 'TITULAR'),
    (1, 8, 'TITULAR');

-- ============================================================
-- INSERT — Eventos del partido
-- ============================================================
INSERT INTO eventos_partido (id_partido, id_jugador, tipo_evento, minuto) VALUES
    (1, 8, 'TRY',        15),
    (1, 8, 'CONVERSION', 16),
    (1, 7, 'TRY',        34),
    (1, 6, 'PENAL',      60);

-- ============================================================
-- SELECT — Estadísticas individuales por temporada
-- Puntuación: TRY=5, CONVERSION=2, PENAL=3, DROP=3
-- ============================================================
SELECT
    j.id_jugador,
    j.apellido,
    j.nombre,
    d.nombre                                         AS division,
    COUNT(DISTINCT pp.id_partido)                    AS partidos_jugados,
    SUM(CASE WHEN e.tipo_evento = 'TRY'        THEN 1 ELSE 0 END) AS tries,
    SUM(CASE WHEN e.tipo_evento = 'CONVERSION' THEN 1 ELSE 0 END) AS conversiones,
    SUM(CASE WHEN e.tipo_evento = 'PENAL'      THEN 1 ELSE 0 END) AS penales,
    SUM(CASE WHEN e.tipo_evento = 'DROP'       THEN 1 ELSE 0 END) AS drops,
    SUM(CASE WHEN e.tipo_evento = 'TARJETA_AMARILLA' THEN 1 ELSE 0 END) AS tarjetas_amarillas,
    SUM(CASE WHEN e.tipo_evento = 'TARJETA_ROJA'     THEN 1 ELSE 0 END) AS tarjetas_rojas,
    SUM(
        CASE e.tipo_evento
            WHEN 'TRY'        THEN 5
            WHEN 'CONVERSION' THEN 2
            WHEN 'PENAL'      THEN 3
            WHEN 'DROP'       THEN 3
            ELSE 0
        END
    )                                                AS puntos_aportados
FROM jugadores j
JOIN divisiones d         ON j.id_division    = d.id_division
LEFT JOIN plantel_partido pp ON j.id_jugador  = pp.id_jugador
LEFT JOIN partidos p      ON pp.id_partido    = p.id_partido
                         AND p.temporada      = 2024
LEFT JOIN eventos_partido e ON e.id_jugador   = j.id_jugador
                           AND e.id_partido   = p.id_partido
WHERE j.estado = 'ACTIVO'
GROUP BY j.id_jugador, j.apellido, j.nombre, d.nombre
ORDER BY puntos_aportados DESC;

-- ============================================================
-- SELECT — Historial de partidos con resultado
-- ============================================================
SELECT
    p.id_partido,
    p.fecha,
    p.temporada,
    d.nombre                                                         AS division,
    cr.nombre                                                        AS rival,
    p.sede,
    p.puntos_local,
    p.puntos_visitante,
    CASE
        WHEN p.sede = 'LOCAL'     AND p.puntos_local > p.puntos_visitante THEN 'VICTORIA'
        WHEN p.sede = 'LOCAL'     AND p.puntos_local < p.puntos_visitante THEN 'DERROTA'
        WHEN p.sede = 'VISITANTE' AND p.puntos_local > p.puntos_visitante THEN 'VICTORIA'
        WHEN p.sede = 'VISITANTE' AND p.puntos_local < p.puntos_visitante THEN 'DERROTA'
        ELSE 'EMPATE'
    END                                                              AS resultado
FROM partidos p
JOIN divisiones     d  ON p.id_division  = d.id_division
JOIN clubes_rivales cr ON p.id_club_rival = cr.id_club
ORDER BY p.fecha;

-- ============================================================
-- SELECT — Reporte completo de un partido (id=1)
-- ============================================================

-- Datos del partido
SELECT
    p.id_partido,
    p.fecha,
    p.temporada,
    d.nombre   AS division,
    cr.nombre  AS rival,
    p.sede,
    p.puntos_local,
    p.puntos_visitante,
    p.estado
FROM partidos p
JOIN divisiones     d  ON p.id_division  = d.id_division
JOIN clubes_rivales cr ON p.id_club_rival = cr.id_club
WHERE p.id_partido = 1;

-- Plantel del partido
SELECT
    j.apellido,
    j.nombre,
    j.posicion,
    pp.rol
FROM plantel_partido pp
JOIN jugadores j ON pp.id_jugador = j.id_jugador
WHERE pp.id_partido = 1
ORDER BY pp.rol DESC, j.apellido;

-- Eventos del partido con jugador
SELECT
    e.minuto,
    e.tipo_evento,
    CONCAT(j.apellido, ', ', j.nombre) AS jugador
FROM eventos_partido e
JOIN jugadores j ON e.id_jugador = j.id_jugador
WHERE e.id_partido = 1
ORDER BY e.minuto;

-- ============================================================
-- DELETE — Baja lógica de jugador (recomendada)
-- Preserva historial estadístico.
-- ============================================================
UPDATE jugadores
SET estado = 'INACTIVO'
WHERE id_jugador = 9;

-- ============================================================
-- DELETE — Eliminación física de un evento registrado por error
-- ============================================================
-- DELETE FROM eventos_partido WHERE id_evento = 99;

-- ============================================================
-- DELETE — Eliminar un partido en estado PENDIENTE
-- Se deben eliminar primero los registros dependientes.
-- ============================================================
-- DELETE FROM plantel_partido  WHERE id_partido = 99;
-- DELETE FROM eventos_partido  WHERE id_partido = 99;
-- DELETE FROM partidos         WHERE id_partido = 99 AND estado = 'PENDIENTE';
