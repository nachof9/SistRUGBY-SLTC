package com.sltc.sistrugby.util;

import java.util.List;

import com.sltc.sistrugby.modelo.Jugador;

/**
 * Algoritmo de BÚSQUEDA binaria sobre lista de jugadores ordenada por DNI.
 *
 * Complejidad temporal: O(log n).
 * Precondición: la lista debe estar previamente ordenada por DNI.
 *
 * Si la lista no está ordenada, el método ordenar() la ordena por DNI antes
 * de delegar en la búsqueda binaria propiamente dicha.
 */
public final class BuscadorJugadores {

    private BuscadorJugadores() { }

    /**
     * Busca un jugador por DNI usando búsqueda binaria iterativa.
     * @return el jugador encontrado o null si no existe.
     */
    public static Jugador buscarPorDni(List<Jugador> ordenadosPorDni, String dni) {
        if (ordenadosPorDni == null || ordenadosPorDni.isEmpty() || dni == null) return null;

        int izq = 0;
        int der = ordenadosPorDni.size() - 1;

        while (izq <= der) {
            int medio = izq + (der - izq) / 2;          // evita overflow
            Jugador j = ordenadosPorDni.get(medio);
            int cmp = j.getDni().compareTo(dni);

            if (cmp == 0) return j;
            if (cmp < 0)  izq = medio + 1;
            else          der = medio - 1;
        }
        return null;
    }

    /**
     * Ordena por DNI (Insertion Sort, eficiente con n pequeño y O(n)
     * cuando la lista ya está casi ordenada).
     */
    public static void ordenarPorDni(List<Jugador> lista) {
        if (lista == null || lista.size() < 2) return;
        for (int i = 1; i < lista.size(); i++) {
            Jugador actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && lista.get(j).getDni().compareTo(actual.getDni()) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }
}
