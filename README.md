# SistRUGBY-SLTC

**Sistema de Gestión de Partidos y Estadísticas de Rugby**  
Santiago Lawn Tennis Club — Santiago del Estero, Argentina

> Proyecto desarrollado en el marco de **Seminario de Práctica** —  
> Licenciatura en Informática · 2026  
> Alumno: Fonzo, Ignacio

---

## Descripción

SistRUGBY-SLTC es una aplicación de escritorio que centraliza la gestión operativa de la sección rugby del SLTC:
registro de jugadores, partidos y eventos deportivos, y generación de estadísticas por temporada.

Metodología: **Proceso Unificado de Desarrollo (PUD)**  
Stack: **Java SE 17 · Java Swing · MySQL 8.0 · JDBC**

---

## Requisitos del sistema

| Componente           | Versión mínima     |
|----------------------|--------------------|
| Java Runtime (JRE)   | 17                 |
| MySQL Server         | 8.0.x              |
| MySQL Connector/J    | 8.3.0 (incluido)   |
| Sistema operativo    | Windows 10/11 (64-bit) |

---

## Instalación

### 1. Base de datos

Ejecutar en MySQL Workbench o en la consola de MySQL:

```sql
SOURCE sql/schema.sql;
SOURCE sql/datos_iniciales.sql;
```

Esto crea la base de datos `sistrugby_sltc` con todas las tablas,
restricciones de integridad referencial y datos de prueba.

### 2. Usuario de base de datos

Crear el usuario de aplicación con los permisos mínimos necesarios:

```sql
CREATE USER 'sistrugby_user'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT SELECT, INSERT, UPDATE, DELETE ON sistrugby_sltc.* TO 'sistrugby_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Variable de entorno

La contraseña de MySQL **no se incluye en el código fuente**.
Definir la variable de entorno `DB_PASS` antes de ejecutar:

```bat
rem Windows (cmd)
set DB_PASS=tu_contraseña_segura

rem Windows (PowerShell)
$env:DB_PASS = "tu_contraseña_segura"
```

```bash
# Linux / macOS
export DB_PASS=tu_contraseña_segura
```

### 4. Compilación y ejecución

```bash
# Compilar (requiere mysql-connector-j-8.3.0.jar en el classpath)
javac -cp ".;lib/mysql-connector-j-8.3.0.jar;lib/jbcrypt-0.4.jar" ^
      src/main/java/com/sltc/sistrugby/**/*.java

# Ejecutar
java -cp ".;lib/mysql-connector-j-8.3.0.jar;lib/jbcrypt-0.4.jar" ^
     com.sltc.sistrugby.presentacion.MainFrame
```

> Para Linux/macOS reemplazar `;` por `:` en el classpath.

---

## Usuarios de prueba

| Usuario       | Contraseña  | Rol            |
|---------------|-------------|----------------|
| `admin`       | `admin2026` | ADMINISTRADOR  |
| `entrenador1` | `rugby2026` | ENTRENADOR     |
| `secretario1` | `sltc2026`  | SECRETARIO     |

> Las contraseñas se almacenan con hash **bcrypt** (cost=12).
> Los hashes en `datos_iniciales.sql` son de prueba y deben regenerarse en producción.

---

## Estructura del proyecto

```
SistRUGBY-SLTC/
├── src/main/java/com/sltc/sistrugby/
│   ├── modelo/           → POJOs del dominio (Jugador, Partido, Usuario, etc.)
│   ├── persistencia/     → ConexionDB + interfaces DAO + implementaciones JDBC
│   │   ├── dao/          → Interfaces: JugadorDAO, PartidoDAO, EventoPartidoDAO
│   │   └── impl/         → Implementaciones: JugadorDAOImpl, PartidoDAOImpl
│   ├── negocio/          → Servicios: JugadorService, PartidoService
│   └── presentacion/     → GUI Swing: MainFrame, LoginPanel, JugadoresPanel
├── sql/
│   ├── schema.sql         → DDL: CREATE DATABASE + 6 tablas con FK e índices
│   └── datos_iniciales.sql → DML: INSERT + SELECT + DELETE de prueba
├── docs/
│   ├── FONZO-IGNACIO-AP1.pdf
│   ├── FONZO-IGNACIO-AP2.pdf
│   └── diagramas/         → Diagramas UML exportados (PNG)
└── README.md
```

---

## Divisiones del sistema

| División         | Tipo            |
|------------------|-----------------|
| M15              | Juvenil         |
| M16              | Juvenil         |
| M17              | Juvenil         |
| M18              | Juvenil         |
| M19              | Juvenil         |
| Pre-Intermedia   | Plantel Superior|
| Intermedia       | Plantel Superior|
| Primera          | Plantel Superior|

---

## Estado del prototipo (AP2 — Sprint 1 y 2)

| Caso de Uso                     | Estado       |
|---------------------------------|--------------|
| CU01 — Iniciar sesión           | ✅ Implementado |
| CU02 — Registrar jugador        | ✅ Implementado |
| CU03 — Modificar datos jugador  | ✅ Implementado |
| CU04 — Dar de baja jugador      | ✅ Implementado |
| CU05 — Registrar partido        | 🔄 Sprint 3    |
| CU06 — Registrar plantel        | 🔄 Sprint 3    |
| CU07 — Registrar evento         | 🔄 Sprint 3    |
| CU08 — Consultar estadísticas   | 🔄 Sprint 4    |
| CU09 — Generar reporte          | 🔄 Sprint 4    |

---

## Documentación técnica

Ver carpeta [`docs/`](docs/) para los documentos AP1 y AP2 y los diagramas UML:
- Diagrama de Clases de Análisis
- Diagramas de Secuencia (CU01, CU05)
- Diagrama de Clases de Diseño
- Diagrama de Componentes
- Diagrama de Despliegue
- Diagrama Entidad-Relación (DER)

---

## Federación

El SLTC está afiliado a la **Unión Santiagueña de Rugby (USR)**.

---

## Licencia

Proyecto académico — Uso exclusivo para Seminario de Práctica · Licenciatura en Informática · 2026.
