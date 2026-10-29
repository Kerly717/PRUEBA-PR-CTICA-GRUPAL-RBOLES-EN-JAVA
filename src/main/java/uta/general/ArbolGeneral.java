package uta.general;

import java.util.ArrayList;

/**
 * Árbol general de unidades académicas.
 *
 * Reglas del caso:
 *  - los códigos de unidad son únicos;
 *  - no se admiten ciclos;
 *  - una unidad no raíz solo puede eliminarse si es hoja;
 *  - la raíz no puede eliminarse.
 *
 * Convención: altura en ARISTAS (árbol vacío = -1, un solo nodo = 0); nivel de la raíz = 0.
 * Los códigos se normalizan (trim + mayúsculas), por lo que "fisei" y "FISEI" son la misma unidad.
 */
public class ArbolGeneral {

    /** Resultado de agregar o eliminar una unidad, con la causa del rechazo. */
    public enum Resultado {
        OK("Operación realizada"),
        CODIGO_VACIO("El código no puede estar vacío"),
        ARBOL_VACIO("El árbol no tiene raíz"),
        PADRE_INEXISTENTE("El padre no existe"),
        CODIGO_DUPLICADO("El código ya existe en la jerarquía"),
        NO_EXISTE("La unidad no existe"),
        TIENE_HIJOS("La unidad tiene descendientes; solo se eliminan hojas"),
        RAIZ_PROTEGIDA("La raíz no puede eliminarse");

        public final String mensaje;
        Resultado(String mensaje) { this.mensaje = mensaje; }
        public boolean ok() { return this == OK; }
        @Override public String toString() { return name() + " (" + mensaje + ")"; }
    }

    private NodoGeneral raiz;

    public boolean estaVacio() {
        return raiz == null;
    }

    public NodoGeneral getRaiz() {
        return raiz;
    }

    private static String normalizar(String codigo) {
        if (codigo == null) return null;
        String c = codigo.trim().toUpperCase();
        return c.isEmpty() ? null : c;
    }

    // ---------------------------------------------------------------- Construcción

    /** Crea la raíz; solo es posible si el árbol está vacío. */
    public boolean crearRaiz(String codigo) {
        String c = normalizar(codigo);
        if (c == null || raiz != null) return false;
        raiz = new NodoGeneral(c);
        return true;
    }

    /** Agrega una unidad hija validando código no vacío, único y padre existente. */
    public boolean agregarUnidad(String codigoPadre, String codigo) {
        return agregarUnidadDetallado(codigoPadre, codigo).ok();
    }

    /** Igual que agregarUnidad, pero informa la causa del rechazo. */
    public Resultado agregarUnidadDetallado(String codigoPadre, String codigo) {
        String p = normalizar(codigoPadre);
        String c = normalizar(codigo);
        if (p == null || c == null) return Resultado.CODIGO_VACIO;
        if (raiz == null) return Resultado.ARBOL_VACIO;
        NodoGeneral padre = buscar(p);
        if (padre == null) return Resultado.PADRE_INEXISTENTE;
        if (buscar(c) != null) return Resultado.CODIGO_DUPLICADO;
        padre.agregarHijo(new NodoGeneral(c));
        return Resultado.OK;
    }

    // ---------------------------------------------------------------- Consultas

    /** Búsqueda por código. O(n). Devuelve null si no existe. */
    public NodoGeneral buscar(String codigo) {
        String c = normalizar(codigo);
        if (c == null) return null;
        return buscar(raiz, c);
    }

    private NodoGeneral buscar(NodoGeneral nodo, String codigo) {
        if (nodo == null) return null;                       // caso base: vacío
        if (nodo.getCodigo().equals(codigo)) return nodo;    // caso base: encontrado
        for (NodoGeneral hijo : nodo.getHijos()) {
            NodoGeneral r = buscar(hijo, codigo);
            if (r != null) return r;
        }
        return null;
    }

    /** Nivel de una unidad (raíz = 0). -1 si no existe. */
    public int nivel(String codigo) {
        String c = normalizar(codigo);
        if (c == null) return -1;
        return nivel(raiz, c, 0);
    }

    private int nivel(NodoGeneral nodo, String codigo, int actual) {
        if (nodo == null) return -1;
        if (nodo.getCodigo().equals(codigo)) return actual;
        for (NodoGeneral hijo : nodo.getHijos()) {
            int r = nivel(hijo, codigo, actual + 1);
            if (r != -1) return r;
        }
        return -1;
    }

    /** Grado = número de hijos directos. -1 si la unidad no existe. */
    public int grado(String codigo) {
        NodoGeneral n = buscar(codigo);
        return n == null ? -1 : n.getHijos().size();
    }

