package uta.analisis;

import uta.abb.ArbolABB;
import uta.abb.Estudiante;
import uta.abb.ResultadoBusqueda;

/**
 * Compara el costo de búsqueda (contador de comparaciones) en dos ABB con las mismas n claves:
 *  - CADENA: inserciones ascendentes 1..n  -> altura n-1, búsqueda O(n);
 *  - BALANCEADO: inserciones por punto medio -> altura ~ floor(log2 n), búsqueda O(log n).
 * El ABB no se autoequilibra: la diferencia proviene únicamente del ORDEN de inserción.
 */
public final class AnalisisBusqueda {
    private AnalisisBusqueda() {}

    public record Reporte(int n,
                          int alturaCadena, int alturaBalanceado,
                          int existenteCadena, int existenteBalanceado,
                          int inexistenteCadena, int inexistenteBalanceado,
                          double promedioCadena, double promedioBalanceado) {}

    /** Claves 1..n en orden ascendente (produce una cadena). Arreglo, no ArrayList. */
    public static int[] ordenAscendente(int n) {
        int[] r = new int[n];
        llenarAscendente(r, 0);
        return r;
    }

    private static void llenarAscendente(int[] r, int i) {
        if (i == r.length) return;              // caso base
        r[i] = i + 1;
        llenarAscendente(r, i + 1);
    }

    /** Orden de inserción por punto medio de 1..n (genera un ABB de altura mínima). */
    public static int[] ordenBalanceado(int n) {
        int[] r = new int[n];
        medio(1, n, r, 0);
        return r;
    }

    /** Escribe el punto medio y luego cada mitad; devuelve la siguiente posición libre. */
    private static int medio(int ini, int fin, int[] r, int pos) {
        if (ini > fin) return pos;              // caso base
        int m = (ini + fin) >>> 1;
        r[pos] = m;
        int siguiente = medio(ini, m - 1, r, pos + 1);
        return medio(m + 1, fin, r, siguiente);
    }

    public static ArbolABB construir(int[] orden) {
        ArbolABB arbol = new ArbolABB();
        for (int k : orden) arbol.insertar(new Estudiante(k, "Estudiante " + k, 8.0));
        return arbol;
    }

    /** Ejecuta el análisis. Existente = mayor clave (n); inexistente = n + 1. Requiere n >= 1. */
    public static Reporte analizar(int n) {
        if (n < 1) throw new IllegalArgumentException("n debe ser >= 1");
        ArbolABB cadena = construir(ordenAscendente(n));
        ArbolABB balanceado = construir(ordenBalanceado(n));
        return new Reporte(n,
                cadena.altura(), balanceado.altura(),
                cadena.buscarContando(n).comparaciones(),
                balanceado.buscarContando(n).comparaciones(),
                cadena.buscarContando(n + 1).comparaciones(),
                balanceado.buscarContando(n + 1).comparaciones(),
                promedioComparaciones(cadena, n),
                promedioComparaciones(balanceado, n));
    }

    private static double promedioComparaciones(ArbolABB arbol, int n) {
        long suma = 0;
        for (int k = 1; k <= n; k++) {
            ResultadoBusqueda r = arbol.buscarContando(k);
            suma += r.comparaciones();
        }
        return (double) suma / n;
    }

    public static String formatear(Reporte r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Análisis de búsqueda con n = ").append(r.n()).append(" matrículas\n");
        sb.append(String.format("%-38s %12s %12s%n", "", "CADENA", "BALANCEADO"));
        sb.append(String.format("%-38s %12d %12d%n", "Altura (aristas)", r.alturaCadena(), r.alturaBalanceado()));
        sb.append(String.format("%-38s %12d %12d%n", "Comparaciones, clave existente (n)", r.existenteCadena(), r.existenteBalanceado()));
        sb.append(String.format("%-38s %12d %12d%n", "Comparaciones, clave inexistente (n+1)", r.inexistenteCadena(), r.inexistenteBalanceado()));
        sb.append(String.format(java.util.Locale.US, "%-38s %12.2f %12.2f%n", "Promedio sobre todas las claves", r.promedioCadena(), r.promedioBalanceado()));
        sb.append("Conclusión: la búsqueda cuesta O(h). En la cadena h = n-1 => O(n); en el árbol\n")
          .append("construido por punto medio h ≈ log2(n) => O(log n). El ABB simple NO se autoequilibra:\n")
          .append("la forma depende solo del orden de inserción.\n");
        return sb.toString();
    }
}
