package uta.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nodo de un árbol general (unidad académica).
 * Los hijos se guardan en un ArrayList (único uso permitido de ArrayList en la estructura).
 */
public class NodoGeneral {
    private final String codigo;
    private final ArrayList<NodoGeneral> hijos = new ArrayList<>();

    NodoGeneral(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    /** Vista de solo lectura de los hijos. */
    public List<NodoGeneral> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    void agregarHijo(NodoGeneral hijo) {
        hijos.add(hijo);
    }

    boolean quitarHijo(NodoGeneral hijo) {
        return hijos.remove(hijo);
    }

    boolean esHoja() {
        return hijos.isEmpty();
    }
}
