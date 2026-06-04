# SistRUGBY-SLTC

**Sistema de Gestión de Partidos y Estadísticas de Rugby**
Santiago Lawn Tennis Club — Santiago del Estero, Argentina

> Proyecto desarrollado en el marco de **Seminario de Práctica** —
> Licenciatura en Informática · Universidad Siglo 21 · 2026
> Alumno: Fonzo, Ignacio

---

## Descripción

SistRUGBY-SLTC es una aplicación de escritorio que centraliza la gestión operativa
de la sección rugby del SLTC: registro de jugadores, partidos y eventos deportivos,
y generación de estadísticas por temporada.

**Metodología:** Proceso Unificado de Desarrollo (PUD)
**Stack:** Java SE 17 · MySQL 8.0 · JDBC · Java Swing (AP2) · Menú por consola (TP3)

---

## Estado del prototipo

| Entrega | Foco | Estado |
| --- | --- | --- |
| **AP1** | Análisis y definición del proyecto | ✅ Entregado |
| **AP2** | Diseño, base de datos, prototipo inicial | ✅ Entregado |
| **AP3 (TP3)** | Implementación del prototipo en Java con POO | ✅ **Entrega actual** |
| AP4   | Transición y despliegue | 🔄 Próximo |

### Casos de uso implementados en AP3

| Caso de Uso | Estado |
| --- | --- |
| CU01 — Iniciar sesión           | ✅ Implementado (PBKDF2 + 3 intentos) |
| CU02 — Registrar jugador        | ✅ Implementado (validación + DNI único) |
| CU03 — Modificar datos jugador  | ✅ Implementado |
| CU04 — Dar de baja jugador      | ✅ Implementado (baja lógica) |
| CU05 — Registrar partido        | ✅ Implementado |
| CU06 — Registrar plantel        | ✅ Implementado |
| CU07 — Registrar evento         | ✅ Implementado (polimórfico) |
| CU08 — Consultar estadísticas   | ✅ Implementado (ranking polimórfico) |
| CU09 — Generar reporte          | 🔄 AP4 |

---

## Aplicación de los pilares de POO (TP3)

| Pilar | Materialización en el código |
| --- | --- |
| **Abstracción**   | Clases abstractas `modelo/Persona.java` y `modelo/eventos/EventoPartido.java` |
| **Herencia**      | `Jugador`/`Usuario extends Persona`; 7 subclases extienden `EventoPartido` |
| **Encapsulamiento** | Atributos `private` + getters/setters + enums tipados (`Estado`, `Rol`, `Tipo`) |
| **Polimorfismo**  | `EventoPartido.calcularPuntos()` sobreescrito por cada subclase; recálculo de marcador y ranking sin `instanceof` |

### Estructuras de datos aplicadas

| Estructura | Clase Java | Uso |
| --- | --- | --- |
| Lista enlazada | `LinkedList<EventoPartido>` | Eventos de un partido en orden cronológico |
| Pila (LIFO)    | `ArrayDeque<EventoPartido>`  | Operación "deshacer último evento" |
| Cola (FIFO)    | `Queue<Integer>`             | Sustituciones planificadas |
| HashMap        | `Map<Integer, T>`            | Acceso O(1) por id en modo memoria |

### Algoritmos manuales

- **QuickSort** con mediana de tres en `util/OrdenadorJugadores.java` — O(n log n) esperado
- **Búsqueda binaria** en `util/BuscadorJugadores.java` — O(log n) sobre lista ordenada

### Patrones de diseño

- **Singleton** — `persistencia/ConexionBD.java`
- **DAO**       — 6 DAOs en `persistencia/`
- **Layered Architecture** — Presentación → Negocio → Persistencia
- **Factory Method** — `EventoPartido.crear()`

---

## Estructura del proyecto

```
SistRUGBY-SLTC/
├── src/main/java/com/sltc/sistrugby/
│   ├── Main.java                          → Punto de entrada
│   ├── modelo/                            → POJOs del dominio
│   │   ├── Persona.java                   → ABSTRACTA (abstracción)
│   │   ├── Jugador.java                   → extends Persona (herencia)
│   │   ├── Usuario.java                   → extends Persona (herencia)
│   │   ├── Partido.java
│   │   ├── PlantelPartido.java
│   │   ├── Categoria.java
│   │   ├── Club.java
│   │   ├── Temporada.java
│   │   └── eventos/                       → POLIMORFISMO
│   │       ├── EventoPartido.java         → ABSTRACTA
│   │       ├── Try.java                   → calcularPuntos() = 5
│   │       ├── Conversion.java            → = 2
│   │       ├── Penal.java                 → = 3
│   │       ├── Drop.java                  → = 3
│   │       ├── TarjetaAmarilla.java       → = 0
│   │       ├── TarjetaRoja.java           → = 0
│   │       └── Sustitucion.java           → = 0
│   ├── excepciones/                       → 4 excepciones de dominio
│   ├── persistencia/                      → DAOs + Singleton + modo memoria
│   ├── negocio/                           → Services (5)
│   ├── util/                              → Hash PBKDF2, QuickSort, binaria
│   └── presentacion/                      → MenuConsola + seeder
├── sql/
│   ├── schema.sql                         → DDL: 8 tablas con FK
│   └── datos_iniciales.sql                → DML inicial
├── docs/
│   ├── FONZO-IGNACIO-AP1.pdf
│   ├── FONZO-IGNACIO-AP2.pdf
│   ├── FONZO-IGNACIO-AP3.pdf              → ⭐ Entrega actual
│   └── diagramas/                         → Diagramas UML (PNG)
├── smoke_input.txt                        → Entradas de prueba scripteada
└── README.md
```

