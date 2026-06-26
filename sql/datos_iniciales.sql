-- ============================================================
-- SistRUGBY-SLTC  |  datos_iniciales.sql  |  MySQL 8.0
-- Datos iniciales del prototipo (AP4). Unificados con el seeder
-- de modo memoria. Hashes PBKDF2-HMAC-SHA256 REALES y verificados.
--   admin/admin2026 | entrenador1/rugby2026 | secretario1/sltc2026
-- ============================================================
USE sistrugby_sltc;
DELETE FROM eventos_partido;
DELETE FROM partido_plantel;
DELETE FROM partidos;
DELETE FROM jugadores;
DELETE FROM temporadas;
DELETE FROM clubes;
DELETE FROM categorias;
DELETE FROM usuarios;
ALTER TABLE usuarios   AUTO_INCREMENT = 1;
ALTER TABLE categorias AUTO_INCREMENT = 1;
ALTER TABLE clubes     AUTO_INCREMENT = 1;
ALTER TABLE temporadas AUTO_INCREMENT = 1;
ALTER TABLE jugadores  AUTO_INCREMENT = 1;
ALTER TABLE partidos   AUTO_INCREMENT = 1;

INSERT INTO usuarios (nombre_usuario, contrasena_hash, rol, activo) VALUES
 ('admin',
  '100000:LloOsSInf3AbimYFrNadJA==:Y/z6nLTvxGpvUhK9nr36jCQooe/NdI9bLXDNubrBwVk=',
  'ADMINISTRADOR', 1),
 ('entrenador1',
  '100000:OM8IH9/a7NcKaPAvjh/w4Q==:uA+BJsmiWyNz6EHhtbaR8WyHtVSjQ0OQ/n5iQvyZsWU=',
  'ENTRENADOR', 1),
 ('secretario1',
  '100000:YO1/tDA9d6OUU5qNMwd5zw==:/YQphzUY+LrWyGYMFJWFQgGvesxJgnzBhq5XvUR6TIU=',
  'SECRETARIO', 1);

INSERT INTO categorias (nombre, tipo, activo) VALUES
 ('M15','JUVENIL',1),('M16','JUVENIL',1),('M17','JUVENIL',1),
 ('M18','JUVENIL',1),('M19','JUVENIL',1),
 ('Pre-Intermedia','PLANTEL_SUPERIOR',1),
 ('Intermedia','PLANTEL_SUPERIOR',1),
 ('Primera','PLANTEL_SUPERIOR',1);

INSERT INTO clubes (nombre, union_pertenencia, contacto, activo) VALUES
 ('Lince Rugby Club','USR',NULL,1),
 ('Santiago Rugby Club','USR',NULL,1),
 ('Catamarca Rugby Club','URC',NULL,1),
 ('Tucuman Rugby Club','URT',NULL,1),
 ('Club Atletico del Norte','USR',NULL,1);

INSERT INTO temporadas (anio, descripcion) VALUES
 (2024,'Temporada 2024'),(2025,'Temporada 2025'),(2026,'Temporada 2026');

INSERT INTO jugadores
 (nombre,apellido,dni,fecha_nacimiento,posicion,id_categoria,estado,fecha_alta) VALUES
 ('Rodrigo','Pereyra','35211001','2000-03-14','Pilar',8,'activo','2024-03-01'),
 ('Matias','Villalba','36089452','2001-07-22','Hooker',8,'activo','2024-03-01'),
 ('Federico','Casas','34567890','1999-11-05','Apertura',8,'activo','2024-03-01'),
 ('Gonzalo','Herrera','37124500','2002-02-18','Ala derecho',8,'activo','2024-03-01'),
 ('Tomas','Ruiz','35980012','2000-09-30','Zaguero',8,'activo','2024-03-01');

INSERT INTO partidos
 (fecha,id_club_rival,id_categoria,id_temporada,sede,pts_local,pts_visitante,estado) VALUES
 ('2026-05-10',1,8,3,'LOCAL',15,0,'FINALIZADO');

INSERT INTO partido_plantel (id_partido,id_jugador,condicion) VALUES
 (1,1,'TITULAR'),(1,2,'TITULAR'),(1,3,'TITULAR'),(1,4,'TITULAR'),(1,5,'TITULAR');

INSERT INTO eventos_partido (id_partido,id_jugador,tipo_evento,minuto) VALUES
 (1,3,'TRY',12),(1,3,'CONVERSION',13),(1,1,'TRY',28),(1,3,'PENAL',55);

-- Estadisticas por jugador (temporada 2026)
SELECT j.apellido, j.nombre, c.nombre AS categoria,
       SUM(e.tipo_evento='TRY') AS tries,
       SUM(CASE e.tipo_evento WHEN 'TRY' THEN 5 WHEN 'CONVERSION' THEN 2
           WHEN 'PENAL' THEN 3 WHEN 'DROP' THEN 3 ELSE 0 END) AS puntos
FROM jugadores j
JOIN categorias c ON j.id_categoria=c.id_categoria
LEFT JOIN eventos_partido e ON e.id_jugador=j.id_jugador
WHERE j.estado='activo'
GROUP BY j.apellido, j.nombre, c.nombre
ORDER BY puntos DESC;
