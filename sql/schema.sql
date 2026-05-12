-- ============================================================
-- SistRUGBY-SLTC  |  schema.sql  |  MySQL 8.0
-- Santiago Lawn Tennis Club — Santiago del Estero, Argentina
-- Autor: Fonzo, Ignacio — Seminario de Práctica 2026
-- ============================================================

CREATE DATABASE IF NOT EXISTS sistrugby_sltc
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sistrugby_sltc;

-- ------------------------------------------------------------
-- Tabla: USUARIOS
-- Almacena las credenciales y roles de acceso al sistema.
-- Las contraseñas se almacenan con hash bcrypt (nunca en texto plano).
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario      INT          AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario  VARCHAR(50)  NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    rol             ENUM('ADMINISTRADOR', 'ENTRENADOR', 'SECRETARIO') NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: DIVISIONES
-- Categorías deportivas: juveniles M15-M19 y plantel superior.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS divisiones (
    id_division INT         AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    tipo        ENUM('JUVENIL', 'PLANTEL_SUPERIOR') NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: JUGADORES
-- Padrón de jugadores del SLTC. Baja lógica mediante campo estado.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jugadores (
    id_jugador       INT          AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    apellido         VARCHAR(100) NOT NULL,
    dni              VARCHAR(15)  NOT NULL UNIQUE,
    fecha_nacimiento DATE         NOT NULL,
    posicion         VARCHAR(50),
    id_division      INT          NOT NULL,
    estado           ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    fecha_alta       DATE         NOT NULL,
    CONSTRAINT fk_jug_div FOREIGN KEY (id_division)
        REFERENCES divisiones (id_division)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: CLUBES_RIVALES
-- Equipos rivales de la USR y otras uniones.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS clubes_rivales (
    id_club           INT          AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL UNIQUE,
    union_pertenencia VARCHAR(100),
    contacto          VARCHAR(200)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: PARTIDOS
-- Registro de encuentros deportivos por división y temporada.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS partidos (
    id_partido       INT  AUTO_INCREMENT PRIMARY KEY,
    fecha            DATE NOT NULL,
    id_club_rival    INT  NOT NULL,
    id_division      INT  NOT NULL,
    sede             ENUM('LOCAL', 'VISITANTE') NOT NULL,
    puntos_local     INT  NOT NULL DEFAULT 0,
    puntos_visitante INT  NOT NULL DEFAULT 0,
    temporada        YEAR NOT NULL,
    estado           ENUM('PENDIENTE', 'FINALIZADO') NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_part_club FOREIGN KEY (id_club_rival)
        REFERENCES clubes_rivales (id_club)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_part_div FOREIGN KEY (id_division)
        REFERENCES divisiones (id_division)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: PLANTEL_PARTIDO
-- Resuelve la relación N:M entre jugadores y partidos.
-- Registra si cada jugador es titular o suplente.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS plantel_partido (
    id_plantel INT AUTO_INCREMENT PRIMARY KEY,
    id_partido INT NOT NULL,
    id_jugador INT NOT NULL,
    rol        ENUM('TITULAR', 'SUPLENTE') NOT NULL,
    UNIQUE KEY uq_plantel (id_partido, id_jugador),
    CONSTRAINT fk_pp_partido FOREIGN KEY (id_partido)
        REFERENCES partidos (id_partido)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_pp_jugador FOREIGN KEY (id_jugador)
        REFERENCES jugadores (id_jugador)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Tabla: EVENTOS_PARTIDO
-- Registro de eventos durante el partido con minuto de ocurrencia.
-- Puntuación rugby: TRY=5, CONVERSION=2, PENAL=3, DROP=3.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS eventos_partido (
    id_evento   INT AUTO_INCREMENT PRIMARY KEY,
    id_partido  INT NOT NULL,
    id_jugador  INT NOT NULL,
    tipo_evento ENUM(
        'TRY',
        'CONVERSION',
        'PENAL',
        'DROP',
        'TARJETA_AMARILLA',
        'TARJETA_ROJA',
        'SUSTITUCION'
    ) NOT NULL,
    minuto INT,
    CONSTRAINT fk_ev_partido FOREIGN KEY (id_partido)
        REFERENCES partidos (id_partido)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_ev_jugador FOREIGN KEY (id_jugador)
        REFERENCES jugadores (id_jugador)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
