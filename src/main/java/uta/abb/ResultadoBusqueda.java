package uta.abb;

/**
 * Resultado de una búsqueda instrumentada.
 * 'comparaciones' = número de nodos cuya clave se comparó con la buscada
 * (cada nodo visitado cuenta una comparación).
 */
public record ResultadoBusqueda(Estudiante estudiante, int comparaciones) {
    public boolean encontrado() {
        return estudiante != null;
    }
}