    /** Altura en aristas: vacío = -1, hoja/singleton = 0. */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoGeneral nodo) {
        if (nodo == null) return -1;
        int max = -1;
        for (NodoGeneral hijo : nodo.getHijos()) {
            max = Math.max(max, altura(hijo));
        }
        return max + 1;   // sin hijos: -1 + 1 = 0
    }

    public int contarNodos() {
        return contarNodos(raiz);
    }

    private int contarNodos(NodoGeneral nodo) {
        if (nodo == null) return 0;
        int total = 1;
        for (NodoGeneral hijo : nodo.getHijos()) total += contarNodos(hijo);
        return total;
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoGeneral nodo) {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return 1;
        int total = 0;
        for (NodoGeneral hijo : nodo.getHijos()) total += contarHojas(hijo);
        return total;
    }

    /** Recorrido preorden: lista de códigos. */
    public ArrayList<String> preorden() {
        ArrayList<String> r = new ArrayList<>();
        preorden(raiz, r);
        return r;
    }

    private void preorden(NodoGeneral nodo, ArrayList<String> r) {
        if (nodo == null) return;
        r.add(nodo.getCodigo());
        for (NodoGeneral hijo : nodo.getHijos()) preorden(hijo, r);
    }

    /** Jerarquía con sangría (2 espacios por nivel). */
    public String imprimirJerarquia() {
        StringBuilder sb = new StringBuilder();
        imprimir(raiz, 0, sb);
        return sb.toString();
    }

    private void imprimir(NodoGeneral nodo, int nivel, StringBuilder sb) {
        if (nodo == null) return;
        sb.append("  ".repeat(nivel)).append(nodo.getCodigo()).append('\n');
        for (NodoGeneral hijo : nodo.getHijos()) imprimir(hijo, nivel + 1, sb);
    }

    // ---------------------------------------------------------------- Modificaciones seguras

    /**
     * Eliminación segura: rechaza la raíz, unidades inexistentes y unidades con descendientes.
     * Devuelve true solo si realmente eliminó una hoja no raíz.
     */
    public boolean eliminar(String codigo) {
        return eliminarDetallado(codigo).ok();
    }

    /** Igual que eliminar, pero informa la causa del rechazo. */
    public Resultado eliminarDetallado(String codigo) {
        String c = normalizar(codigo);
        if (c == null) return Resultado.CODIGO_VACIO;
        if (raiz == null) return Resultado.ARBOL_VACIO;
        NodoGeneral nodo = buscar(raiz, c);
        if (nodo == null) return Resultado.NO_EXISTE;
        if (nodo == raiz) return Resultado.RAIZ_PROTEGIDA;      // la raíz no se elimina
        if (!nodo.esHoja()) return Resultado.TIENE_HIJOS;       // con descendencia: rechazado
        NodoGeneral padre = buscarPadre(raiz, nodo);
        return padre != null && padre.quitarHijo(nodo) ? Resultado.OK : Resultado.NO_EXISTE;
    }

    private NodoGeneral buscarPadre(NodoGeneral actual, NodoGeneral objetivo) {
        if (actual == null) return null;
        for (NodoGeneral hijo : actual.getHijos()) {
            if (hijo == objetivo) return actual;
            NodoGeneral r = buscarPadre(hijo, objetivo);
            if (r != null) return r;
        }
        return null;
    }

    /**
     * Mueve una unidad (con su subárbol) bajo otro padre. Rechaza ciclos: el nuevo padre
     * no puede ser la propia unidad ni uno de sus descendientes. La raíz no se mueve.
     */
    public boolean reubicarUnidad(String codigo, String nuevoPadre) {
        NodoGeneral nodo = buscar(codigo);
        NodoGeneral destino = buscar(nuevoPadre);
        if (nodo == null || destino == null || nodo == raiz) return false;
        if (contiene(nodo, destino)) return false;            // evitaría un ciclo
        NodoGeneral padreActual = buscarPadre(raiz, nodo);
        if (padreActual == destino) return true;               // ya está allí
        padreActual.quitarHijo(nodo);
        destino.agregarHijo(nodo);
        return true;
    }

    /** true si 'objetivo' está en el subárbol de 'subraiz' (incluida ella misma). */
    private boolean contiene(NodoGeneral subraiz, NodoGeneral objetivo) {
        if (subraiz == objetivo) return true;
        for (NodoGeneral hijo : subraiz.getHijos()) {
            if (contiene(hijo, objetivo)) return true;
        }
        return false;
    }

    /**
     * Verifica integridad: códigos únicos y sin ciclos.
     * Un ciclo haría visitar dos veces el mismo nodo y, por tanto, repetir su código; por eso
     * basta detectar un código repetido, y el recorrido se detiene ahí (termina aunque haya ciclo).
     * No usa ArrayList (la guía lo reserva para hijos y resultados de recorridos): los códigos
     * vistos se guardan en un arreglo propio que crece al duplicarse. O(n²) por la búsqueda lineal.
     */
    public boolean esJerarquiaValida() {
        return validar(raiz, new Vistos());
    }

    /** Arreglo dinámico mínimo de códigos ya visitados. */
    private static final class Vistos {
        private String[] datos = new String[16];
        private int cantidad = 0;

        boolean contiene(String codigo) { return contiene(codigo, 0); }

        private boolean contiene(String codigo, int i) {          // recursivo, caso base: fin del arreglo
            if (i == cantidad) return false;
            return datos[i].equals(codigo) || contiene(codigo, i + 1);
        }

        void agregar(String codigo) {
            if (cantidad == datos.length) {
                String[] mayor = new String[datos.length * 2];
                System.arraycopy(datos, 0, mayor, 0, cantidad);
                datos = mayor;
            }
            datos[cantidad++] = codigo;
        }
    }

    private boolean validar(NodoGeneral nodo, Vistos vistos) {
        if (nodo == null) return true;
        if (vistos.contiene(nodo.getCodigo())) return false;     // código duplicado o nodo repetido (ciclo)
        vistos.agregar(nodo.getCodigo());
        for (NodoGeneral hijo : nodo.getHijos()) {
            if (!validar(hijo, vistos)) return false;
        }
        return true;
    }
}
