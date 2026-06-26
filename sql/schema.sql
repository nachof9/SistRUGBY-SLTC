-- ============================================================
-- SistRUGBY-SLTC  |  schema.sql  |  MySQL 8.0
-- Santiago Lawn Tennis Club - Santiago del Estero, Argentina
-- Autor: Fonzo, Ignacio - Seminario de Practica 2026 (AP4)
-- ------------------------------------------------------------
-- Esquema alineado 1:1 con la capa de persistencia Java (DAOs).
-- Normalizado hasta 3FN. Integridad referencial con InnoDB.
-- ============================================================
CREATE DATABASE IF NOT EXISTS sistrugby_sltc
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sistrugby_sltc;
DROP TABLE IF EXISTS eventos_partido;
DROP TABLE IF EXISTS partido_plantel;
DROP TABLE IF EXISTS partidos;
DROP TABLE IF EXISTS jugadores;
DROP TABLE IF EXISTS temporadas;
DROP TABLE IF EXISTS clubes;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
    id_usuario      INT          AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario  VARCHAR(50)  NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    rol             ENUM('ADMINISTRADOR','ENTRENADOR','SECRETARIO') NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1
) ENGINE = InnoDB;
CREATE TABLE categorias (
    id_categoria INT         AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(50) NOT NULL UNIQUE,
    tipo         ENUM('JUVENIL','PLANTEL_SUPERIOR') NOT NULL,
    activo       TINYINT(1)  NOT NULL DEFAULT 1
) ENGINE = InnoDB;
CREATE TABLE clubes (
    id_club           INT          AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL UNIQUE,
    union_pertenencia VARCHAR(100),
    contacto          VARCHAR(200),
    activo            TINYINT(1)   NOT NULL DEFAULT 1
) ENGINE = InnoDB;
CREATE TABLE temporadas (
    id_temporada INT          AUTO_INCREMENT PRIMARY KEY,
    anio         INT          NOT NULL UNIQUE,
    descripcion  VARCHAR(100)
) ENGINE = InnoDB;
CREATE TABLE jugadores (
    id_jugador       INT          AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(80)  NOT NULL,
    apellido         VARCHAR(80)  NOT NULL,
    dni              VARCHAR(10)  NOT NULL UNIQUE,
    fecha_nacimiento DATE         NOT NULL,
    posicion         VARCHAR(30),
    id_categoria     INT          NOT NULL,
    estado           ENUM('activo','inactivo') NOT NULL DEFAULT 'activo',
    fecha_alta       DATE         NOT NULL DEFAULT (CURDATE()),
    fecha_baja       DATE,
    CONSTRAINT fk_jug_cat FOREIGN KEY (id_categoria)
        REFERENCES categorias (id_categoria) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;
CREATE TABLE partidos (
    id_partido    INT  AUTO_INCREMENT PRIMARY KEY,
    fecha         DATE NOT NULL,
    id_club_rival INT  NOT NULL,
    id_categoria  INT  NOT NULL,
    id_temporada  INT  NOT NULL,
    sede          ENUM('LOCAL','VISITANTE') NOT NULL,
    pts_local     INT  NOT NULL DEFAULT 0,
    pts_visitante INT  NOT NULL DEFAULT 0,
    estado        ENUM('BORRADOR','FINALIZADO') NOT NULL DEFAULT 'BORRADOR',
    CONSTRAINT fk_par_club FOREIGN KEY (id_club_rival)
        REFERENCES clubes (id_club)          ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_par_cat FOREIGN KEY (id_categoria)
        REFERENCES categorias (id_categoria) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_par_temp FOREIGN KEY (id_temporada)
        REFERENCES temporadas (id_temporada) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;
CREATE TABLE partido_plantel (
    id_plantel INT AUTO_INCREMENT PRIMARY KEY,
    id_partido INT NOT NULL,
    id_jugador INT NOT NULL,
    condicion  ENUM('TITULAR','SUPLENTE') NOT NULL,
    UNIQUE KEY uq_plantel (id_partido, id_jugador),
    CONSTRAINT fk_pp_partido FOREIGN KEY (id_partido)
        REFERENCES partidos (id_partido)  ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_pp_jugador FOREIGN KEY (id_jugador)
        REFERENCES jugadores (id_jugador) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;
CREATE TABLE eventos_partido (
    id_evento   INT AUTO_INCREMENT PRIMARY KEY,
    id_partido  INT NOT NULL,
    id_jugador  INT NOT NULL,
    tipo_evento ENUM('TRY','CONVERSION','PENAL','DROP',
                     'TARJETA_AMARILLA','TARJETA_ROJA','SUSTITUCION') NOT NULL,
    minuto      INT,
    descripcion VARCHAR(200),
    CONSTRAINT fk_ev_partido FOREIGN KEY (id_partido)
        REFERENCES partidos (id_partido)  ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ev_jugador FOREIGN KEY (id_jugador)
        REFERENCES jugadores (id_jugador) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;
CREATE INDEX idx_jug_categoria ON jugadores (id_categoria);
CREATE INDEX idx_ev_partido    ON eventos_partido (id_partido);
CREATE INDEX idx_ev_jugador    ON eventos_partido (id_jugador);
CREATE INDEX idx_par_temporada ON partidos (id_temporada);
