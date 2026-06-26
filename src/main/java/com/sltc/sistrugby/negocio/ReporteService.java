package com.sltc.sistrugby.negocio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.sltc.sistrugby.modelo.eventos.EventoPartido;

/**
 * Servicio de generacion de reportes (CU09).
 *
 * <h3>Persistencia en ARCHIVOS (consigna AP4)</h3>
 * Exporta el ranking de jugadores y el resumen de eventos a un archivo de
 * texto plano. La escritura usa {@code try-with-resources} sobre un
 * {@link BufferedWriter} (cierre automatico del recurso) y declara/maneja
 * {@link IOException}, complementando la persistencia en MySQL con un
 * soporte de salida en archivo apto para imprimir o compartir.
 *
 * <h3>Uso complementario de arreglos y ArrayList</h3>
 * El reporte combina el {@code ArrayList<FilaRanking>} (tamano dinamico) con
 * el arreglo {@code int[]} de conteo por tipo de evento (tamano fijo), ambos
 * provistos por {@link EstadisticaService}.
 */
public class ReporteService {

    private final EstadisticaService estadSvc = new EstadisticaService();

    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Genera el reporte de estadisticas y lo escribe en {@code rutaArchivo}.
     *
     * @param rutaArchivo ruta destino (por ejemplo "reporte_sltc.txt").
     * @return la ruta absoluta del archivo escrito.
     * @throws IOException  si ocurre un error de E/S al escribir.
     * @throws SQLException si falla la lectura de datos (modo MySQL).
     */
    public String exportarReporte(String rutaArchivo) throws IOException, SQLException {
        List<EstadisticaService.FilaRanking> ranking = estadSvc.rankingPorPuntos();
        int[] conteoPorTipo = estadSvc.conteoGlobalPorTipo();   // arreglo fijo

        Path destino = Path.of(rutaArchivo);
        // try-with-resources: el BufferedWriter se cierra automaticamente.
        try (BufferedWriter w = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            w.write("============================================================");
            w.newLine();
            w.write(" SistRUGBY-SLTC - Reporte de estadisticas (CU09)");
            w.newLine();
            w.write(" Generado: " + LocalDateTime.now().format(TS));
            w.newLine();
            w.write("============================================================");
            w.newLine();
            w.newLine();

            // ----- Ranking de jugadores (ArrayList dinamico) -----
            w.write("RANKING DE JUGADORES POR PUNTOS");
            w.newLine();
            w.write(String.format("%-25s | %-4s | %-4s | %-4s | %-4s | %s",
                    "JUGADOR", "TRY", "CONV", "PEN", "DROP", "PUNTOS"));
            w.newLine();
            w.write("------------------------------------------------------------");
            w.newLine();
            if (ranking.isEmpty()) {
                w.write("(Sin eventos registrados)");
                w.newLine();
            } else {
                for (EstadisticaService.FilaRanking f : ranking) {
                    String nom = f.jugador != null && f.jugador.getNombreCompleto() != null
                            ? f.jugador.getNombreCompleto() : "#" + f.jugador.getId();
                    w.write(String.format("%-25s | %-4d | %-4d | %-4d | %-4d | %d",
                            nom, f.tries, f.conversiones, f.penales, f.drops, f.puntosTotal));
                    w.newLine();
                }
            }

            // ----- Resumen por tipo de evento (arreglo int[]) -----
            w.newLine();
            w.write("RESUMEN GLOBAL POR TIPO DE EVENTO");
            w.newLine();
            w.write("------------------------------------------------------------");
            w.newLine();
            EventoPartido.Tipo[] tipos = EventoPartido.Tipo.values(); // arreglo
            for (int i = 0; i < tipos.length; i++) {
                w.write(String.format("%-18s : %d", tipos[i], conteoPorTipo[i]));
                w.newLine();
            }
            w.newLine();
            w.write("Fin del reporte.");
            w.newLine();
        }
        return destino.toAbsolutePath().toString();
    }

    /**
     * Variante que envuelve IOException como excepcion no verificada, util
     * cuando el llamador no puede declarar checked exceptions.
     */
    public String exportarReporteSeguro(String rutaArchivo) throws SQLException {
        try {
            return exportarReporte(rutaArchivo);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir el reporte", e);
        }
    }
}
