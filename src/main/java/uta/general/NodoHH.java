package uta.general;

/** Nodo de la representación binaria hijo-izquierdo / hermano-derecho. */
public class NodoHH {
    public final String codigo;
    public NodoHH primerHijo;       // "izquierdo": primer hijo en el árbol general
    public NodoHH hermanoDerecho;   // "derecho": siguiente hermano en el árbol general

    public NodoHH(String codigo) {
        this.codigo = codigo;
    }
}
