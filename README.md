# SistRUGBY-SLTC

**Sistema de Gestión de Partidos y Estadísticas de Rugby**
Santiago Lawn Tennis Club — Santiago del Estero, Argentina

> Seminario de Práctica · Licenciatura en Informática · Universidad Siglo 21 · 2026
> Alumno: Fonzo, Ignacio · Entrega **AP4** (versión final integradora)

## Descripción
SistRUGBY-SLTC centraliza la gestión operativa de la sección rugby del SLTC:
registro de jugadores, partidos y eventos, y estadísticas por temporada.
Stack: **Java SE 17 · JDBC · MySQL 8.0**. Arquitectura en 3 capas.
Patrón protagonista: **DAO** (con Singleton y Factory Method de apoyo).

## Novedades AP4
- Conexión real a MySQL completada en todos los DAOs (alta, consulta, update, baja).
- Modo dual robusto: si no hay MySQL, cae a memoria sin abortar.
- Hashes PBKDF2-HMAC-SHA256 reales en `datos_iniciales.sql` (login funciona en MySQL).
- Arreglos + ArrayList complementarios (`int[]` fijo + `ArrayList` dinámico).
- Persistencia en archivos: `ReporteService` exporta a `.txt` (CU09).

## Ejecución

### Modo demo (sin MySQL)
```bash
javac -d out -encoding UTF-8 $(find src/main/java -name "*.java")
java -cp out com.sltc.sistrugby.Main
java -cp out com.sltc.sistrugby.Main < smoke_input.txt
```

### Modo producción (con MySQL)
```bash
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/datos_iniciales.sql
export DB_PASS='tu_password'
java -cp out:lib/mysql-connector-j-8.3.0.jar com.sltc.sistrugby.Main
```
En Windows: `;` en el classpath y `set DB_PASS=...`.

## Usuarios de prueba
| Usuario | Contraseña | Rol |
| --- | --- | --- |
| admin | admin2026 | ADMINISTRADOR |
| entrenador1 | rugby2026 | ENTRENADOR |
| secretario1 | sltc2026 | SECRETARIO |

Contraseñas con hash PBKDF2-HMAC-SHA256 (100.000 iteraciones, salt de 16 bytes).

## Estructura
- `src/main/java/com/sltc/sistrugby/` — modelo, persistencia (DAO), negocio, util, presentacion
- `sql/schema.sql` — 8 tablas 3FN con FK e índices
- `sql/datos_iniciales.sql` — DML con hashes PBKDF2 reales + SELECT de prueba
- `docs/` — AP1, AP2, AP3, AP4 y diagramas UML

Proyecto académico — Universidad Siglo 21 · 2026.
