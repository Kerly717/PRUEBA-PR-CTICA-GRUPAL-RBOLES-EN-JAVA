package uta.abb;

/** Nodo del ABB. La clave de ordenamiento es la matrícula del estudiante. */
class NodoABB {
    Estudiante dato;
    NodoABB izquierdo;
    NodoABB derecho;

    NodoABB(Estudiante dato) {
        this.dato = dato;
    }

    int clave() {
        return dato.getMatricula();
    }
}
