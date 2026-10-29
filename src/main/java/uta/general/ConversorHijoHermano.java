package uta.general;

/**
 * Conversión árbol general <-> árbol binario hijo-izquierdo / hermano-derecho.
 *
 * Interpretación correcta: el enlace izquierdo apunta al PRIMER HIJO y el derecho al
 * SIGUIENTE HERMANO. Error a evitar: leer el enlace derecho como "hijo" (no lo es) o creer
 * que la altura/nivel del binario coinciden con los del árbol general.
 */
public final class ConversorHijoHermano {
    private ConversorHijoHermano() {}

    public static NodoHH convertir(ArbolGeneral arbol) {
        if (arbol == null || arbol.estaVacio()) return null;
        return convertir(arbol.getRaiz());
    }

    private static NodoHH convertir(NodoGeneral nodo) {
        NodoHH r = new NodoHH(nodo.getCodigo());
        NodoHH anterior = null;
        for (NodoGeneral hijo : nodo.getHijos()) {
            NodoHH h = convertir(hijo);
            if (anterior == null) r.primerHijo = h;
            else anterior.hermanoDerecho = h;
            anterior = h;
        }
        return r;
    }

    /** Reconstruye el árbol general original a partir de la forma binaria. */
    public static ArbolGeneral reconstruir(NodoHH raiz) {
        ArbolGeneral arbol = new ArbolGeneral();
        if (raiz == null) return arbol;
        if (raiz.hermanoDerecho != null) {
            throw new IllegalArgumentException("La raíz no puede tener hermano derecho.");
        }
        arbol.crearRaiz(raiz.codigo);
        reconstruir(raiz, arbol);
        return arbol;
    }

    private static void reconstruir(NodoHH nodo, ArbolGeneral arbol) {
        for (NodoHH h = nodo.primerHijo; h != null; h = h.hermanoDerecho) {
            arbol.agregarUnidad(nodo.codigo, h.codigo);
            reconstruir(h, arbol);
        }
    }

    /** Altura en aristas de la forma binaria (suele diferir de la altura del árbol general). */
    public static int alturaBinaria(NodoHH nodo) {
        if (nodo == null) return -1;
        return 1 + Math.max(alturaBinaria(nodo.primerHijo), alturaBinaria(nodo.hermanoDerecho));
    }

    /** Texto con las relaciones de cada nodo. */
    public static String describir(NodoHH raiz) {
        StringBuilder sb = new StringBuilder();
        describir(raiz, sb);
        return sb.toString();
    }

    private static void describir(NodoHH n, StringBuilder sb) {
        if (n == null) return;
        sb.append(n.codigo)
          .append("  [primer hijo: ").append(n.primerHijo == null ? "-" : n.primerHijo.codigo)
          .append(" | hermano derecho: ").append(n.hermanoDerecho == null ? "-" : n.hermanoDerecho.codigo)
          .append("]\n");
        describir(n.primerHijo, sb);
        describir(n.hermanoDerecho, sb);
    }
}