---

## Requisitos

| Componente | Versión mínima |
| --- | --- |
| Java Development Kit | 17 (LTS) |
| MySQL Server         | 8.0.x (solo modo producción) |
| MySQL Connector/J    | 8.3.0 (incluido en `lib/`) |
| Sistema operativo    | Windows 10/11, Linux o macOS |

---

## Modos de ejecución

El prototipo TP3 ofrece **dos modos**: uno demo en memoria (recomendado para corrección)
y uno productivo conectado a MySQL.

### 🎯 Modo demo (sin MySQL) — recomendado para corrección

No requiere MySQL instalado. La aplicación detecta la ausencia de `DB_PASS` y activa
automáticamente un repositorio en memoria con datos precargados.

```bash
# Compilar
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out -encoding UTF-8 --source 17 --target 17 @sources.txt

# Ejecutar interactivamente
java -Dfile.encoding=UTF-8 -cp out com.sltc.sistrugby.Main

# Ejecutar prueba automatizada
java -Dfile.encoding=UTF-8 -cp out com.sltc.sistrugby.Main < smoke_input.txt
```

### 🚀 Modo producción (con MySQL)

```bash
# 1) Crear base de datos
mysql -u root -p < sql/schema.sql
mysql -u root -p < sql/datos_iniciales.sql

# 2) Crear usuario de aplicación (permisos mínimos)
mysql -u root -p <<'SQL'
CREATE USER 'sistrugby_user'@'localhost' IDENTIFIED BY 'tu_password_seguro';
GRANT SELECT, INSERT, UPDATE, DELETE ON sistrugby_sltc.* TO 'sistrugby_user'@'localhost';
FLUSH PRIVILEGES;
SQL

# 3) Variables de entorno (Linux / macOS)
export DB_URL='jdbc:mysql://localhost:3306/sistrugby_sltc?useSSL=false'
export DB_USER='sistrugby_user'
export DB_PASS='tu_password_seguro'

# Windows (PowerShell)
$env:DB_URL='jdbc:mysql://localhost:3306/sistrugby_sltc?useSSL=false'
$env:DB_USER='sistrugby_user'
$env:DB_PASS='tu_password_seguro'

# 4) Ejecutar con el driver JDBC en el classpath
java -Dfile.encoding=UTF-8 \
     -cp out:lib/mysql-connector-j-8.3.0.jar \
     com.sltc.sistrugby.Main
```

---

## Credenciales de prueba

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `admin`         | `admin2026` | ADMINISTRADOR |
| `entrenador1`   | `rugby2026` | ENTRENADOR |
| `secretario1`   | `sltc2026`  | SECRETARIO |

> Las contraseñas se almacenan con hash **PBKDF2-HMAC-SHA256** (100.000 iteraciones,
> salt aleatorio de 16 bytes, comparación en tiempo constante). El algoritmo está
> disponible directamente en el JDK estándar (`javax.crypto`), evitando dependencias
> externas. Cumple NIST SP 800-132. Los hashes en `datos_iniciales.sql` son de
> prueba y se regeneran en cada arranque de modo demo.

---

## Divisiones del sistema

| División | Tipo |
| --- | --- |
| M15, M16, M17, M18, M19 | Juvenil |
| Pre-Intermedia, Intermedia, Primera | Plantel superior |

---

## Documentación técnica

Ver carpeta [`docs/`](docs/) para los documentos AP1, AP2 y **AP3**, además
de los diagramas UML:

- Diagrama de Dominio (UML 2.x)
- Diagrama de Casos de Uso
- Diagramas de Secuencia (CU01, CU05, CU07)
- Diagrama de Clases de Diseño
- Diagrama de Componentes
- Diagrama de Despliegue
- Diagrama Entidad-Relación
- Jerarquía POO del prototipo TP3 (nuevo)

---

## Federación

El SLTC está afiliado a la **Unión Santiagueña de Rugby (USR)**.

---

## Licencia

Proyecto académico — Uso exclusivo para Seminario de Práctica · Licenciatura en Informática · 2026.
