package com.sltc.sistrugby.util;

import java.util.List;

import com.sltc.sistrugby.modelo.Jugador;

/**
 * Algoritmo de ORDENACIÓN manual sobre la lista de jugadores.
 *
 * Se implementa QuickSort recursivo (in-place) para ordenar por apellido
 * + nombre alfabéticamente. Complejidad esperada O(n log n), peor caso
 * O(n^2) cuando el pivote es el menor o el mayor.
 *
 * Se incluye también un método auxiliar de selección de pivote por la
 * técnica "mediana de tres" para mitigar el peor caso con listas
 * parcialmente ordenadas.
 */
public final class OrdenadorJugadores {

    private OrdenadorJugadores() { }

    public static void ordenarPorApellido(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.size() < 2) return;
        quickSort(jugadores, 0, jugadores.size() - 1);
    }

    private static void quickSort(List<Jugador> lista, int izq, int der) {
        if (izq < der) {
            int p = particionar(lista, izq, der);
            quickSort(lista, izq, p - 1);
            quickSort(lista, p + 1, der);
        }
    }

    private static int particionar(List<Jugador> lista, int izq, int der) {
        // Mediana de tres: minimiza el peor caso O(n^2)
        int medio = izq + (der - izq) / 2;
        if (compararJugadores(lista.get(izq), lista.get(medio)) > 0) intercambiar(lista, izq, medio);
        if (compararJugadores(lista.get(izq), lista.get(der)) > 0) intercambiar(lista, izq, der);
        if (compararJugadores(lista.get(medio), lista.get(der)) > 0) intercambiar(lista, medio, der);

        Jugador pivote = lista.get(medio);
        intercambiar(lista, medio, der);

        int i = izq - 1;
        for (int j = izq; j < der; j++) {
            if (compararJugadores(lista.get(j), pivote) <= 0) {
                i++;
                intercambiar(lista, i, j);
            }
        }
        intercambiar(lista, i + 1, der);
        return i + 1;
    }

    private static int compararJugadores(Jugador a, Jugador b) {
        int cmp = a.getApellido().compareToIgnoreCase(b.getApellido());
        if (cmp != 0) return cmp;
        return a.getNombre().compareToIgnoreCase(b.getNombre());
    }

    private static void intercambiar(List<Jugador> lista, int i, int j) {
        Jugador tmp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, tmp);
    }
}
