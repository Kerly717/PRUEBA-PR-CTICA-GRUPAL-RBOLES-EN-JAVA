package uta.abb;

import java.util.ArrayList;

/**
 * Árbol binario de búsqueda de matrículas (sin duplicados).
 * Altura en aristas: vacío = -1, un solo nodo = 0. Implementación recursiva.
 * Nota: este ABB NO se autoequilibra; su forma depende del orden de inserción.
 */
public class ArbolABB {
    private NodoABB raiz;

    public boolean estaVacio() {
        return raiz == null;
    }

    // ---------------------------------------------------------------- Inserción

    /** Inserta recursivamente. Rechaza (false) nulos y matrículas duplicadas. */
    public boolean insertar(Estudiante e) {
        if (e == null || buscar(e.getMatricula()) != null) return false;
        raiz = insertar(raiz, e);
        return true;
    }

    private NodoABB insertar(NodoABB nodo, Estudiante e) {
        if (nodo == null) return new NodoABB(e);                 // caso base
        if (e.getMatricula() < nodo.clave()) nodo.izquierdo = insertar(nodo.izquierdo, e);
        else nodo.derecho = insertar(nodo.derecho, e);
        return nodo;
    }

    // ---------------------------------------------------------------- Búsqueda

    public Estudiante buscar(int matricula) {
        return buscarContando(matricula).estudiante();
    }

    /** Búsqueda recursiva que registra el número de comparaciones. O(h). */
    public ResultadoBusqueda buscarContando(int matricula) {
        return buscar(raiz, matricula, 0);
    }

    private ResultadoBusqueda buscar(NodoABB nodo, int matricula, int comparaciones) {
        if (nodo == null) return new ResultadoBusqueda(null, comparaciones);   // caso base: no existe
        comparaciones++;                                                       // compara con nodo.clave()
        if (matricula == nodo.clave()) return new ResultadoBusqueda(nodo.dato, comparaciones);
        if (matricula < nodo.clave()) return buscar(nodo.izquierdo, matricula, comparaciones);
        return buscar(nodo.derecho, matricula, comparaciones);
    }

    // ---------------------------------------------------------------- Recorridos

    public ArrayList<Integer> preorden() {
        ArrayList<Integer> r = new ArrayList<>();
        preorden(raiz, r);
        return r;
    }

    private void preorden(NodoABB n, ArrayList<Integer> r) {
        if (n == null) return;
        r.add(n.clave());
        preorden(n.izquierdo, r);
        preorden(n.derecho, r);
    }

    public ArrayList<Integer> inorden() {
        ArrayList<Integer> r = new ArrayList<>();
        inorden(raiz, r);
        return r;
    }

    private void inorden(NodoABB n, ArrayList<Integer> r) {
        if (n == null) return;
        inorden(n.izquierdo, r);
        r.add(n.clave());
        inorden(n.derecho, r);
    }

    public ArrayList<Integer> posorden() {
        ArrayList<Integer> r = new ArrayList<>();
        posorden(raiz, r);
        return r;
    }

    private void posorden(NodoABB n, ArrayList<Integer> r) {
        if (n == null) return;
        posorden(n.izquierdo, r);
        posorden(n.derecho, r);
        r.add(n.clave());
    }

    /** Inorden con los datos completos de cada estudiante. */
    public ArrayList<Estudiante> inordenEstudiantes() {
        ArrayList<Estudiante> r = new ArrayList<>();
        inordenEstudiantes(raiz, r);
        return r;
    }

    private void inordenEstudiantes(NodoABB n, ArrayList<Estudiante> r) {
        if (n == null) return;
        inordenEstudiantes(n.izquierdo, r);
        r.add(n.dato);
        inordenEstudiantes(n.derecho, r);
    }

    // ---------------------------------------------------------------- Modificación sin alterar la clave

    /**
     * Actualiza nombre y promedio SIN cambiar la matrícula (la clave determina la posición;
     * cambiarla in situ rompería el invariante del ABB). false si no existe o los datos son inválidos.
     */
    public boolean modificar(int matricula, String nuevoNombre, double nuevoPromedio) {
        Estudiante e = buscar(matricula);
        if (e == null) return false;
        try {
            e.actualizar(nuevoNombre, nuevoPromedio);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------- Eliminación (3 casos)

    /** Elimina una matrícula: hoja, un hijo o dos hijos (sucesor inorden). false si no existe. */
    public boolean eliminar(int matricula) {
        if (buscar(matricula) == null) return false;
        raiz = eliminar(raiz, matricula);
        return true;
    }

    private NodoABB eliminar(NodoABB nodo, int matricula) {
        if (nodo == null) return null;
        if (matricula < nodo.clave()) {
            nodo.izquierdo = eliminar(nodo.izquierdo, matricula);
        } else if (matricula > nodo.clave()) {
            nodo.derecho = eliminar(nodo.derecho, matricula);
        } else {
            if (nodo.izquierdo == null) return nodo.derecho;     // caso 1 (hoja) y caso 2 (solo hijo derecho)
            if (nodo.derecho == null) return nodo.izquierdo;     // caso 2 (solo hijo izquierdo)
            NodoABB sucesor = minimo(nodo.derecho);              // caso 3: sucesor inorden
            nodo.dato = sucesor.dato;
            nodo.derecho = eliminar(nodo.derecho, sucesor.clave());
        }
        return nodo;
    }

    // ---------------------------------------------------------------- Métricas

    /** Altura en aristas: vacío = -1, singleton = 0. */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoABB n) {
        if (n == null) return -1;
        return 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
    }

    public int contarNodos() {
        return contarNodos(raiz);
    }

    private int contarNodos(NodoABB n) {
        return n == null ? 0 : 1 + contarNodos(n.izquierdo) + contarNodos(n.derecho);
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoABB n) {
        if (n == null) return 0;
        if (n.izquierdo == null && n.derecho == null) return 1;
        return contarHojas(n.izquierdo) + contarHojas(n.derecho);
    }

    /** Estudiante con la menor matrícula, o null si está vacío. */
    public Estudiante minimo() {
        return raiz == null ? null : minimo(raiz).dato;
    }

    private NodoABB minimo(NodoABB n) {
        return n.izquierdo == null ? n : minimo(n.izquierdo);
    }

    /** Estudiante con la mayor matrícula, o null si está vacío. */
    public Estudiante maximo() {
        return raiz == null ? null : maximo(raiz).dato;
    }

    private NodoABB maximo(NodoABB n) {
        return n.derecho == null ? n : maximo(n.derecho);
    }

    /** Promedio aritmético de los promedios registrados (0.0 si el árbol está vacío). */
    public double promedioGeneral() {
        int n = contarNodos();
        return n == 0 ? 0.0 : sumaPromedios(raiz) / n;
    }

    private double sumaPromedios(NodoABB n) {
        if (n == null) return 0.0;
        return n.dato.getPromedio() + sumaPromedios(n.izquierdo) + sumaPromedios(n.derecho);
    }

    // ---------------------------------------------------------------- Invariante

    /** Verifica que todo nodo cumpla izquierda < nodo < derecha usando límites. O(n). */
    public boolean esABBValido() {
        return validar(raiz, null, null);
    }

    private boolean validar(NodoABB n, Integer min, Integer max) {
        if (n == null) return true;
        if (min != null && n.clave() <= min) return false;
        if (max != null && n.clave() >= max) return false;
        return validar(n.izquierdo, min, n.clave()) && validar(n.derecho, n.clave(), max);
    }

    /** Dibujo lateral del árbol (raíz a la izquierda, derecha arriba). */
    public String imprimirEstructura() {
        StringBuilder sb = new StringBuilder();
        estructura(raiz, 0, sb);
        return sb.toString();
    }

    private void estructura(NodoABB n, int nivel, StringBuilder sb) {
        if (n == null) return;
        estructura(n.derecho, nivel + 1, sb);
        sb.append("    ".repeat(nivel)).append(n.clave()).append('\n');
        estructura(n.izquierdo, nivel + 1, sb);
    }
}
